package com.workout.workoutManager.config;

import com.workout.workoutManager.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/mypage/**")  // 마이페이지 관련 모든 경로
                .addPathPatterns("/api/workout/**")  // 운동 관련 API
                .excludePathPatterns(
                        "/api/auth/**",     // 인증 관련 API 제외
                        "/api/register",    // 회원가입 제외
                        "/login",           // 로그인 페이지 제외
                        "/swagger-ui/**",   // Swagger UI 제외
                        "/v3/api-docs/**"   // API 문서 제외
                );
    }
}