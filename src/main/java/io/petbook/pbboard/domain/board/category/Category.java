package io.petbook.pbboard.domain.board.category;

import com.google.common.collect.Lists;
import io.petbook.pbboard.common.exception.InvalidParamException;
import io.petbook.pbboard.common.exception.InvalidTokenException;
import io.petbook.pbboard.common.util.TokenGenerator;
import io.petbook.pbboard.domain.AbstractEntity;
import io.petbook.pbboard.domain.board.article.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

/**
 * [Kang] Category JPA Domain Entity
 */

@Slf4j
@Getter
@Entity
@NoArgsConstructor
@Table(name = "_category")
public class Category extends AbstractEntity {
    private static final String ENTITY_PREFIX = "ctgy_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "title", length = 75, nullable = false)
    private String title;

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

    /**
     * [Kang] CascadeType 에 대해 고찰해볼 필요가 있다. 추후 자세히 알아보자.
     * https://data-make.tistory.com/668
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.PERSIST)
    private List<Article> posts = Lists.newArrayList();

    @Builder
    public Category (
        String title,
        boolean visible,
        String userToken
    ) {
        if (StringUtils.isEmpty(title)) {
            throw new InvalidParamException("파라미터 오류 - Category : title");
        }

        if (StringUtils.isEmpty(title)) {
            throw new InvalidParamException("파라미터 오류 - Category : userToken");
        }

        // [Kang] Category 객체 대체키 (Auto Increment ID 대체용)
        this.token = TokenGenerator.randomCharacterWithPrefix(ENTITY_PREFIX);

        // [Kang] Main Fields
        this.title = title;

        if (visible) {
            this.enable();
        } else {
            this.disable();
        }

        // [Kang] FK Setting (User)
        this.userToken = userToken;

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
        return this.visibleStatus == VisibleStatus.ENABLED;
    }

    public void modifyByCommand(CategoryCommand.Modifier command) {
        // [Kang] TODO: Modifier 에서 접속 회원의 토큰을 가져와 저장된 회원 토큰과 연관되는지 체크해야 한다.
        // [Kang] 회원 접속 정보는 토큰의 일치 여부로 간단하게 하지만, 암호화 데이터인 경우에는 SHA, MD 등에 따른 알고리즘으로 체크하는 경우도 있어야 할 것이다.

        if (!StringUtils.equals(this.token, command.getToken())) {
            throw new InvalidTokenException("토큰이 일치하지 않아 수정 작업이 불가능 합니다.");
        }

        this.title = command.getTitle();

        if (command.getVisible()) {
            this.enable();
        } else {
            this.disable();
        }
    }
}
