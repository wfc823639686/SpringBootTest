package com.wfc.web.common

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        def r = [
                "msg": e.getMessage(),
                 "code": 0,
                 "url": req.getRequestURI(),
                "query": req.getQueryString()
        ];
        return r;
    }
}