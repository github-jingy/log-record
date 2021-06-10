package com.yss.logrecord.config;

import com.yss.common.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * <p>
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 * </p>
 *
 * @author yss
 * @since 2019-04-17
 */
@Slf4j
@ControllerAdvice
@Order(600)
public class GlobalExceptionHandler {

    /**
     * Handler exception response result
     *
     * @param req req
     * @param ex  ex
     * @return the response result
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult<Object> handlerException(HttpServletRequest req, Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(toErrorLog(req.getRequestURI(), httpStatus.value(), ex.getMessage()));
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) ex;
            String errorMsg = violationException.getConstraintViolations().iterator().next().getMessage();
            return ResponseResult.error(500,errorMsg);
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) ex;
            String errorMsg = validException.getBindingResult().getFieldErrors().iterator().next().getDefaultMessage();
            return ResponseResult.error(500,errorMsg);
        }
        return ResponseResult.error(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    /**
     * To error log string
     *
     * @param url     url
     * @param status  status
     * @param message message
     * @return the string
     */
    private String toErrorLog(String url, int status, String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n##################### [ ERROR START ] ####################\n");
        stringBuilder.append("URL: ").append(url).append("\n");
        stringBuilder.append("STATUS CODE: ").append(status).append("\n");
        stringBuilder.append("ERROR MESSAGE: ").append(message).append("\n");
        stringBuilder.append("##################### [ ERROR END ]   ####################\n");
        return stringBuilder.toString();
    }

}
