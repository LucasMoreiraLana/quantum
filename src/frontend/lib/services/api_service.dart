import 'dart:convert';
import 'package:http/http.dart' as http;
import 'auth_service.dart';

class ApiService {
  // Ajuste a URL base conforme sua configuração
  final String baseUrl = 'http://localhost:8080/v1';
  final AuthService _authService = AuthService();

  // ============= MÉTODOS COM AUTENTICAÇÃO =============

  /// Helper para pegar o token
  Future<Map<String, String>> _getHeaders() async {
    final token = await _authService.getToken();
    return {
      'Content-Type': 'application/json',
      if (token != null) 'Authorization': 'Bearer $token',
    };
  }

  // ============= USUÁRIOS =============

  Future<List<dynamic>> getUsers() async {
    final headers = await _getHeaders();
    final response = await http.get(Uri.parse('$baseUrl/users'), headers: headers);
    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else {
      throw Exception('Falha ao carregar usuários: ${response.statusCode}');
    }
  }

  Future<Map<String, dynamic>> getUserById(String userId) async {
    final headers = await _getHeaders();
    final response = await http.get(Uri.parse('$baseUrl/users/$userId'), headers: headers);
    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else {
      throw Exception('Falha ao carregar detalhes do usuário: ${response.statusCode}');
    }
  }

  Future<void> createUser(
      String username,
      String email,
      String password,
      String sector,
      String position,
      ) async {
    final headers = await _getHeaders();
    final response = await http.post(
      Uri.parse('$baseUrl/users'),
      headers: headers,
      body: json.encode({
        'username': username,
        'email': email,
        'password': password,
        'sector': sector,
        'position': position,
      }),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception('Falha ao criar usuário: ${response.body}');
    }
  }

  Future<Map<String, dynamic>> updateUser(
      String userId, {
        required String username,
        required String email,
        String? password,
        required bool active,
        required String sector,
        required String position,
      }) async {
    final headers = await _getHeaders();
    final Map<String, dynamic> body = {
      'username': username,
      'email': email,
      'active': active,
      'sector': sector,
      'position': position,
    };

    if (password != null && password.isNotEmpty) {
      body['password'] = password;
    }

    final response = await http.put(
      Uri.parse('$baseUrl/users/$userId'),
      headers: headers,
      body: json.encode(body),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode == 200) {
      if (response.body.isNotEmpty) {
        return json.decode(response.body);
      }
      return {'success': true};
    } else {
      final errorMessage = response.body.isNotEmpty
          ? json.decode(response.body)['message'] ?? 'Erro desconhecido'
          : 'Erro ao atualizar usuário';
      throw Exception(errorMessage);
    }
  }

  Future<void> toggleUserStatus(String userId, bool newStatus) async {
    final currentUser = await getUserById(userId);
    final headers = await _getHeaders();

    final response = await http.put(
      Uri.parse('$baseUrl/users/$userId'),
      headers: headers,
      body: json.encode({
        'username': currentUser['username'],
        'email': currentUser['email'],
        'active': newStatus,
        'sector': currentUser['sector'],
        'position': currentUser['position'],
      }),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200) {
      final errorMessage = response.body.isNotEmpty
          ? json.decode(response.body)['message'] ?? 'Erro desconhecido'
          : 'Erro ao alterar status do usuário';
      throw Exception(errorMessage);
    }
  }

  Future<List<dynamic>> getActiveUsers() async {
    final allUsers = await getUsers();
    return allUsers.where((user) => user['active'] == true).toList();
  }

  Future<List<dynamic>> getInactiveUsers() async {
    final allUsers = await getUsers();
    return allUsers.where((user) => user['active'] == false).toList();
  }

  // ============= DOCUMENTOS =============

  Future<List<dynamic>> getDocuments() async {
    final headers = await _getHeaders();
    final response = await http.get(Uri.parse('$baseUrl/documents'), headers: headers);

    if (response.statusCode == 200) {
      final List<dynamic> docs = json.decode(response.body);
      return docs;
    } else if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else {
      throw Exception('Falha ao carregar documentos: ${response.statusCode}');
    }
  }

