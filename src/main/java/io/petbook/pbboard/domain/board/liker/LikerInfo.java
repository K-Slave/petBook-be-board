package io.petbook.pbboard.domain.board.liker;

import io.petbook.pbboard.domain.board.subscriber.Subscriber;
import io.petbook.pbboard.domain.board.subscriber.SubscriberInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

/**
 * [Kang] Liker Reading Model
 */
public class LikerInfo {
    @Getter
    @Builder
    public static class Main {
        private final String userToken;
        private final String articleToken;
        private final String viewCreatedAt;

        public static LikerInfo.Main toInfo(Liker liker) {
            return LikerInfo.Main.builder()
                    .userToken(liker.getUserToken())
                    .articleToken(liker.getArticleToken())
                    .viewCreatedAt(liker.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
        }
    }
}
