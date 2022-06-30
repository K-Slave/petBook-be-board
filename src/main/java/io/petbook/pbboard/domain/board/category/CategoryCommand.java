package io.petbook.pbboard.domain.board.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * [Kang] Category Writing Model
 */

public class CategoryCommand {
    @Getter
    @Builder
    public static class Main {
        @ApiModelProperty(value = "카테고리 제목", required = true, example = "노하우 게시판")
        private final String title;

        @ApiModelProperty(value = "카테고리 등록을 위한 사용자 토큰", required = true, example = "user_abcde12345")
        private final String userToken;

        @ApiModelProperty(value = "카테고리 공개 여부", required = true, example = "true")
        private final Boolean visible;

        public Category toEntity() {
            return Category.builder()
                    .title(title)
                    .userToken(userToken)
                    .visible(visible)
                    .build();
        }

        // [Kang] TODO : Data Validation Check
    }

    @Getter
    @Builder
    public static class Modifier {
        @ApiModelProperty(value = "카테고리 토큰", required = true, example = "ctgy_abcde12345")
        private final String token;

        @ApiModelProperty(value = "카테고리 제목", required = true, example = "노하우 게시판")
        private final String title;

        @ApiModelProperty(value = "카테고리 공개 여부", required = true, example = "true")
        private final Boolean visible;

        // [Kang] TODO : Data Validation Check
        // [Kang] 생각: 접속자의 토큰은 Security Header 에서 가져와서 처리해야 하는데, 수정자 객체에서 가공시켜야 할 이유가 있을까?
    }
}

/**
 * https://minholee93.tistory.com/entry/ERROR-Cannot-construct-instancrre-of-no-Creators-like-default-construct-exist-cannot-deserialize-from-Object-value-no-delegate-or-property-based-Creator
 * cannot deserialize from Object value (no delegate- or property-based Creator)
 * @Builder / @Data 동시 선언되어 있는 경우... lombok 설정을 따로 해줘야 할 수도 있다. (1.8.0 버전 이상부터)
 */