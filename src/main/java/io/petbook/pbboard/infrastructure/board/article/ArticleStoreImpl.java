package io.petbook.pbboard.infrastructure.board.article;

import io.petbook.pbboard.domain.board.article.Article;
import io.petbook.pbboard.domain.board.article.ArticleStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleStoreImpl implements ArticleStore {
    private final ArticleRepository articleRepository;

    @Override
    public Article store(Article article) {
        return articleRepository.save(article);
    }
}
