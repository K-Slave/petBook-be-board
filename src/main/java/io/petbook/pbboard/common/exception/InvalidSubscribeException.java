package io.petbook.pbboard.common.exception;

import io.petbook.pbboard.common.response.ErrorCode;

public class InvalidSubscribeException extends BaseException {

    public InvalidSubscribeException() {
        super(ErrorCode.INVALID_SUBSCRIBE_EXCEPTION);
    }

    public InvalidSubscribeException(String message) {
        super(message, ErrorCode.INVALID_SUBSCRIBE_EXCEPTION);
    }
}
