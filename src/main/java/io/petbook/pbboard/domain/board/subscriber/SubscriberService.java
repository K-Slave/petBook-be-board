package io.petbook.pbboard.domain.board.subscriber;

import io.petbook.pbboard.domain.board.article.Article;

public interface SubscriberService {
    SubscriberInfo.Main createForAnonymous(SubscriberCommand.Anonymous anonymous);
    SubscriberInfo.Main createForAccessor(SubscriberCommand.Accessor accessor);
    long countByArticleToken(String articleToken);
}
