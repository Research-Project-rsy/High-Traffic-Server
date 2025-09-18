package researchProject.sessionServer.domain.readRequest.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
// @RequiredArgsConstructor
public class SessionValkeyReadManager {

    // Read1 인스턴스 Host
    @Value("${session.read1.host}")
    private String host1;

    // Read1 인스턴스 Port
    @Value("${session.read1.port}")
    private int port1;

    // Read2 인스턴스 Host
    @Value("${session.read2.host}")
    private String host2;

    // Read2 인스턴스 Port
    @Value("${session.read2.port}")
    private int port2;

    private final AtomicInteger rrCounter = new AtomicInteger(0);

    // 호출마다 다른 replica 선택
    private RedisTemplate<String, String> nextTemplate() {
        int index = rrCounter.getAndUpdate(i -> (i + 1) % 2);

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        if (index == 0) {
            config.setHostName(host1);
            config.setPort(port1);
        } else {
            config.setHostName(host2);
            config.setPort(port2);
        }

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    // 세션 서버에서 값 조회
    public String getValue(String key) {
        RedisTemplate<String, String> template = nextTemplate();
        ValueOperations<String, String> ops = template.opsForValue();
        return ops.get(key);
    }

    // ======================= deprecated =========================

    /**
     * sessionReadTemplate 라운드 로빈 방식은 Bean 생성 시점에만 한 번 선택되는데,
     * 호출마다 다른 replica를 타야 한다면 Manager 쪽에서 ConnectionFactory를 직접 돌려주는 방식으로 바꿔야 합니다.
     * 현재 구조는 "애플리케이션 구동 시 한 번 선택된 replica만 계속 사용"
     *
     */
//    private final RedisTemplate<String, String> sessionValkeyTemplate;
//
//    public SessionValkeyReadManager(
//            @Qualifier("sessionReadTemplate") RedisTemplate<String, String> sessionValkeyTemplate) {
//        this.sessionValkeyTemplate = sessionValkeyTemplate;
//    }
//
//    // 세션 서버에서 값 조회
//    public String getValue(String key) {
//        ValueOperations<String, String> ops = sessionValkeyTemplate.opsForValue();
//        return ops.get(key);
//    }

}
