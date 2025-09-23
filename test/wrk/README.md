# Wrk 성능 테스트

이 디렉토리는 Wrk를 사용한 성능 테스트를 위한 스크립트와 도구들을 포함합니다.

## 디렉토리 구조

```
wrk/
├── scripts/                  # Lua 테스트 스크립트들
│   ├── post-session.lua     # 세션 쓰기 API 테스트
│   └── read-session.lua     # 세션 읽기 API 테스트
├── run-wrk.bat             # Wrk 실행 편의 스크립트
└── README.md               # 이 파일
```

## 사용법

### 1. 기본 GET 요청 테스트
```bash
# test 디렉토리에서 실행
cd test

# 기본 GET 요청 (스크립트 불필요)
docker-compose run --rm wrk -t12 -c400 -d30s http://localhost:8080/api/read
```

### 2. Lua 스크립트를 사용한 테스트

#### 세션 쓰기 API 테스트
```bash
# POST 요청으로 세션 데이터 생성
docker-compose run --rm wrk -t12 -c400 -d30s --script=wrk/scripts/post-session.lua http://localhost:8080/api/write
```

#### 세션 읽기 API 테스트 (동적 세션 ID)
```bash
# 랜덤 세션 ID로 GET 요청
docker-compose run --rm wrk -t12 -c400 -d30s --script=wrk/scripts/read-session.lua http://localhost:8080/api/read
```

### 3. 편의 스크립트 사용
```bash
# wrk 디렉토리에서 실행
cd wrk

# 기본 사용법
run-wrk.bat -t12 -c400 -d30s http://localhost:8080/api/read

# Lua 스크립트 사용
run-wrk.bat -t12 -c400 -d30s --script=scripts/post-session.lua http://localhost:8080/api/write
```

## Lua 스크립트 설명

### `post-session.lua`
- **목적**: 세션 서버 쓰기 API 테스트
- **기능**: 
  - 랜덤 세션 ID, 사용자 ID, 타임스탬프 생성
  - JSON 페이로드로 POST 요청 전송
  - 응답 상태 확인 및 통계 출력

### `read-session.lua`
- **목적**: 세션 서버 읽기 API 테스트
- **기능**:
  - 랜덤 세션 ID를 URL 파라미터로 전달
  - GET 요청으로 세션 데이터 조회
  - 응답 상태 확인

## 고급 사용법

### 더 강력한 테스트
```bash
# 더 많은 스레드와 연결로 테스트
docker-compose run --rm wrk -t20 -c1000 -d60s --script=wrk/scripts/post-session.lua http://localhost:8080/api/write

# 결과를 파일로 저장
docker-compose run --rm wrk -t12 -c400 -d30s --script=wrk/scripts/post-session.lua http://localhost:8080/api/write > wrk-results.txt
```

### 커스텀 스크립트 작성
1. `scripts/` 디렉토리에 새로운 `.lua` 파일 생성
2. Wrk Lua API 참고하여 스크립트 작성
3. `docker-compose run --rm wrk --script=wrk/scripts/your-script.lua` 명령으로 실행

