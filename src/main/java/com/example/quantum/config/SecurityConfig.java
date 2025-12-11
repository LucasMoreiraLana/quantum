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

                        // 1. Permite o pre-flight OPTIONS do CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()

                        // =======================================================
                        // ROTAS DE USUÁRIOS
                        // =======================================================

                        // 👇 Somente ADMIN pode criar usuários
                        .requestMatchers(HttpMethod.POST, "/v1/users").hasAuthority("ROLE_ADMINISTRADOR")

                        // 👇 ADMIN e GESTOR podem atualizar
                        .requestMatchers(HttpMethod.PUT, "/v1/users/**")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")

                        // 👇 Apenas ADMIN pode deletar
                        .requestMatchers(HttpMethod.DELETE, "/v1/users/**").hasAuthority("ROLE_ADMINISTRADOR")

                        // 👇 Todos os cargos podem visualizar usuários, exceto anônimos
                        .requestMatchers(HttpMethod.GET, "/v1/users/**").authenticated()

                        // =======================================================
                        // ROTAS DE DOCUMENTOS
                        // =======================================================

                        .requestMatchers(HttpMethod.GET, "/v1/documents").authenticated()
                        // CORREÇÃO: Usando hasAnyAuthority com prefixo ROLE_
                        .requestMatchers(HttpMethod.POST, "/v1/documents").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        // CORREÇÃO: Usando hasAnyAuthority com prefixo ROLE_
                        .requestMatchers(HttpMethod.PUT, "/v1/documents/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/v1/documents/**").hasAuthority("ROLE_ADMINISTRADOR")


                        // =======================================================
                        // ROTAS DE PROCESSOS
                        // =======================================================

                        // 👇 GET: Todos autenticados podem visualizar processos
                        .requestMatchers(HttpMethod.GET, "/v1/processes/**").authenticated()

                        // CORREÇÃO: Usando hasAnyAuthority com prefixo ROLE_
                        .requestMatchers(HttpMethod.POST, "/v1/processes").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")

                        // CORREÇÃO: Usando hasAnyAuthority com prefixo ROLE_
                        .requestMatchers(HttpMethod.PUT, "/v1/processes/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_GESTOR")

                        // 👇 DELETE: Apenas ADMIN pode deletar
                        .requestMatchers(HttpMethod.DELETE, "/v1/processes/**").hasAuthority("ROLE_ADMINISTRADOR")


                        // Qualquer outra requisição deve ser autenticada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // A configuração de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("http://localhost:*");
        configuration.addAllowedOriginPattern("http://127.0.0.1:*");
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
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