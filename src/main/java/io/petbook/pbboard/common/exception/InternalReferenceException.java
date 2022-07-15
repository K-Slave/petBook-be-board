package io.petbook.pbboard.common.exception;

import io.petbook.pbboard.common.response.ErrorCode;

/**
 * [Kang] 코드 내부의 참조가 잘 못 되었을 경우 (Java Reflection 활용) 이 예외를 발생시킨다.
 */
public class InternalReferenceException extends BaseException {
    public InternalReferenceException() {
        super(ErrorCode.INTERNAL_REFERENCE_EXCEPTION);
    }

    public InternalReferenceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalReferenceException(String errorMsg) {
        super(errorMsg, ErrorCode.INTERNAL_REFERENCE_EXCEPTION);
    }

    public InternalReferenceException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
