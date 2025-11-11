import 'dart:convert';
import 'package:http/http.dart' as http;

class ApiService {
  // Ajuste a URL base conforme sua configuração
  final String baseUrl = 'http://localhost:8080/v1';

  // ============= MÉTODOS EXISTENTES =============

  Future<List<dynamic>> getUsers() async {
    final response = await http.get(Uri.parse('$baseUrl/users'));
    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Falha ao carregar usuários: ${response.statusCode}');
    }
  }

  Future<Map<String, dynamic>> getUserById(String userId) async {
    final response = await http.get(Uri.parse('$baseUrl/users/$userId'));
    if (response.statusCode == 200) {
      return json.decode(response.body);
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
    final response = await http.post(
      Uri.parse('$baseUrl/users'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'username': username,
        'email': email,
        'password': password,
        'active': active,
        'sector': sector,
        'position': position,
      }),
    );

    if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception('Falha ao criar usuário: ${response.body}');
    }
  }

  // ============= NOVOS MÉTODOS =============

  /// Atualiza os dados de um usuário
  Future<Map<String, dynamic>> updateUser(
      String userId, {
        required String username,
        required String email,
        String? password, // Senha opcional (só envia se foi alterada)
        required bool active,
        required String sector,
        required String position,
      }) async {
    final Map<String, dynamic> body = {
      'username': username,
      'email': email,
      'active': active,
      'sector': sector,
      'position': position,
    };

    // Só inclui a senha se foi fornecida (permite editar sem alterar senha)
    if (password != null && password.isNotEmpty) {
      body['password'] = password;
    }

    final response = await http.put(
      Uri.parse('$baseUrl/users/$userId'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(body),
    );

    if (response.statusCode == 200) {
      // Se a API retornar o usuário atualizado
      if (response.body.isNotEmpty) {
        return json.decode(response.body);
      }
      // Se retornar vazio (status 200 sem body)
      return {'success': true};
    } else {
      final errorMessage = response.body.isNotEmpty
          ? json.decode(response.body)['message'] ?? 'Erro desconhecido'
          : 'Erro ao atualizar usuário';
      throw Exception(errorMessage);
    }
  }

  /// Alterna o status ativo/inativo de um usuário
  /// Primeiro busca o usuário, depois atualiza apenas o campo 'active'
  Future<void> toggleUserStatus(String userId, bool newStatus) async {
    // Busca os dados atuais do usuário
    final currentUser = await getUserById(userId);

    // Atualiza mantendo todos os outros campos
    final response = await http.put(
      Uri.parse('$baseUrl/users/$userId'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'username': currentUser['username'],
        'email': currentUser['email'],
        'active': newStatus,
        'sector': currentUser['sector'],
        'position': currentUser['position'],
        // Não enviamos a senha na atualização de status
      }),
    );

    if (response.statusCode != 200) {
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