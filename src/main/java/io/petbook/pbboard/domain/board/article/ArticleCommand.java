package io.petbook.pbboard.domain.board.article;

/**
 * [Kang] Article Writing Model
 */

import io.petbook.pbboard.domain.board.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class ArticleCommand {
    @Getter
    @Builder
    public static class Main {
        private final String title;
        private final String context;
        private final Boolean visible;
        private final String userToken;
        private final String categoryToken;

        public Article toEntity(Category category) {
            Article article = Article.builder()
                                .title(title)
                                .context(context)
                                .userToken(userToken)
                                .category(category)
                                .build();

            if (visible) {
                article.enable();
            } else {
                article.disable();
            }

            return article;
        }

        // [Kang] TODO : Data Validation Check
    }

    // [Kang] 페이징네이션에 대한 org.springframework.core.convert.ConversionFailedException 를 확인해보자.
    @Getter
    @Builder
    public static class Paginate {
        @Builder.Default
        private Integer pg = 1;

        @Builder.Default
        private Integer sz = 10;

        private Integer ob;
        private Integer sb;

        @Builder.Default
        private Boolean secret = false;

        private final String st;
        private final String ctgTk; // [Kang] 카테고리 토큰

        // [Kang] 비밀글 검색 여부는 secret 이 true 인 경우에만 실행한다.
        public boolean checkVisible() {
            return (this.secret == null || this.secret == false);
        }

        @Getter
        @RequiredArgsConstructor
        public enum OrderBy {
            // [Kang] 코드 순서는 향후에 바뀔 수도 있다.
            CREATED_AT_ASC(0),
            CREATED_AT_DESC(1),
            VIEW_COUNTS_DESC(2),
            LIKE_COUNTS_DESC(3),
            ACCURATE_DESC(4); // [Kang] TODO: 검색 결과에 대한 정확도 계산 향상

            private final int code;
        }

        @Getter
        @RequiredArgsConstructor
        public enum SearchBy {
            ALL_CONTAINS(1), // [Kang] 제목, 내용 모두 해당.
            TITLE_CONTAINS(2),
            CONTEXT_CONTAINS(3);

            // [Kang] 사용자 이름 정보는 User Domain 에서 얻어와 해결해야 하기 때문에 향후 생각해볼 것.

            private final int code;
        }

        // [Kang] Enumeration 데이터를 코드로 가져오기 위한 로직. (단, Ordinal 로 되어 있다는 점을 명심하라.)
        public static OrderBy loadObByIntCode(int code) {
            OrderBy[] enums = OrderBy.values();
            if (code >= enums.length || code < 0) {
                return null;
            }
            return enums[code];
        }

        // [Kang] Enumeration 데이터를 코드로 가져오기 위한 로직. (단, Ordinal 로 되어 있다는 점을 명심하라.)
        public static SearchBy loadSbByIntCode(int code) {
            SearchBy[] enums = SearchBy.values();
            if (code >= enums.length || code < 0) {
                return null;
            }
            return enums[code];
        }
    }

    /**
     * [Kang] Article 정보를 수정시켜 주는 객체
     */
    @Getter
    @Builder
    public static class Modifier {
        private final String token;
        private final String title;
        private final String context;
        private final Boolean visible;
    }

    /**
     * [Kang] ArticleInfo.Detail 데이터에 다른 정보 (조회수, 좋아요, 키워드 등) 을 주입하기 위한 객체
     */
    @Getter
    @Builder
    public static class InfoAccessor {
        private final Long likeCount;
        private final Long viewCount;
        private final String author;
    }
}
