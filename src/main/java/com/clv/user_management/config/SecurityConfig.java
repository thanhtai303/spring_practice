package com.clv.user_management.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.NonFinal;

@Configuration

public class SecurityConfig {

        @Autowired
        private CustomUserDetailsService user;

        @NonFinal
        @Value("${jwt.secret}")
        protected String SECRET_KEY;

        protected static final String[] PUBLIC_PATHS = {
                        "/api/v1/auth/login",
                        "/api/v1/auth/register",
        };
        protected static final String[] PUBLIC_GET_PATHS = {};

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authorizeHttpRequests(
                                                requests -> requests
                                                                .requestMatchers(HttpMethod.POST, PUBLIC_PATHS)
                                                                .permitAll()
                                                                .requestMatchers(HttpMethod.GET, PUBLIC_GET_PATHS)
                                                                .permitAll()
                                                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**",
                                                                                "/swagger-ui.html")
                                                                .permitAll()
                                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))

                                                .authenticationEntryPoint((req, resp, ex) -> resp
                                                                .sendError(HttpServletResponse.SC_UNAUTHORIZED)))

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                httpSecurity.csrf(AbstractHttpConfigurer::disable);
                return httpSecurity.build();
        }

        @Bean
        public JwtDecoder jwtDecoder() {
                SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8),
                                "HmacSHA256");
                return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                                .macAlgorithm(MacAlgorithm.HS256)
                                .build();
        }

        @Bean
        public JwtEncoder jwtEncoder() {
                SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8),
                                "HmacSHA256");
                return new NimbusJwtEncoder(
                                new ImmutableSecret<>(secretKeySpec));
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000",
                                "http://localhost:5500"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
                                "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(user);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
