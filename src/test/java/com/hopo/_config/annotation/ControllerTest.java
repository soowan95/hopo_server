package com.hopo._config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.hopo._config.SecurityConfig;
import com.hopo._config.jwt.JwtAuthenticationEntryPoint;
import com.hopo._filter.JwtFilter;
import com.hopo._config.jwt.TokenProvider;
import com.hopo._global.exception.GlobalExceptionHandler;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({TokenProvider.class, JwtFilter.class, SecurityConfig.class, JwtAuthenticationEntryPoint.class, GlobalExceptionHandler.class})
public @interface ControllerTest {
}
