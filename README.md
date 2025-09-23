## NoSQL(Valkey)기반 Session Server 안정화 연구 프로젝트

### 1. Connection 자원 비효율적 활용으로 인한, 트래픽 처리 Latency 발생 문제 해결 
#### Stiuation
* wrk 부하 테스트 시 지연(latency) 평균 700ms 이상, 처리량 50 req/s 수준, Socket timeout 발생
#### Task
* WAS 레이어의 Connection 프로세스를 개선하여, Latency를 최소화 시켜야 함  
#### Action 
* 싱글톤 패턴 적용 : Valkey Config의 ConnectionFactory, RedisTemplate 빈 재사용 
* 커넥션 풀 적용 : LettuceConnectionFactory 내부에서 TCP 연결을 Pooling 하여 Connection을 재사용
#### Result
| 구분       | Avg Latency | Req/Sec | Total Requests | Socket Timeout |
|----------| ----------- | ------- | -------------- | -------------- |
| **개선 전** | 724.94ms    | 8.03    | 1,491          | 99             |
| **개선 후** | 33.28ms     | 156.46  | 47,378         | 48             |

### 정리
현 실무 기준으로 봤을때,    
* 소규모 웹 서비스: 초당 수십~수백 req 수준    
* 중규모 서비스: 초당 수천~수만 req   
* 대규모 서비스: 초당 수십만~수백만 req

나의 모든 스레드를 합산한 req는 평균 1,580 req/s였다.  
이는 소규모 서비스 ~ 중규모 서비스 초입 수준은 커버가 가능하다는 의미이다.   
다음은 Thread, Connection, Redis 클러스터, Pool 튜닝등을 활용하여, 중규모 서비스까지 커버 가능한 시스템을 구축할 차례이다.



