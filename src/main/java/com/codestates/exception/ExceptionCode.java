package com.codestates.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404,"MEMBER NOT FOUND"),
    MEMBER_EXISTS(404,"MEMBER EXISTS"),
    CANNOT_REGISTRATION_QUESTION(404, "MANAGER CANNOT REGISTRATION QUESTION");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
