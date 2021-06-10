package com.yss.logrecord.core.application.exceptions;

public abstract class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Exception ex) {
        super(message, ex);
    }
}