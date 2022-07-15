package io.petbook.pbboard.infrastructure.board.subscriber;

import io.petbook.pbboard.domain.board.subscriber.Subscriber;
import io.petbook.pbboard.domain.board.subscriber.SubscriberReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriberReaderImpl implements SubscriberReader {
    private final SubscriberRepository subscriberRepository;

    private final RedisTemplate<String, Subscriber> redisTemplate;

    @Override
    public long countByArticleToken(String articleToken) {
        if (articleToken == null) {
            return 0L;
        } else {
            // [Kang] Repository 인터페이스에 Method Query 로 만들면 작동이 안 된다.
            // [Kang] 그래서 어쩔 수 없이 RedisTemplate 를 사용하여 결과를 반환해야 한다.
            // [Kang] Repository 에서 List 를 반환하면 메모리를 많이 차지할 것이 뻔하기 때문에 Count 를 실행하는게 옳다.
            String key = String.format("%s:%s:%s",
                Subscriber.class.getSimpleName().toLowerCase(),
                "articleToken", // [Kang] TODO: articleToken Field 에 대해 Reflection 을 대응해야 하지 않을까?
                articleToken
            );

            if (redisTemplate.hasKey(key)) {
                return redisTemplate.opsForSet().size(key);
            } else {
                return 0L;
            }
        }
    }
}
