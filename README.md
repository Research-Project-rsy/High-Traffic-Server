### 현재  구현 완료 된 System Architecture

<img width="660" height="440" alt="HighTrafficArch" src="https://github.com/user-attachments/assets/2d7b086f-3bdc-4966-a720-b89a507c9520" />

#### 연구 기록 보관소(Notion) :    
https://knotty-toast-80a.notion.site/26b1979809dd800681eff595e8dbe3bd?source=copy_link  

---

### 구현 기록 Log (내림차순)

- [Setting] Valkey Instance 이중화 완료 → Primary (인스턴스 수 : 1) + Replica (인스턴스 수 : 2)
- [Setting] Replica의 Primary 데이터 실시간 복제 성공 → **( !!! 추가 과제 발생 : 복제 Cool Time 줄여서 Primary-Replica 간 데이터 불일치 시나리오 방지하기 !!!)**
- [Setting] Spring Boot - Primary Valkey Instance 연결 완료
- [트래픽 분산] Round Rofin 로드 밸런싱 전략 → 읽기 트래픽 다중 Replica에 균등 분배 성공
- [Test 진행] 동시 400 요청 연결 Test → 다양한 문제 발생 → 원인 추적을 위해 트래픽 감소
- [Test 진행] 동시 50 요청 연결 Test → WAS 영역에서 Read 트래픽 처리 Latency 발생
- [병목 Point 발견] Connection Pooling 로직의 문제 확인 → 높은 커넥션 생성/삭제 비용으로 인한 트래픽 처리 Latency 발생
- [성능 개선] SingleTon + Connection Pool로 **Latency 개선 시도 → Latency Time 약 21.8배 감소 (724.94ms → 33.28ms )**
- [중규모 트래픽 처리] 스레드 24, 커넥션 500, 기한 60초로 중규모 트레픽 테스트 → 3000 req/sec 달성
- [목표 설정] 대규모 서비스를 기준으로 서버 최적화 진행하기 → 10000 req/sec 목표 설정
- [성능 개선] WAS-DB 사이의 Connection Pool 8배 증설 + WAS의 Thread Pool 설정 → Total req/sec 10% 향상 성공
- [성능 개선 시도 - 실패] JVM 튜닝을 통한 성능 향상 시도 → Total req/sec 70% 감소
- [목표 설정] WAS/DB 수평 확장을 통한 대규모 트래픽 대응 → WAS 5/DB 15로 확장
- [목표 설정] Valkey Sentinel 추가 → Connection 로직 단순화 + Fail-Over 시스템 추가
- [Poc] Valkey Sentinel 프로세스 + 5배 수평 확장 서버 증설 테스트 환경 구축 완료
- [Poc - Issue] Valkey - Sentinel간의 연결 불가 이슈  -  Sentinel configuration에서 hostname을 resolve “sentinel resolve-hostnames yes”
- [Poc] WAS-Sentinel Layer - DB 라인 안정화 작업
- 현재 진행 중 ....
