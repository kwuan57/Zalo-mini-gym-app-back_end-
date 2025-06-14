package com.example.gym.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.gym.enums.RolePlay;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] publicEntrypoint = {
            "/auth/login",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh"
    };

    @Autowired
    @Lazy
    CustomJwtDecoder jwtDecoder;

    private static final String userEntrypoint = "/api/v1/users";
    private static final String roleEntrypoint = "/api/v1/roles";
    private static final String exerciseEntrypoint = "/api/v1/exercises";
    private static final String heathEntrypoint = "/api/v1/health-data";
    private static final String imageEntrypoint = "/api/v1/images";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, publicEntrypoint)
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, userEntrypoint)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, userEntrypoint + "/*")
                        .hasAnyRole(RolePlay.USER.name(), RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, roleEntrypoint + "/user/uploads/*")
                        .hasAnyRole(RolePlay.USER.name(), RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, userEntrypoint + "/*")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, roleEntrypoint + "/user/*")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, exerciseEntrypoint + "/**")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, heathEntrypoint + "/**")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())

                        .requestMatchers(HttpMethod.GET, imageEntrypoint + "/*")
                        .permitAll()
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthenticationConverter;
    }

    // @Bean
    // public CorsFilter corsFilter() {
    // CorsConfiguration corsConfiguration = new CorsConfiguration();
    // corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Cấu hình
    // frontend domain
    // corsConfiguration.addAllowedMethod("*"); // Cho phép tất cả các phương thức
    // HTTP
    // corsConfiguration.addAllowedHeader("*"); // Cho phép tất cả các headers
    // corsConfiguration.setAllowCredentials(true); // Chép tật bộ người dùng
    // UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new
    // UrlBasedCorsConfigurationSource();
    // urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",
    // corsConfiguration);
    // return new CorsFilter(urlBasedCorsConfigurationSource);
    // }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://5dc0-1-54-5-164.ngrok-free.app" // Thay bằng Ngrok URL của frontend
        ));
        configuration.addAllowedMethod("*"); // Cho phép tất cả phương thức HTTP
        configuration.addAllowedHeader("*"); // Cho phép tất cả headers
        configuration.setAllowCredentials(true); // Cho phép gửi cookies & token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Lazy
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
