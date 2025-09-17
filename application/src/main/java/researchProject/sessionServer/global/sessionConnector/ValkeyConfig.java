package researchProject.sessionServer.global.sessionConnector;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ValkeyConfig {

    // Valkey Session Redis 설정
    @Value("${spring.data.valkey.session.host}")
    private String sessionValkeyHost;

    @Value("${spring.data.valkey.session.port}")
    private int sessionValkeyPort;

    // ======================= Redis Basic Config =========================

    // 기본 이름의 RedisTemplate (Valkey용)
    @Bean(name = "valkeyTemplate")
    public RedisTemplate<String, String> valkeyTemplate(
            @Qualifier("sessionRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        return sessionValkeyTemplate(connectionFactory); // 재사용
    }

    // ======================= Session Redis =========================

    // Session Redis Connection Factory
    @Bean(name = "sessionRedisConnectionFactory")
    public RedisConnectionFactory sessionRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(sessionValkeyHost);
        config.setPort(sessionValkeyPort);
        return new LettuceConnectionFactory(config);
    }

    // Session RedisTemplate (데이터 저장용)
    @Bean(name = "sessionValkeyTemplate")
    public RedisTemplate<String, String> sessionValkeyTemplate(
            @Qualifier("sessionRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet(); // 필수
        return template;
    }
}
