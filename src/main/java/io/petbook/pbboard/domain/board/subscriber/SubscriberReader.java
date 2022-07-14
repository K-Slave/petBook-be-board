package io.petbook.pbboard.domain.board.subscriber;

public interface SubscriberReader {
    long countByArticleToken(String articleToken);
}
