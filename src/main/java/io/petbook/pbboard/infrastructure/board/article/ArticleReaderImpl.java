package io.petbook.pbboard.infrastructure.board.article;

import io.petbook.pbboard.common.exception.EntityNotFoundException;
import io.petbook.pbboard.domain.board.article.Article;
import io.petbook.pbboard.domain.board.article.ArticleCommand;
import io.petbook.pbboard.domain.board.article.ArticleReader;
import io.petbook.pbboard.domain.board.category.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleReaderImpl implements ArticleReader {
    private final ArticleRepository articleRepository;

    @Override
    public Article getEntity(String token) {
        return articleRepository
                .findByToken(token)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Article getEntityIsDeleted(String token) {
        return articleRepository
                .findByTokenIsDeleted(token)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Map<String, Object> getList(ArticleCommand.Paginate paginate) {
        return articleRepository.findByPaginate(paginate);
    }

    @Override
    public List<Article> getListIsDeleted() {
        List<Article> articles = new ArrayList<>();
        articleRepository.findAllIsDeleted().forEach(articles::add);
        return articles;
    }
}
