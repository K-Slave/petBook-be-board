package io.petbook.pbboard.domain.board.article;

/**
 * [Kang] Article 데이터에 대한 첨삭성 작업
 */
public interface ArticleStore {
    Article store(Article article);
}
