package com.yss.logrecord.core.gateways.ohs;


import com.yss.common.result.ResponseResult;
import com.yss.logrecord.core.application.exceptions.ApplicationDomainException;
import com.yss.logrecord.core.application.exceptions.ApplicationInfrastructureException;
import com.yss.logrecord.core.application.exceptions.ApplicationValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Slf4j
public class Resources {

    private String requestType;
    private HttpStatus successfulStatus;
    private HttpStatus errorStatus;
    private HttpStatus failedStatus;
    private Resources(String requestType) {
        this.requestType = requestType;
    }

    public static Resources with(String requestType) {
        return new Resources(requestType);
    }

    public Resources onSuccess(HttpStatus status) {
        this.successfulStatus = status;
        return this;
    }

    public Resources onError(HttpStatus status) {
        this.errorStatus = status;
        return this;
    }

    public Resources onFailed(HttpStatus status) {
        this.failedStatus = status;
        return this;
    }

    public <T> ResponseResult<T> execute(Supplier<T> supplier) {
        try {
            T entity = supplier.get();
            return ResponseResult.success(entity, successfulStatus.value(), successfulStatus.getReasonPhrase());
        } catch (ApplicationValidationException ex) {
            log.error(String.format("The request of %s is invalid", requestType), ex);
            return ResponseResult.error(errorStatus.value(), getMessage(ex));
        } catch (ApplicationDomainException ex) {
            log.error(String.format("Exception raised %s REST Call", requestType), ex);
            return ResponseResult.error(failedStatus.value(), getMessage(ex));
        } catch (ApplicationInfrastructureException ex) {
            log.error(String.format("Fatal exception raised %s REST Call", requestType), ex);
            return ResponseResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), getMessage(ex));
        }
    }

    public ResponseResult<Object> execute(Runnable runnable) {
        try {
            runnable.run();
            return ResponseResult.success();
        } catch (ApplicationValidationException ex) {
            log.error(String.format("The request of %s is invalid", requestType), ex);
            return ResponseResult.error(errorStatus.value(), getMessage(ex));
        } catch (ApplicationDomainException ex) {
            log.error(String.format("Exception raised %s REST Call", requestType), ex);
            return ResponseResult.error(failedStatus.value(), getMessage(ex));
        } catch (ApplicationInfrastructureException ex) {
            log.error(String.format("Fatal exception raised %s REST Call", requestType), ex);
            return ResponseResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), getMessage(ex));
        }
    }

    private String getMessage(RuntimeException ex) {
        if (ex != null) {
            String message = ex.getMessage();
            String[] split = message.split(":");
            if (split.length == 0) {
                return null;
            }
            return split[split.length - 1];
        }
        return null;
    }
}