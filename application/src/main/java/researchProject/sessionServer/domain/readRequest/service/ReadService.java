package researchProject.sessionServer.domain.readRequest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import researchProject.sessionServer.domain.readRequest.manager.SessionValkeyReadManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadService {

    /* Connection Pool / SingleTone 적용 X - 매 요청마다 Connection 연결&생성 */
    private final SessionValkeyReadManager sessionValkeyReadManager;



    // 세션 서버에 테스트 데이터 조회 메서드
    public String testRead() {

        // 세션 서버에 저장할 Key 설정
        String key = "test:currentTime";

        // 조회 메서드 호출
        String data = sessionValkeyReadManager.getValue(key);
        log.info("Session Server에 테스트 데이터 조회 완료 : key={}, data={}", key, data);

        return data;
    }
}
