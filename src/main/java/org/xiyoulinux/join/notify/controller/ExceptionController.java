package org.xiyoulinux.join.notify.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.xiyoulinux.join.notify.model.dto.result.RespCode;
import org.xiyoulinux.join.notify.model.dto.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/14 下午9:39
 */
@ControllerAdvice
@RestController
@Log4j2
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> argExceptionHandler(IllegalArgumentException ex) {
        return Result.<Void>builder().failure(RespCode.ARG_CODE, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> otherExceptionHandler(Exception ex, HttpServletRequest request) {
        log.error("request failed, request url [{}], method [{}], exception [{}], [{}]",
                request.getRequestURI(),
                request.getMethod(),
                ex.getClass().getSimpleName(),
                Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse("")
        );
        log.error(ex);
        return Result.builder(ex.getClass().getSimpleName()).fromResp(RespCode.FAILURE);
    }

}
