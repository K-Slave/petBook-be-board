package io.petbook.pbboard.infrastructure.board.liker;

import io.petbook.pbboard.domain.board.liker.Liker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikerRepository extends CrudRepository<Liker, String> {
}
