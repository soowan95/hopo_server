package com.hopo._config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import com.hopo._config.SecurityConfig;
import com.hopo._config.jwt.JwtAuthenticationEntryPoint;
import com.hopo._config.jwt.JwtFilter;
import com.hopo._config.jwt.TokenProvider;
import com.hopo.member.controller.MemberController;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({TokenProvider.class, JwtFilter.class, SecurityConfig.class, JwtAuthenticationEntryPoint.class})
public @interface ControllerTest {
}
