package io.petbook.pbboard.infrastructure.board.category;

import io.petbook.pbboard.domain.board.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    // [Kang] JPA 에서 메소드 네이밍이 길어지면, JPQL 를 사용하는 걸 권장한다.
    @Query("select c from Category c where c.crudStatus <> \'DELETED\'")
    Iterable<Category> findAll();

    @Query("select c from Category c where c.crudStatus = \'DELETED\'")
    Iterable<Category> findAllIsDeleted();

    @Query("select c from Category c where c.crudStatus <> \'DELETED\' and c.token = :token")
    Optional<Category> findByToken(@Param("token") String token);

    @Query("select c from Category c where c.crudStatus = \'DELETED\' and c.token = :token")
    Optional<Category> findByTokenIsDeleted(@Param("token") String token);
}
