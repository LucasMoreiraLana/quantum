import 'dart:convert';
import 'package:http/http.dart' as http;
import '../services/auth_service.dart';

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

  // ============= MÉTODOS EXISTENTES (AGORA COM AUTH) =============

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
      bool active,
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
        'active': active,
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

  // ============= NOVOS MÉTODOS (AGORA COM AUTH) =============

  /// Atualiza os dados de um usuário
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

  /// Alterna o status ativo/inativo de um usuário
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

  /// Busca apenas usuários ativos
  Future<List<dynamic>> getActiveUsers() async {
    final allUsers = await getUsers();
    return allUsers.where((user) => user['active'] == true).toList();
  }

  /// Busca apenas usuários inativos
  Future<List<dynamic>> getInactiveUsers() async {
    final allUsers = await getUsers();
    return allUsers.where((user) => user['active'] == false).toList();
  }
}