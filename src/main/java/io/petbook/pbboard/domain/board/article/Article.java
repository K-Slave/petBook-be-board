package io.petbook.pbboard.domain.board.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.petbook.pbboard.common.exception.InvalidParamException;
import io.petbook.pbboard.common.exception.InvalidTokenException;
import io.petbook.pbboard.common.util.TokenGenerator;
import io.petbook.pbboard.domain.AbstractEntity;
import io.petbook.pbboard.domain.board.category.Category;
import io.petbook.pbboard.domain.board.keyword.Keyword;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Set;


/**
 * [Kang] Article JPA Domain Entity
 */

@Slf4j
@Getter
@Entity
@NoArgsConstructor
@Table(name = "_post")
public class Article extends AbstractEntity {
    private static final String ENTITY_PREFIX = "atcl_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "title", length = 75, nullable = false)
    private String title;

    @Column(name = "context", length = 2048, nullable = false)
    private String context;

    @Column(name = "user_token", nullable = false)
    private String userToken;

    @Column(name = "visible_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private VisibleStatus visibleStatus;

    @Getter
    @RequiredArgsConstructor
    public enum VisibleStatus {
        ENABLED("공개"), DISABLED("비공개");
        private final String description;
    }

    // [Kang] 자동으로 두면 fetch Type 은 Eager 로 된다.
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // [Kang] Keyword 데이터 추가
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "articles", cascade = CascadeType.PERSIST)
    private Set<Keyword> keywords;

    // [Kang] TODO: 권한 별 공개 여부, 게시글 조회수 등등

    @Builder
    public Article (
        String title,
        String context,
        boolean visible,
        String userToken,
        Category category
    ) {
        if (StringUtils.isEmpty(title)) {
            throw new InvalidParamException("파라미터 오류 - Article : title");
        }

        if (StringUtils.isEmpty(context)) {
            throw new InvalidParamException("파라미터 오류 - Article : context");
        }

        if (StringUtils.isEmpty(userToken)) {
            throw new InvalidParamException("파라미터 오류 - Article : userToken");
        }

        if (category == null) {
            throw new InvalidParamException("파라미터 오류 - Article : category");
        }

        // [Kang] Article 객체 대체키 (Auto Increment ID 대체용)
        this.token = TokenGenerator.randomCharacterWithPrefix(ENTITY_PREFIX);

        // [Kang] Main Fields
        this.title = title;
        this.context = context;

        if (visible) {
            this.enable();
        } else {
            this.disable();
        }

        // [Kang] FK Setting (User, Category)
        this.userToken = userToken;
        this.category = category;

        // [Kang] Abstract Entity Created 설정
        super.created();
    }

    public void enable() {
        this.visibleStatus = VisibleStatus.ENABLED;
    }

    public void disable() {
        this.visibleStatus = VisibleStatus.DISABLED;
    }

    public boolean isVisible() {
        return this.visibleStatus == Article.VisibleStatus.ENABLED;
    }

    public void modifyByCommand(ArticleCommand.Modifier command) {
        if (!StringUtils.equals(this.token, command.getToken())) {
            throw new InvalidTokenException("토큰이 일치하지 않아 수정 작업이 불가능 합니다.");
        }

        this.title = command.getTitle();
        this.context = command.getContext();

        if (command.getVisible()) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public String contextBriefing() {
        if (this.context == null) {
            return "";
        } else {
            return this.context.substring(0, Math.min(this.context.length(), 32)) + "...";
        }
    }
}