  Future<Map<String, dynamic>> getDocumentById(String documentId) async {
    final headers = await _getHeaders();
    final response = await http.get(Uri.parse('$baseUrl/documents/$documentId'), headers: headers);
    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode == 404) {
      throw Exception('Documento não encontrado');
    } else {
      throw Exception('Falha ao carregar documento: ${response.statusCode}');
    }
  }

  Future<List<dynamic>> getActiveDocuments() async {
    final allDocuments = await getDocuments();
    return allDocuments.where((doc) => doc['active'] == true).toList();
  }

  Future<List<dynamic>> getInactiveDocuments() async {
    final allDocuments = await getDocuments();
    return allDocuments.where((doc) => doc['active'] == false).toList();
  }

  Future<void> createDocument({
    required String nameDocument,
    required String content,
    required int tempoDeRetencao, // Argumento pode manter o 'De' para leitura no Dart
    required String type,
    required String origin,
    required String sector,
  }) async {
    final headers = await _getHeaders();
    final response = await http.post(
      Uri.parse('$baseUrl/documents'),
      headers: headers,
      body: json.encode({
        'nameDocument': nameDocument,
        'content': content,
        'tempoDeRetencao': tempoDeRetencao,
        'type': type,
        'origin': origin,
        'sector': sector,
      }),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200 && response.statusCode != 201) {
      // Tenta extrair mensagem de erro do backend
      try {
        final errorBody = json.decode(response.body);
        throw Exception(errorBody['message'] ?? 'Falha ao criar documento');
      } catch (e) {
        throw Exception('Falha ao criar documento: ${response.body}');
      }
    }
  }

  Future<void> updateDocument({
    required String documentId,
    required String nameDocument,
    required String content,
    required int tempoDeRetencao,
    required bool active,
    required String type,
    required String origin,
    required String sector,
  }) async {
    final headers = await _getHeaders();
    final response = await http.put(
      Uri.parse('$baseUrl/documents/$documentId'),
      headers: headers,
      body: json.encode({
        'nameDocument': nameDocument,
        'content': content,
        'tempoDeRetencao': tempoDeRetencao,
        'active': active,
        'type': type,
        'origin': origin,
        'sector': sector,
      }),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200) {
      // Tenta extrair mensagem de erro do backend
      try {
        final errorBody = json.decode(response.body);
        throw Exception(errorBody['message'] ?? 'Falha ao atualizar documento');
      } catch (e) {
        throw Exception('Falha ao atualizar documento: ${response.body}');
      }
    }
  }

  // Substitua o método toggleDocumentStatus no api_service.dart por este:

  Future<void> toggleDocumentStatus(String documentId, bool newStatus) async {
    print('\n🌐 API: toggleDocumentStatus');
    print('   DocumentId: $documentId');
    print('   Novo Status (active): $newStatus');

    final currentDoc = await getDocumentById(documentId);

    print('   📄 Documento atual:');
    print('      - Nome: ${currentDoc['nameDocument']}');
    print('      - Active ANTES: ${currentDoc['active']}');

    final headers = await _getHeaders();

    final bodyData = {
      'nameDocument': currentDoc['nameDocument'],
      'content': currentDoc['content'],
      'tempoDeRetencao': currentDoc['tempoDeRetencao'],
      'active': newStatus,  // ← ESTE É O CAMPO CRÍTICO
      'type': currentDoc['type'],
      'origin': currentDoc['origin'],
      'sector': currentDoc['sector'],
    };

    print('   📤 Body enviado para API:');
    print('      ${json.encode(bodyData)}');

    final response = await http.put(
      Uri.parse('$baseUrl/documents/$documentId'),
      headers: headers,
      body: json.encode(bodyData),
    );

    print('   📥 Resposta da API:');
    print('      Status Code: ${response.statusCode}');
    print('      Body: ${response.body}');

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200) {
      final errorMessage = response.body.isNotEmpty
          ? json.decode(response.body)['message'] ?? 'Erro desconhecido'
          : 'Erro ao alterar status do documento';
      throw Exception(errorMessage);
    }

    print('   ✅ Status alterado com sucesso!\n');
  }

  // ============= PROCESSOS =============

  Future<List<dynamic>> getProcesses() async {
    final headers = await _getHeaders();
    final response = await http.get(Uri.parse('$baseUrl/processes'), headers: headers);

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else {
      throw Exception('Falha ao carregar processos: ${response.statusCode}');
    }
  }

  Future<Map<String, dynamic>> getProcessById(String processId) async {
    final headers = await _getHeaders();
    final response = await http.get(Uri.parse('$baseUrl/processes/$processId'), headers: headers);

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else {
      throw Exception('Falha ao carregar detalhes do processo: ${response.statusCode}');
    }
  }

  Future<void> createProcess({
    required String nameProcess,
    required String dateApproval, // Formato yyyy-MM-dd
    required String dateConclusion, // Formato yyyy-MM-dd
    required String sector,
    required String cyclePDCA,
  }) async {
    final headers = await _getHeaders();
    final response = await http.post(
      Uri.parse('$baseUrl/processes'),
      headers: headers,
      body: json.encode({
        'nameProcess': nameProcess,
        'dateApproval': dateApproval,
        'dateConclusion': dateConclusion,
        'sector': sector,
        'cyclePDCA': cyclePDCA,
      }),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200 && response.statusCode != 201) {
      try {
        final errorBody = json.decode(response.body);
        throw Exception(errorBody['message'] ?? 'Falha ao criar processo');
      } catch (e) {
        throw Exception('Falha ao criar processo: ${response.body}');
      }
    }
  }

  Future<void> updateProcess({
    required String processId,
    required String nameProcess,
    required String dateApproval,
    required String dateConclusion,
    required String sector,
    required String cyclePDCA,
  }) async {
    final headers = await _getHeaders();
    final response = await http.put(
      Uri.parse('$baseUrl/processes/$processId'),
      headers: headers,
      body: json.encode({
        'nameProcess': nameProcess,
        'dateApproval': dateApproval,
        'dateConclusion': dateConclusion,
        'sector': sector,
        'cyclePDCA': cyclePDCA,
      }),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200) {
      try {
        final errorBody = json.decode(response.body);
        throw Exception(errorBody['message'] ?? 'Falha ao atualizar processo');
      } catch (e) {
        throw Exception('Falha ao atualizar processo: ${response.body}');
      }
    }
  }
