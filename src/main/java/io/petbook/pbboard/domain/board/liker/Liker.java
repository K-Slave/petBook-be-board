package io.petbook.pbboard.domain.board.liker;

import io.petbook.pbboard.common.exception.InvalidLikeTokenException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * [Kang] Subscriber Redis Domain Entity
 * [Kang] 추가로 Serializable 를 상속하지 않더라도 암묵적으로 해두는 경우가 있다. 이는 한 번 더 참조해보자.
 * [Kang] 이 데이터는 Redis 에 쌓이기만 하고, 삭제 되지 않아야 한다.
 */
@Slf4j
@Getter
@NoArgsConstructor
@RedisHash("liker")
public class Liker {
    private static final long serialVersionUID = 1L;

    public static final String LIKER_SPLIT_REGEX = "_liker_";

    /**
     * [Kang] 좋아요 ID (이는 null 로 넣어도 임의의 값이 저장된다.)
     */
    @Id
    private String id;

    /**
     * [Kang] 좋아요 토큰 (게시물 + '_' + 사용자)
     */
    @Indexed
    private String likeToken;

    /**
     * [Kang] 조회 발생 시간
     */
    private LocalDateTime createdAt;

    public Liker(String articleToken, String userToken) {
        this.likeToken = this.initLikeToken(articleToken, userToken);
        this.createdAt = LocalDateTime.now();
    }

    public String initLikeToken(String articleToken, String userToken) {
        return String.format("%s%s%s", articleToken, LIKER_SPLIT_REGEX, userToken);
    }

    public void checkInvalidToken() {
        if (StringUtils.isEmpty(this.likeToken)) {
            throw new InvalidLikeTokenException("문자열이 아무 것도 존재하지 않습니다.");
        }
        if (!this.likeToken.contains(LIKER_SPLIT_REGEX)) {
            throw new InvalidLikeTokenException(String.format("좋아요 토큰 중 중간 글자 %s 를 포함시켜야 합니다.", LIKER_SPLIT_REGEX));
        }
    }

    public String getArticleToken() {
        this.checkInvalidToken();
        return this.likeToken.split(LIKER_SPLIT_REGEX)[0];
    }

    public String getUserToken() {
        this.checkInvalidToken();
        return this.likeToken.split(LIKER_SPLIT_REGEX)[1];
    }

    public boolean isLikerValid(String articleToken, String userToken) {
        this.checkInvalidToken();
        return this.likeToken.equals(this.initLikeToken(articleToken, userToken));
    }
}
