package com.yss.logrecord.core.application.exceptions;

public class ApplicationInfrastructureException extends ApplicationException {
    public ApplicationInfrastructureException(String message, Exception ex) {
        super(message, ex);
    }
}