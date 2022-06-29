package io.petbook.pbboard.domain.board.category;

import lombok.Builder;
import lombok.Getter;

public class CategoryInfo {
    @Getter
    @Builder
    public static class Main {
        private final String token;
        private final String title;
        private final boolean visible;

        public static Main toInfo(Category category) {
            return Main.builder()
                    .token(category.getToken())
                    .visible(category.isVisible())
                    .title(category.isVisible() ? category.getTitle() : "[[제목 비공개]]")
                    .build();
        }
    }

    @Getter
    @Builder
    public static class DeleteProcStatus {
        private final boolean completed;
    }
}
