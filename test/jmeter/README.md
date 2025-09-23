# JMeter 테스트 파일 디렉토리

이 디렉토리는 JMeter 테스트 계획(.jmx 파일)과 결과 파일들을 저장하는 곳입니다.

## 사용법

1. JMeter GUI에서 테스트 계획을 작성하여 `.jmx` 파일로 저장
2. 이 디렉토리에 `.jmx` 파일을 복사
3. Docker Compose를 사용하여 테스트 실행

## 예시 파일 구조

```
jmeter/
├── test-plans/
│   ├── session-read-test.jmx
│   └── session-write-test.jmx
├── results/
│   └── (결과 파일들이 여기에 저장됩니다)
└── reports/
    └── (HTML 리포트가 여기에 생성됩니다)
```
