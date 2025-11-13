enum Position {
  ADMINISTRADOR,
  DIRETOR,
  GESTOR,
  ENGENHEIRO,
  ANALISTA,
  ESTAGIARIO
}

enum Sector {
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
  TECNOLOGIA_DA_INFORMACAO,
  DETALHAMENTO,
  COMERCIAL,
  PINTURA,
  MANUTENCAO
}

extension EnumFormatting on Enum {
  String get displayName {
    return name
        .replaceAll('_', ' ')
        .split(' ')
        .map((word) => word[0].toUpperCase() + word.substring(1).toLowerCase())
        .join(' ');
  }
}