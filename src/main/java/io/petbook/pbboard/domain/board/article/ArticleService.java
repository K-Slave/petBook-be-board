package io.petbook.pbboard.domain.board.article;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    Map<String, Object>  getArticleBriefList(ArticleCommand.Paginate paginate);
    List<ArticleInfo.Main> getArticleInfoIsDeleted();
    ArticleInfo.Detail getArticleDetailInfo(String token);
    ArticleInfo.Brief createArticleInfo(ArticleCommand.Main command);
    ArticleInfo.Brief modifyArticleInfo(ArticleCommand.Modifier command);
    ArticleInfo.DeleteProcStatus deleteCategoryInfo(String token);
    ArticleInfo.Brief restoreArticleInfo(String token);
    ArticleInfo.Main enableArticleInfo(String token);
    ArticleInfo.Main disableArticleInfo(String token);

}
