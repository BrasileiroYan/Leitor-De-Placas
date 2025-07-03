package br.com.prf.leitordeplacas.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomSecurityExceptionHandler customSecurityExceptionHandler;

    public SecurityConfig(
            AuthenticationFilter jwtAuthFilter,
            AuthenticationProvider authenticationProvider,
            CustomSecurityExceptionHandler customSecurityExceptionHandler
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.customSecurityExceptionHandler = customSecurityExceptionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customSecurityExceptionHandler)
                        .accessDeniedHandler(customSecurityExceptionHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/h2-console/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/admin/**", "/crimes/**", "/addresses/**", "/criminals/**", "/criminal-records/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/vehicles/**", "/scans/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STANDARD")
                        .requestMatchers("/app/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STANDARD")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
