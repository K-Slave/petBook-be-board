package io.petbook.pbboard.domain;

import io.petbook.pbboard.common.exception.IllegalStatusException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity {
    // CreationTimestamp 만 Zone 데이터 허용.
    // CreatedDate, LastModifiedDate 는 반영되지 않는다 하더라.

    // LocalDateTime 와 다른 건 시간 대가 해외 설정을 제공한다.
    // 데이터들의 변동에 대한 최소한의 정보를 담기 위해 createdAt, modifiedAt 를 사용하는 것이 좋다.
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Column(name = "crud_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CrudStatus crudStatus;

    @Getter
    @RequiredArgsConstructor
    public enum CrudStatus {
        CREATED("생성"), MODIFIED("수정"), DELETED("삭제"), RESTORED("복구");
        private final String description;
    }

    public void created() {
        if (this.crudStatus == null) {
            this.crudStatus = CrudStatus.CREATED;
        }

        if (this.crudStatus != CrudStatus.CREATED) {
            throw new IllegalStatusException("상태 전달 오류 - MODIFIED, DELETED, RESTORED -> CREATED 상태로 전환이 불가능 합니다.");
        }
    }

    public void modified() {
        if (this.crudStatus == CrudStatus.DELETED) {
            throw new IllegalStatusException("상태 전달 오류 - DELETED -> MODIFIED 상태로 전환이 불가능 합니다.");
        }

        this.crudStatus = CrudStatus.MODIFIED;
    }

    public void deleted() {
        this.crudStatus = CrudStatus.DELETED;
    }

    public void restored() {
        if (this.crudStatus != CrudStatus.DELETED) {
            throw new IllegalStatusException("상태 전달 오류 - CREATED, MODIFIED, RESTORED -> RESTORED 상태로 전환이 불가능 합니다.");
        }

        this.crudStatus = CrudStatus.RESTORED;
    }
}
