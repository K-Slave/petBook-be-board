package io.petbook.pbboard.common.exception;

import io.petbook.pbboard.common.response.ErrorCode;

public class InvalidTokenException extends BaseException {

    public InvalidTokenException() {
        super(ErrorCode.COMMON_INVALID_TOKEN);
    }

    public InvalidTokenException(String message) {
        super(message, ErrorCode.COMMON_INVALID_TOKEN);
    }
}
