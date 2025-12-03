package com.example.quantum.enums;

import com.fasterxml.jackson.annotation.JsonValue; // <-- ADICIONE ESTE IMPORT!

public enum Sector {

    ADMINISTRATIVO,
    ALMOXARIFADO,
    COMPRAS,
    DEPARTAMENTO_PESSOAL,
    RECURSOS_HUMANOS,
    FINANCEIRO,
    INSPECAO,
    DIRECAO,
    QUALIDADE,
    PLANEJAMENTO,
    SEGURANCA_DO_TRABALHO,
    TECNOLOGIA_DA_INFORMACAO, // <-- Mantenha o nome aqui (com sublinhados)
    DETALHAMENTO,
    COMERCIAL,
    PINTURA,
    MANUTENCAO;

    // 💡 Método para dizer ao Jackson para usar o nome exato (com sublinhados)
    @JsonValue
    public String toValue() {
        // Retorna o nome da constante, que deve ter os sublinhados (TECNOLOGIA_DA_INFORMACAO)
        return this.name();
    }
}