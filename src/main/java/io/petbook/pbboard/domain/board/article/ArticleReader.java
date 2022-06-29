package io.petbook.pbboard.domain.board.article;

import java.util.List;
import java.util.Map;

/**
 * [Kang] Article 데이터에 대한 조회성 작업
 */
public interface ArticleReader {
    Article getEntity(String token);
    Article getEntityIsDeleted(String token);
    Map<String, Object> getList(ArticleCommand.Paginate paginate);
    List<Article> getListIsDeleted();
}
