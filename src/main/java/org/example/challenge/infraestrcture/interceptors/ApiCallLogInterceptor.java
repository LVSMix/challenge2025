package org.example.challenge.infraestrcture.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.challenge.application.service.ApiCallLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiCallLogInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiCallLogService logService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String error = ex != null ? ex.getMessage() : null;
        String responseBody = response.getStatus() == 200 ? "Success" : null;

        logService.log(
                request.getRequestURI(),
                request.getQueryString(),
                responseBody,
                error
        );
    }
}
