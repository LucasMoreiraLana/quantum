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

// ========== EXTENSÃO PARA FORMATAÇÃO ==========

extension EnumFormatting on Enum {
  String get displayName {
    return name
        .replaceAll('_', ' ')
        .split(' ')
        .map((word) => word[0].toUpperCase() + word.substring(1).toLowerCase())
        .join(' ');
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
}