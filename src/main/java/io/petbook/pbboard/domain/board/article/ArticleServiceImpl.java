package io.petbook.pbboard.domain.board.article;

import io.petbook.pbboard.domain.board.category.Category;
import io.petbook.pbboard.domain.board.category.CategoryReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleReader articleReader;
    private final ArticleStore articleStore;

    private final CategoryReader categoryReader;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getArticleBriefList(ArticleCommand.Paginate paginate) {
        Map<String, Object> pgnMap = articleReader.getList(paginate);
        List<Article> articles = (List<Article>) pgnMap.get("data");
        List<ArticleInfo.Brief> articleBriefs = articles.stream().map(ArticleInfo.Brief::toInfo).collect(Collectors.toList());
        pgnMap.put("briefs", articleBriefs);
        return pgnMap;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleInfo.Main> getArticleInfoIsDeleted() {
        return articleReader
                .getListIsDeleted()
                .stream()
                .map(ArticleInfo.Brief::toInfo)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public ArticleInfo.Detail getArticleDetailInfo(String token) {
        Article article = articleReader.getEntity(token);
        return ArticleInfo.Detail.toInfo(article);
    }

    @Override
    @Transactional
    public ArticleInfo.Brief createArticleInfo(ArticleCommand.Main command) {
        // [Kang] TODO: Category 정보 주입은 Facade 로 옮기는게 낫지 않겠니?
        Category category = categoryReader.getEntity(command.getCategoryToken());
        Article newArticle = command.toEntity(category);
        Article article = articleStore.store(newArticle);
        return ArticleInfo.Brief.toInfo(article);
    }

    @Override
    @Transactional
    public ArticleInfo.Brief modifyArticleInfo(ArticleCommand.Modifier command) {
        Article article = articleReader.getEntity(command.getToken());
        article.modifyByCommand(command);
        article.modified();
        return ArticleInfo.Brief.toInfo(article);
    }

    @Override
    @Transactional
    public ArticleInfo.DeleteProcStatus deleteCategoryInfo(String token) {
        Article article = articleReader.getEntity(token);
        article.deleted();
        return ArticleInfo.DeleteProcStatus.builder().completed(true).build();
    }

    @Override
    @Transactional
    public ArticleInfo.Brief restoreArticleInfo(String token) {
        Article article = articleReader.getEntityIsDeleted(token);
        article.restored();
        return ArticleInfo.Brief.toInfo(article);
    }

    @Override
    @Transactional
    public ArticleInfo.Main enableArticleInfo(String token) {
        Article article = articleReader.getEntity(token);
        article.enable();
        return ArticleInfo.Brief.toInfo(article);
    }

    @Override
    @Transactional
    public ArticleInfo.Main disableArticleInfo(String token) {
        Article article = articleReader.getEntity(token);
        article.disable();
        return ArticleInfo.Brief.toInfo(article);
    }
}
