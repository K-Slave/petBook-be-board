package io.petbook.pbboard.domain.board.article;

import io.petbook.pbboard.domain.board.category.Category;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [Kang] Article Writing Model
 */
public class ArticleCommand {
    @Getter
    @Builder
    public static class Main {
        @ApiModelProperty(value = "게시물 제목", required = true, example = "게시글을 등록합니다.")
        private final String title;

        @ApiModelProperty(value = "게시물 내용", required = true, example = "<p>게시물을 등록합니다.</p>")
        private final String context;

        @ApiModelProperty(value = "게시물 공개 여부", required = true, example = "true")
        private final Boolean visible;

        @ApiModelProperty(value = "게시물 작성자 토큰", required = true, example = "user_abcde12345")
        private final String userToken;

        @ApiModelProperty(value = "게시물 등록을 위한 카테고리 토큰", required = true, example = "ctgy_abcde12345")
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
        @ApiParam(value = "게시물 페이지", required = true, example = "1")
        private Integer pg = 1;

        @Builder.Default
        @ApiParam(value = "게시물 페이징 사이즈", required = true, example = "10")
        private Integer sz = 10;

        @ApiParam(value = "게시물 정렬 코드", example = "1")
        private Integer ob;

        @ApiParam(value = "게시물 검색 코드", example = "1")
        private Integer sb;

        @Builder.Default
        @ApiParam(value = "비밀글 여부", example = "false")
        private Boolean secret = false;

        @ApiParam(value = "게시물 검색어", example = "펫북")
        private final String st;

        @ApiParam(value = "카테고리 토큰", example = "ctgy_abcde12345")
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
        @ApiModelProperty(value = "게시물 토큰", required = true, example = "atcl_abcde12345")
        private final String token;

        @ApiModelProperty(value = "게시물 제목", required = true, example = "수정할 게시물 제목입니다.")
        private final String title;

        @ApiModelProperty(value = "게시물 내용", required = true, example = "<p>게시물을 수정합니다.</p>")
        private final String context;

        @ApiModelProperty(value = "게시물 공개 여부", required = true, example = "true")
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
