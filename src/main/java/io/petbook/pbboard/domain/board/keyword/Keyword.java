package io.petbook.pbboard.domain.board.keyword;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.petbook.pbboard.domain.board.article.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Set;

/**
 * [Kang] Keyword JPA Domain Entity
 */

@Slf4j
@Getter
@Entity
@NoArgsConstructor
@Table(name = "_keyword")
public class Keyword {
    private static final String ENTITY_PREFIX = "kywd_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "title", length = 15, nullable = false)
    private String title;

    /**
     * [Kang] ManyToMany 관계 설정할 때 List 로 하면 삭제 뒤 Add 된다는 소문이 있다.
     * JPA Relation 설정 시, 자료구조 설정에도 영향이 어느 정도는 있다. 개발 시 유의할 것.
     * https://hyunsoori.tistory.com/13 게시물을 참조하라.
     */
    // [Kang] JoinTable 어노테이션을 따로 안 붙어줘도, _keyword_articles 테이블은 생기는 거 같다.
    // [Kang] Keyword, Article 간의 관계는 Keyword - KWRD_ATCL - Article 이렇게 연동이 될 수 밖에 없다. (RDB 사용 기준)
    @JsonIgnore
    @JoinTable(
        name = "_keyword_article",
        joinColumns = @JoinColumn(name = "keyword_id"),
        inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Article> articles;
}
