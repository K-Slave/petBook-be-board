package io.petbook.pbboard.infrastructure.board.subscriber;

import io.petbook.pbboard.domain.board.article.Article;
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

    private final RedisTemplate<Subscriber, String> redisTemplate;

    @Override
    public long countByArticleToken(String articleToken) {
        if (articleToken == null) {
            return 0L;
        } else {
            return subscriberRepository.countByArticleToken(articleToken);
        }
    }
}
