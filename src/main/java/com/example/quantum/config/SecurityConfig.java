package com.example.quantum.config;

import com.example.quantum.infra.security.CustomUserDetailsService;
import com.example.quantum.infra.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Permite o pre-flight OPTIONS do CORS para todas as rotas
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Login público
                        .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()

                        // 3. Usuários
                        .requestMatchers(HttpMethod.POST, "/v1/users").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/v1/users/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/v1/users/**").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/v1/users/**").authenticated()

                        // 4. Documentos
                        .requestMatchers(HttpMethod.GET, "/v1/documents/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/documents").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        .requestMatchers(HttpMethod.PUT, "/v1/documents/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/v1/documents/**").hasAuthority("ROLE_ADMINISTRADOR")

                        // 5. Processos
                        .requestMatchers(HttpMethod.GET, "/v1/processes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/processes").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        .requestMatchers(HttpMethod.PUT, "/v1/processes/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/v1/processes/**").hasAuthority("ROLE_ADMINISTRADOR")

                        // 6. NÃO-CONFORMIDADES (NC)
                        .requestMatchers(HttpMethod.GET, "/v1/nc/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/nc").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        .requestMatchers(HttpMethod.PUT, "/v1/nc/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/v1/nc/**").hasAuthority("ROLE_ADMINISTRADOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite origens do Flutter (localhost ou IPs do emulador)
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*", "http://10.0.2.2:*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}