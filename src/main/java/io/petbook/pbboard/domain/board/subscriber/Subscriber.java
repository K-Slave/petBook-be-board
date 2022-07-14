package io.petbook.pbboard.domain.board.subscriber;

import io.petbook.pbboard.common.util.TokenGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * [Kang] Subscriber Redis Domain Entity
 * [Kang] 추가로 Serializable 를 상속하지 않더라도 암묵적으로 해두는 경우가 있다. 이는 한 번 더 참조해보자.
 * [Kang] 이 데이터는 Redis 에 쌓이기만 하고, 삭제 되지 않아야 한다.
 */
@Slf4j
@Getter
@NoArgsConstructor
@RedisHash("subscriber")
public class Subscriber implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SUBSCRIBER_PREFIX = "sbscr_";
    private static final String ANONYMOUS_PREFIX = "anms_";

    /**
     * [Kang] 접속자 로그 ID (이는 null 로 넣어도 임의의 값이 저장된다.)
     */
    @Id
    private String id;

    /**
     * [Kang] 조회 접속자 토큰
     */
    private String userToken;

    /**
     * [Kang] 조회 게시물 토큰
     * [Kang] JPA 메소드로 쿼링 하려면 Index 데이터여야 한다.
     */
    @Indexed
    private String articleToken;

    /**
     * [Kang] 조회 발생 시간
     */
    private LocalDateTime viewCreatedAt;

    /**
     * [Kang] 비회원 접속자 전용
     */
    public Subscriber(String articleToken) {
        this.userToken = TokenGenerator.randomCharacterWithPrefix(ANONYMOUS_PREFIX);
        this.articleToken = articleToken;
        this.viewCreatedAt = LocalDateTime.now();
    }

    /**
     * [Kang] 회원 접속자 전용
     * [Kang] 회원 접속 정보를 가져오기 위해 Header 에서 사용자 ID 와 토큰을 연동해야 한다.
     */
    public Subscriber(String userToken, String articleToken) {
        this.userToken = userToken;
        this.articleToken = articleToken;
        this.viewCreatedAt = LocalDateTime.now();
    }
}
