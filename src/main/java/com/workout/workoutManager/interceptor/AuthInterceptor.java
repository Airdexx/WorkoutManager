package com.workout.workoutManager.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 쿠키에서 access token 확인
        String accessToken = getAccessTokenFromCookie(request);
        
        if (accessToken == null) {
            // 로그인 페이지로 리디렉션
            response.sendRedirect("/login");
            return false;
        }
        
        // 토큰 유효성 검증 로직 추가
        
        return true;
    }
    
    private String getAccessTokenFromCookie(HttpServletRequest request) {
        // 쿠키에서 토큰 추출 로직
        return null;
    }
}