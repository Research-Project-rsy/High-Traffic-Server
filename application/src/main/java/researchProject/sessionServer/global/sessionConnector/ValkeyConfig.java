package researchProject.sessionServer.global.sessionConnector;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ValkeyConfig {

    // Write 인스턴스 Host
    @Value("${session.write.host}")
    private String sessionWriteHost;

    // Write 인스턴스 Port
    @Value("${session.write.port}")
    private int sessionWritePort;

    // Read1 인스턴스 Host
    @Value("${session.read1.host}")
    private String sessionRead1Host;

    // Read1 인스턴스 Port
    @Value("${session.read1.port}")
    private int sessionRead1Port;

    // Read2 인스턴스 Host
    @Value("${session.read2.host}")
    private String sessionRead2Host;

    // Read2 인스턴스 Port
    @Value("${session.read2.port}")
    private int sessionRead2Port;

    // ======================= Write =========================
    @Bean(name = "sessionWriteTemplate")
    public RedisTemplate<String, String> sessionWriteTemplate(
            @Qualifier("sessionPrimaryConnectionFactory") RedisConnectionFactory connectionFactory) {
        return buildTemplate(connectionFactory);
    }

    @Bean(name = "sessionPrimaryConnectionFactory")
    public RedisConnectionFactory sessionPrimaryConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(sessionWriteHost);
        config.setPort(sessionWritePort);
        return new LettuceConnectionFactory(config);
    }

    // ======================= Read (Round Robin) =========================
    private final AtomicInteger rrCounter = new AtomicInteger(0);

    @Bean(name = "sessionReadTemplate")
    public RedisTemplate<String, String> sessionReadTemplate() {
        return buildTemplate(nextReplicaConnectionFactory());
    }

    private RedisConnectionFactory nextReplicaConnectionFactory() {
        int index = rrCounter.getAndUpdate(i -> (i + 1) % 2);

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        if (index == 0) {
            config.setHostName(sessionRead1Host);
            config.setPort(sessionRead1Port);
        } else {
            config.setHostName(sessionRead2Host);
            config.setPort(sessionRead2Port);
        }
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();
        return factory;
    }

    // ======================= 공용 템플릿 빌더 =========================

    /* 증복 제거용 Helper 메서드 */
    // RedisTemplate 생성 시 중복되는 초기화 로직을 공용 메서드로 뺀 것.
    // sessionWriteTemplate / sessionReadTemplate 모두 동일한 방식으로 일관성 있게 초기화
    private RedisTemplate<String, String> buildTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    // ======================= Deprecated =========================

    // ======================= Redis Basic Config =========================

//    // 기본 이름의 RedisTemplate (Valkey용)
//    @Bean(name = "valkeyTemplate")
//    public RedisTemplate<String, String> valkeyTemplate(
//            @Qualifier("sessionRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
//        return sessionValkeyTemplate(connectionFactory); // 재사용
//    }

    // ======================= Session Redis  =========================

//    // Session Redis Connection Factory
//    @Bean(name = "sessionRedisConnectionFactory")
//    public RedisConnectionFactory sessionRedisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//        config.setHostName(sessionValkeyHost);
//        config.setPort(sessionValkeyPort);
//        return new LettuceConnectionFactory(config);
//    }
//
//    // Session RedisTemplate (데이터 저장용)
//    @Bean(name = "sessionValkeyTemplate")
//    public RedisTemplate<String, String> sessionValkeyTemplate(
//            @Qualifier("sessionRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        template.afterPropertiesSet(); // 필수
//        return template;
//    }
}
