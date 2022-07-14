package io.petbook.pbboard.domain.board.subscriber;

import io.petbook.pbboard.domain.board.category.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * [Kang] Subscriber Writing Model
 */
public class SubscriberCommand {
    @Getter
    @Builder
    public static class Anonymous {
        @ApiModelProperty(value = "게시물 토큰", required = true, example = "atcl_abcde12345")
        private final String articleToken;

        public Subscriber toEntity() {
            return new Subscriber(articleToken);
        }
    }

    @Getter
    @Builder
    public static class Accessor {
        @ApiModelProperty(value = "게시물 접속자", required = true, example = "user_abcde12345")
        private final String userToken;

        @ApiModelProperty(value = "게시물 토큰", required = true, example = "atcl_abcde12345")
        private final String articleToken;

        public Subscriber toEntity() {
            return new Subscriber(userToken, articleToken);
        }
    }
}
