package com.jbaacount.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig
{
    private static final String ENDPOINT_WHITELIST[] = {
            "/login",
            "/error"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .headers((headers) ->
                        headers
                                .frameOptions((frameOptions) -> frameOptions.disable()))
                .csrf(csrf -> csrf
                        .disable())
                .cors(cors -> cors
                        .disable())
                .httpBasic(httpBasic -> httpBasic
                        .disable())
                .formLogin(formLogin -> formLogin
                        .disable())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
