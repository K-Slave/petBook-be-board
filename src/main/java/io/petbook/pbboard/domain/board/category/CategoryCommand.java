package io.petbook.pbboard.domain.board.category;

import lombok.Builder;
import lombok.Getter;

/**
 * [Kang] Category Writing Model
 */

public class CategoryCommand {
    @Getter
    @Builder
    public static class Main {
        private final String title;
        private final String userToken;
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
        private final String token;
        private final String title;
        private final Boolean visible;

        // [Kang] TODO : Data Validation Check
        // [Kang] 생각: 접속자의 토큰은 Security Header 에서 가져와서 처리해야 하는데, 수정자 객체에서 가공시켜야 할 이유가 있을까?
    }
}
