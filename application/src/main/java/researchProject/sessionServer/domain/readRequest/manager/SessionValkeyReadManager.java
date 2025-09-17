package researchProject.sessionServer.domain.readRequest.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionValkeyReadManager {

    @Qualifier("sessionValkeyTemplate")
    private final RedisTemplate<String, String> sessionValkeyTemplate;

    // 세션 서버에서 값 조회
    public String getValue(String key) {
        ValueOperations<String, String> ops = sessionValkeyTemplate.opsForValue();
        return ops.get(key);
    }

}
