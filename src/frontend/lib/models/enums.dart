// ========== ENUMS DE USUÁRIO ==========

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

// ========== ENUMS DE DOCUMENTO ==========

enum DocumentType {
  REGISTRO,
  PROCEDIMENTO,
  INSTRUCAO_TECNICA,
  FORMULARIO,
  REGULAMENTO,
  SISTEMA_INFORMATIZADO
}

enum DocumentOrigin {
  INTERNO,
  EXTERNO
}


enum Cycle {
  P,
  D,
  C,
  A
}

enum Priority {
  LOW,
  MEDIUM,
  HIGH;

  String get displayName {
    switch (this) {
      case Priority.LOW: return 'Baixa';
      case Priority.MEDIUM: return 'Média';
      case Priority.HIGH: return 'Alta';
    }
  }
}

// ========== EXTENSÃO PARA FORMATAÇÃO DE EXIBIÇÃO ==========

extension EnumFormatting on Enum {
  /// Retorna o nome formatado para exibição (com espaços e capitalizado)
  String get displayName {
    return name
        .replaceAll('_', ' ')
        .split(' ')
        .map((word) => word[0].toUpperCase() + word.substring(1).toLowerCase())
        .join(' ');
  }

  /// Retorna o nome original do enum (como está definido)
  /// Para enviar ao backend exatamente como esperado
  String get apiValue {
    return name;
  }
}

// ========== HELPERS PARA CONVERSÃO ==========

class EnumHelper {
  // Usuários
  static Position? positionFromString(String? value) {
    if (value == null) return null;
    try {
      return Position.values.firstWhere((e) => e.name == value);
    } catch (e) {
      return null;
    }
  }

  static Sector? sectorFromString(String? value) {
    if (value == null) return null;
    try {
      return Sector.values.firstWhere((e) => e.name == value);
    } catch (e) {
      return null;
    }
  }

  // Documentos
  static DocumentType? documentTypeFromString(String? value) {
    if (value == null) return null;
    try {
      return DocumentType.values.firstWhere((e) => e.name == value);
    } catch (e) {
      return null;
    }
  }

  static DocumentOrigin? documentOriginFromString(String? value) {
    if (value == null) return null;
    try {
      return DocumentOrigin.values.firstWhere((e) => e.name == value);
    } catch (e) {
      return null;
    }
  }

  //Process
  static Cycle? cycleFromString(String? value) {
    if (value == null) return null;
    try {
      return Cycle.values.firstWhere((e) => e.name == value);
    } catch (e) {
      return null;
    }
  }
}
