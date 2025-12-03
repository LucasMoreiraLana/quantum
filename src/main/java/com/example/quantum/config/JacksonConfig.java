package com.example.quantum.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        // Cria o ObjectMapper padrão
        ObjectMapper objectMapper = new ObjectMapper();

        // Registra o módulo Java Time
        objectMapper.registerModule(new JavaTimeModule());

        // Opcional: Desabilita a falha se não conseguir serializar um LocalDate/LocalDateTime não anotado
        // Isso pode ser útil em cenários específicos, mas a linha acima deve ser suficiente.
        // objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }
}