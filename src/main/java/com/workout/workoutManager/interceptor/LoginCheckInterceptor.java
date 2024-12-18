package com.workout.workoutManager.interceptor;

import com.workout.workoutManager.config.JwtConfig.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginCheckInterceptor.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Request URL: {}", request.getRequestURL());

        // 쿠키에서 액세스 토큰 확인
        String token = getAccessTokenFromCookie(request);

        // 토큰 존재 여부 및 유효성 검증
        if (token != null && jwtTokenProvider.validateToken(token)) {
            logger.info("Valid token found for IP: {}", request.getRemoteAddr());
            return true;
        }

        logger.debug("Token validation failed for IP: {}", request.getRemoteAddr());

        // 요청 타입에 따른 응답 처리
        if (isAjaxRequest(request)) {
            // API 요청인 경우 JSON 응답
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Please login first\"}");
        } else {
            // 일반 요청인 경우 로그인 페이지로 리디렉션
            response.sendRedirect("/login");
        }

        return false;
    }

    private String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        return (accept != null && accept.contains("application/json")) ||
                (xRequestedWith != null && xRequestedWith.equals("XMLHttpRequest"));
    }
}