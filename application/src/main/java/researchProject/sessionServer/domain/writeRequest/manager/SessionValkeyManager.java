package researchProject.sessionServer.domain.writeRequest.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SessionValkeyManager {

    @Qualifier("sessionValkeyTemplate")
    private final RedisTemplate<String, String> sessionValkeyTemplate;

    // 세션 서버에 값 저장
    public void setValue(String key, String value, long ttlMinutes) {
        ValueOperations<String, String> ops = sessionValkeyTemplate.opsForValue();
        ops.set(key, value, ttlMinutes, TimeUnit.MINUTES);
    }

    // 세션 서버에서 값 조회
    public String getValue(String key) {
        ValueOperations<String, String> ops = sessionValkeyTemplate.opsForValue();
        return ops.get(key);
    }
}
