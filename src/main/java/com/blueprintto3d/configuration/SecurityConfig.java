package com.blueprintto3d.configuration;

import com.blueprintto3d.jwt.TokenProvider;
import com.blueprintto3d.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    @Value("${jwt.token.secret}")
    private String secretKey;

    private final String[] SWAGGER = {
            "/swagger-ui/**"
    };
    private final String[] POST_PERMIT = {
            "/api/users/join",
            "/api/users/login",
            "/api/users/logout",
            "/users/login",
            "/users/logout",
            "/users/join",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers(POST_PERMIT).permitAll()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .apply(new JwtSecurityConfig(tokenProvider))
                .and()
                .build();


    }
}
