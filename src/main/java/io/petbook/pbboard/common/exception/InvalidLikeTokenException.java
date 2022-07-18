package io.petbook.pbboard.common.exception;

import io.petbook.pbboard.common.response.ErrorCode;

public class InvalidLikeTokenException extends BaseException {

    public InvalidLikeTokenException() {
        super(ErrorCode.INVALID_LIKE_TOKEN);
    }

    public InvalidLikeTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidLikeTokenException(String errorMsg) {
        super(errorMsg, ErrorCode.INVALID_LIKE_TOKEN);
    }

    public InvalidLikeTokenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
