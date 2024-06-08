package edu.bethlehem.user_service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserService userDetailsService;
    public static final String ADMIN = "admin";
    public static final String USER = "user";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (authz) -> authz.requestMatchers("/api/some-path-here/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET)
                        .permitAll()
                        .requestMatchers(HttpMethod.PUT)
                        .hasAuthority(USER)
                        .requestMatchers(HttpMethod.POST)
                        .permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(loginConfig -> {
                    // loginConfig.successForwardUrl("/dashboard");
                    loginConfig.usernameParameter("email");
                    loginConfig.passwordParameter("password");
                })
                .authenticationProvider(authenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(httpSession -> httpSession.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(c -> c.disable());
        return http.build();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }



}