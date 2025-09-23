# 성능 테스트 도구

이 디렉토리는 Wrk와 JMeter를 사용한 성능 테스트를 위한 Docker Compose 설정을 포함합니다.

## 사용법

### 1. Wrk 성능 테스트

#### 기본 사용법
```bash
# test 디렉토리에서 실행
cd test

# 기본 GET 요청 테스트 (12 threads, 400 connections, 30초)
docker-compose run --rm wrk -t12 -c400 -d30s http://localhost:8080/api/read

# POST 요청 테스트 (JSON 페이로드 포함)
docker-compose run --rm wrk -t12 -c400 -d30s --script=wrk/scripts/post-session.lua http://localhost:8080/api/write

# 특정 헤더 포함 테스트
docker-compose run --rm wrk -t12 -c400 -d30s -H "Content-Type: application/json" http://localhost:8080/api/read

# 읽기 API 테스트 (동적 세션 ID)
docker-compose run --rm wrk -t12 -c400 -d30s --script=wrk/scripts/read-session.lua http://localhost:8080/api/read
```

#### 고급 사용법
```bash
# 더 많은 스레드와 연결로 테스트
docker-compose run --rm wrk -t20 -c1000 -d60s http://localhost:8080/api/read

# 특정 스크립트 파일 사용
docker-compose run --rm wrk -t12 -c400 -d30s --script=wrk/scripts/post-session.lua http://localhost:8080/api/write

# 결과를 파일로 저장
docker-compose run --rm wrk -t12 -c400 -d30s http://localhost:8080/api/read > wrk-results.txt
```

### 2. JMeter 성능 테스트

#### 기본 사용법
```bash
# test 디렉토리에서 실행
cd test

# 테스트 계획 실행 (GUI 없이)
docker-compose run --rm jmeter -n -t test-plans/session-test.jmx -l results/test-results.jtl

# 테스트 계획 실행 후 HTML 리포트 생성
docker-compose run --rm jmeter -n -t test-plans/session-test.jmx -l results/test-results.jtl -e -o reports/html-report

# 특정 사용자 수와 램프업 시간으로 테스트
docker-compose run --rm jmeter -n -t test-plans/session-test.jmx -l results/test-results.jtl -Jusers=100 -Jrampup=60
```

#### 고급 사용법
```bash
# 여러 테스트 계획 실행
docker-compose run --rm jmeter -n -t test-plans/read-test.jmx -l results/read-results.jtl
docker-compose run --rm jmeter -n -t test-plans/write-test.jmx -l results/write-results.jtl

# 시스템 속성 설정
docker-compose run --rm jmeter -n -t test-plans/session-test.jmx -l results/test-results.jtl -Jserver.host=localhost -Jserver.port=8080

# 결과 파일에 타임스탬프 추가
docker-compose run --rm jmeter -n -t test-plans/session-test.jmx -l results/test-$(date +%Y%m%d_%H%M%S).jtl
```

## Lua 스크립트 파일들

### `post-session.lua`
- 세션 서버 쓰기 API 테스트용
- 랜덤 세션 ID, 사용자 ID, 타임스탬프 생성
- JSON 페이로드로 POST 요청 전송

### `read-session.lua`
- 세션 서버 읽기 API 테스트용
- 랜덤 세션 ID를 URL 파라미터로 전달
- GET 요청으로 세션 데이터 조회

## 디렉토리 구조

```
test/
├── docker-compose.yml          # Docker Compose 설정 파일
├── wrk/                       # Wrk 성능 테스트 도구
│   ├── scripts/              # Lua 테스트 스크립트들
│   │   ├── post-session.lua  # 세션 쓰기 테스트 스크립트
│   │   └── read-session.lua  # 세션 읽기 테스트 스크립트
│   ├── run-wrk.bat          # Wrk 실행 편의 스크립트
│   └── README.md            # Wrk 사용법
├── jmeter/                   # JMeter 관련 파일들
│   ├── test-plans/          # .jmx 테스트 계획 파일들
│   ├── results/             # 테스트 결과 파일들 (.jtl)
│   ├── reports/             # HTML 리포트 생성 위치
│   ├── run-jmeter.bat      # JMeter 실행 편의 스크립트
│   └── README.md            # JMeter 사용법
└── README.md                # 전체 사용법 (이 파일)
```

## 주의사항

1. **포트 확인**: 테스트 대상 서버가 실행 중인지 확인하세요
2. **리소스 모니터링**: 고성능 테스트 시 시스템 리소스를 모니터링하세요
3. **네트워크**: Docker 컨테이너는 `test-network`에서 실행되며, localhost로 접근 가능합니다
4. **결과 파일**: JMeter 결과 파일은 `jmeter/results/` 디렉토리에 저장됩니다
5. **Lua 스크립트**: POST 요청이나 복잡한 테스트를 위해서는 Lua 스크립트가 필요합니다

## 예시 테스트 시나리오

### 세션 서버 읽기 성능 테스트
```bash
# 100명의 동시 사용자, 60초간 테스트
docker-compose run --rm wrk -t10 -c100 -d60s http://localhost:8080/api/read

# 동적 세션 ID로 읽기 테스트
docker-compose run --rm wrk -t10 -c100 -d60s --script=wrk/scripts/read-session.lua http://localhost:8080/api/read
```

### 세션 서버 쓰기 성능 테스트
```bash
# POST 요청으로 세션 데이터 쓰기 테스트
docker-compose run --rm wrk -t10 -c50 -d60s --script=wrk/scripts/post-session.lua http://localhost:8080/api/write
```