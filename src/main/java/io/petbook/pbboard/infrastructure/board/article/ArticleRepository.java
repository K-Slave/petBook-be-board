package io.petbook.pbboard.infrastructure.board.article;

import com.google.common.collect.Maps;
import io.petbook.pbboard.common.exception.InvalidParamException;
import io.petbook.pbboard.domain.board.article.Article;
import io.petbook.pbboard.domain.board.article.ArticleCommand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    // [Kang] JPA 에서 메소드 네이밍이 길어지면, JPQL 를 사용하는 걸 권장한다.
    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.token = :token")
    Optional<Article> findByToken(@Param("token") String token);

    @Query("select a from Article a where a.crudStatus = \'DELETED\' and a.token = :token")
    Optional<Article> findByTokenIsDeleted(@Param("token") String token);

    @Query("select a from Article a where a.crudStatus = \'DELETED\'")
    Iterable<Article> findAllIsDeleted();

    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.visibleStatus = :visibleStatus")
    Page<Article> findAll(@Param("visibleStatus") Article.VisibleStatus visibleStatus, Pageable pageable);

    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.visibleStatus = :visibleStatus and a.category.token = :categoryToken")
    Page<Article> findAllWithCategory(@Param("visibleStatus") Article.VisibleStatus visibleStatus, @Param("categoryToken") String categoryToken, Pageable pageable);

    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.visibleStatus = :visibleStatus and (a.title like %:text% or a.context like %:text%) ")
    Page<Article> findAllTextContains(@Param("text") String text, @Param("visibleStatus") Article.VisibleStatus visibleStatus, Pageable pageable);

    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.visibleStatus = :visibleStatus and a.title like %:text%")
    Page<Article> findTitleTextContains(@Param("text") String text, @Param("visibleStatus") Article.VisibleStatus visibleStatus, Pageable pageable);

    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.visibleStatus = :visibleStatus and a.context like %:text%")
    Page<Article> findContextTextContains(@Param("text") String text, @Param("visibleStatus") Article.VisibleStatus visibleStatus, Pageable pageable);

    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.visibleStatus = :visibleStatus and a.category.token = :categoryToken and (a.title like %:text% or a.context like %:text%)")
    Page<Article> findAllTextContainsWithCategory(@Param("text") String text, @Param("visibleStatus") Article.VisibleStatus visibleStatus, @Param("categoryToken") String categoryToken, Pageable pageable);

    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.visibleStatus = :visibleStatus and a.category.token = :categoryToken and a.title like %:text%")
    Page<Article> findTitleTextContainsWithCategory(@Param("text") String text, @Param("visibleStatus") Article.VisibleStatus visibleStatus, @Param("categoryToken") String categoryToken, Pageable pageable);

    @Query("select a from Article a where a.crudStatus <> \'DELETED\' and a.visibleStatus = :visibleStatus and a.category.token = :categoryToken and a.context like %:text%")
    Page<Article> findContextTextContainsWithCategory(@Param("text") String text, @Param("visibleStatus") Article.VisibleStatus visibleStatus, @Param("categoryToken") String categoryToken, Pageable pageable);

    // [Kang] 페이징네이션 객체 기반 쿼리 처리 문단
    default Map<String, Object> findByPaginate(ArticleCommand.Paginate paginate) {
        if (paginate.getSz() == 0L) {
            throw new InvalidParamException("파라미터 오류 : 페이징네이션 페이지 수는 0 보다 큰 양수여야 합니다.");
        }

        Page<Article> articles = null;
        PageRequest pageRequest = PageRequest.of(paginate.getPg() - 1, paginate.getSz());
        Article.VisibleStatus visibleStatus = paginate.checkVisible() ? Article.VisibleStatus.ENABLED : Article.VisibleStatus.DISABLED;

        // [Kang] Wrapper 클래스에 쿼리 스트링 ob 값을 설정 안 하면 값이 null 로 뜨는 거 같다. (아무리 @Builder.Default 를 하더라도...)
        if (paginate.getOb() != null) {
            // [Kang] Order By 처리
            switch (ArticleCommand.Paginate.loadObByIntCode(paginate.getOb())) {
                case CREATED_AT_ASC:
                    pageRequest = pageRequest.withSort(Sort.by("createdAt"));
                    break;
                case CREATED_AT_DESC:
                    pageRequest = pageRequest.withSort(Sort.by(Sort.Direction.DESC, "createdAt"));
                    break;

                // [Kang] 좋아요, 조회수, 정확도는 TODO.
            }
        } else {
            // [Kang] 아무런 값이 없는 경우에는 생성 시간 DESC 로 설정한다.
            pageRequest = pageRequest.withSort(Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        boolean hasCtgTk = !StringUtils.isEmpty(paginate.getCtgTk());

        // [Kang] Search By 처리
        if (paginate.getSb() != null) {
            switch (ArticleCommand.Paginate.loadSbByIntCode(paginate.getSb())) {
                case ALL_CONTAINS:
                    articles =
                            hasCtgTk ?
                                    findAllTextContainsWithCategory(paginate.getSt(), visibleStatus, paginate.getCtgTk(), pageRequest) :
                                    findAllTextContains(paginate.getSt(), visibleStatus, pageRequest);
                case TITLE_CONTAINS:
                    articles =
                            hasCtgTk ?
                                    findTitleTextContainsWithCategory(paginate.getSt(), visibleStatus, paginate.getCtgTk(), pageRequest) :
                                    findTitleTextContains(paginate.getSt(), visibleStatus, pageRequest);
                    break;
                case CONTEXT_CONTAINS:
                    articles =
                            hasCtgTk ?
                                    findContextTextContainsWithCategory(paginate.getSt(), visibleStatus, paginate.getCtgTk(), pageRequest) :
                                    findContextTextContains(paginate.getSt(), visibleStatus, pageRequest);
                    break;
            }
        } else {
            articles =
                    hasCtgTk ?
                            findAll(visibleStatus, pageRequest) :
                            findAllWithCategory(visibleStatus, paginate.getCtgTk(), pageRequest);
        }

        // [Kang] 결과물 반환
        Map<String, Object> result = Maps.newHashMap();
        result.put("data", articles.getContent());
        result.put("total", articles.getTotalElements());
        result.put("current", (long) paginate.getPg());
        result.put("pages", (long) articles.getTotalPages());

        return result;
    }
}
