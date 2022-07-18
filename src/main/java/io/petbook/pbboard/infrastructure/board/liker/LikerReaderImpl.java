package io.petbook.pbboard.infrastructure.board.liker;


import io.petbook.pbboard.domain.board.liker.Liker;
import io.petbook.pbboard.domain.board.liker.LikerReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikerReaderImpl implements LikerReader {
    private final RedisTemplate<String, Liker> redisTemplate;

    @Override
    public long countByArticleToken(String articleToken) {
        if (StringUtils.isEmpty(articleToken)) {
            return 0L;
        } else {
            String key = String.format("%s:%s:%s*",
                Liker.class.getSimpleName().toLowerCase(),
                "likeToken", // [Kang] TODO: likeToken Field 에 대해 Reflection 을 대응해야 하지 않을까?
                articleToken
            );

            return redisTemplate.opsForSet().size(key);
        }
    }

    @Override
    public boolean existByLikeToken(String likeToken) {
        if (StringUtils.isEmpty(likeToken)) {
            return false;
        } else {
            String key = String.format("%s:%s:%s*",
                Liker.class.getSimpleName().toLowerCase(),
                "likeToken", // [Kang] TODO: likeToken Field 에 대해 Reflection 을 대응해야 하지 않을까?
                likeToken
            );

            return redisTemplate.opsForSet().size(key) > 0;
        }
    }
}
