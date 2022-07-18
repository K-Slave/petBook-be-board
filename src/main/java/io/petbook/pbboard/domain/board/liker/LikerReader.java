package io.petbook.pbboard.domain.board.liker;

public interface LikerReader {
    long countByArticleToken(String articleToken);
    boolean existByLikeToken(String likeToken);
}