// ============= NÃO-CONFORMIDADES =============

  Future<List<dynamic>> getNonCompliances() async {
    final headers = await _getHeaders();
    final response = await http.get(Uri.parse('$baseUrl/nc'), headers: headers);  // Mudado para /nc

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else {
      throw Exception('Falha ao carregar não-conformidades: ${response.statusCode}');
    }
  }

  Future<Map<String, dynamic>> getNonComplianceById(String nonComplianceId) async {
    final headers = await _getHeaders();
    final response = await http.get(Uri.parse('$baseUrl/nc/$nonComplianceId'), headers: headers);  // Mudado para /nc/{id}

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else {
      throw Exception('Falha ao carregar detalhes da não-conformidade: ${response.statusCode}');
    }
  }

  Future<void> createNonCompliance({
    required String createdBy,
    required String dateOpening, // Formato yyyy-MM-dd
    required String processId,
    required String sector,
    required String origin,
    required String priority,
    required String customer,
    required String description,
    required bool efficacy,
    required String datePrevision, // Formato yyyy-MM-dd
  }) async {
    final headers = await _getHeaders();
    final response = await http.post(
      Uri.parse('$baseUrl/nc'),  // Mudado para /nc
      headers: headers,
      body: json.encode({
        'createdBy': createdBy,
        'dateOpening': dateOpening,
        'processId': processId,
        'sector': sector,
        'origin': origin,
        'priority': priority,
        'customer': customer,
        'description': description,
        'efficacy': efficacy,
        'datePrevision': datePrevision,
      }),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200 && response.statusCode != 201) {
      try {
        final errorBody = json.decode(response.body);
        throw Exception(errorBody['message'] ?? 'Falha ao criar não-conformidade');
      } catch (e) {
        throw Exception('Falha ao criar não-conformidade: ${response.body}');
      }
    }
  }

  Future<void> updateNonCompliance({
    required String nonComplianceId,
    required String createdBy,
    required String dateOpening,
    required String processId,
    required String sector,
    required String origin,
    required String priority,
    required String customer,
    required String description,
    required bool efficacy,
    required String datePrevision,
  }) async {
    final headers = await _getHeaders();
    final response = await http.put(
      Uri.parse('$baseUrl/nc/$nonComplianceId'),  // Mudado para /nc/{id}
      headers: headers,
      body: json.encode({
        'createdBy': createdBy,
        'dateOpening': dateOpening,
        'processId': processId,
        'sector': sector,
        'origin': origin,
        'priority': priority,
        'customer': customer,
        'description': description,
        'efficacy': efficacy,
        'datePrevision': datePrevision,
      }),
    );

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200) {
      try {
        final errorBody = json.decode(response.body);
        throw Exception(errorBody['message'] ?? 'Falha ao atualizar não-conformidade');
      } catch (e) {
        throw Exception('Falha ao atualizar não-conformidade: ${response.body}');
      }
    }
  }

  Future<void> deleteNonCompliance(String nonComplianceId) async {
    final headers = await _getHeaders();
    final response = await http.delete(Uri.parse('$baseUrl/nc/$nonComplianceId'), headers: headers);  // Mudado para /nc/{id}

    if (response.statusCode == 401) {
      throw Exception('Sessão expirada. Faça login novamente.');
    } else if (response.statusCode != 200) {
      throw Exception('Falha ao deletar não-conformidade: ${response.statusCode}');
    }
  }
}