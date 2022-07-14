package io.petbook.pbboard.domain.board.subscriber;

import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

/**
 * [Kang] Subscriber Reading Model
 */
public class SubscriberInfo {
    @Getter
    @Builder
    public static class Main {
        private final String userToken;
        private final String articleToken;
        private final String viewCreatedAt;

        public static Main toInfo(Subscriber subscriber) {
            return Main.builder()
                    .userToken(subscriber.getUserToken())
                    .articleToken(subscriber.getArticleToken())
                    .viewCreatedAt(subscriber.getViewCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
        }
    }
}
