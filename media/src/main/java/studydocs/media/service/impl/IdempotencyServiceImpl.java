package studydocs.media.service.impl;

import studydocs.media.service.IdempotencyService;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {
    private final StringRedisTemplate redisTemplate;
    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void save(String key, String value, long expireInSeconds) {
        redisTemplate.opsForValue().set(key, value, expireInSeconds, TimeUnit.SECONDS);
    }
}
