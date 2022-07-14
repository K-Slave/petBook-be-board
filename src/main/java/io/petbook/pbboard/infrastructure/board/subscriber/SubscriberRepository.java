package io.petbook.pbboard.infrastructure.board.subscriber;

import io.petbook.pbboard.domain.board.subscriber.Subscriber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, String> {
    // [Kang] 게시물 토큰으로 조회자 정보들을 가져온다. (향후 그래프 작업에 활용 가능 예상)
    Iterable<Subscriber> findByArticleToken(String articleToken);

    // [Kang] 게시물 토큰으로 조회수를 가져온다.
    long countByArticleToken(String articleToken);
}
