package io.petbook.pbboard.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // 장애 상황
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다."),
    COMMON_INVALID_TOKEN("일치하지 않는 데이터 토큰입니다."),

    INVALID_SUBSCRIBE_EXCEPTION("유효하지 않는 조회 시도입니다."),

    INTERNAL_REFERENCE_EXCEPTION("비즈니스 로직의 참조가 잘못 되었습니다.");

    // TODO: 요청 및 응답에 대해 필요한 임의 오류 코드 생성 필요.


    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
