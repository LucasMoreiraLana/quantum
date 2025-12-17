import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

class AuthService {
  final String baseUrl = 'http://localhost:8080/v1';
  final FlutterSecureStorage _storage = const FlutterSecureStorage();

  // Keys para armazenamento seguro
  static const String _tokenKey = 'auth_token';
  static const String _userIdKey = 'user_id';
  static const String _usernameKey = 'username';
  static const String _emailKey = 'email';
  static const String _positionKey = 'position';
  static const String _sectorKey = 'sector';
  static const String _activeKey = 'active';

  // ============= LOGIN =============

  /// Realiza o login do usuário
  /// Retorna os dados do usuário se sucesso, lança exceção se falhar
  Future<Map<String, dynamic>> login(String email, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/login'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode({
          'email': email,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final String token = data['token'];
        final String name = data['name'];

        // Salva o token
        await _storage.write(key: _tokenKey, value: token);
        await _storage.write(key: _usernameKey, value: name);

        // Decodifica o token para pegar o userId
        final userId = _getUserIdFromToken(token);
        await _storage.write(key: _userIdKey, value: userId);

        // Busca os dados completos do usuário
        final userData = await _fetchUserData(userId, token);

        // Valida se o usuário está ativo
        if (userData['active'] != true) {
          await logout(); // Limpa os dados
          throw Exception('Usuário inativo. Entre em contato com o administrador.');
        }

        // Salva os dados do usuário
        await _saveUserData(userData);

        return userData;
      } else if (response.statusCode == 400) {
        throw Exception('Email ou senha incorretos');
      } else {
        throw Exception('Erro ao fazer login: ${response.statusCode}');
      }
    } catch (e) {
      if (e.toString().contains('Usuário inativo')) {
        rethrow;
      }
      throw Exception('Erro de conexão: $e');
    }
  }

  // ============= TOKEN =============

  /// Extrai o userId do token JWT
  String _getUserIdFromToken(String token) {
    try {
      // Divide o token em suas partes (header.payload.signature)
      final parts = token.split('.');
      if (parts.length != 3) {
        throw Exception('Token inválido');
      }

      // Decodifica o payload (segunda parte)
      final payload = parts[1];
      // Adiciona padding se necessário
      final normalized = base64Url.normalize(payload);
      final decoded = utf8.decode(base64Url.decode(normalized));
      final payloadMap = json.decode(decoded);

      // O subject contém o userId
      return payloadMap['sub'];
    } catch (e) {
      throw Exception('Erro ao decodificar token: $e');
    }
  }

  /// Busca os dados completos do usuário
  Future<Map<String, dynamic>> _fetchUserData(String userId, String token) async {
    final response = await http.get(
      Uri.parse('$baseUrl/users/$userId'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Erro ao buscar dados do usuário');
    }
  }

  /// Salva os dados do usuário no armazenamento seguro
  Future<void> _saveUserData(Map<String, dynamic> userData) async {
    await _storage.write(key: _emailKey, value: userData['email']);
    await _storage.write(key: _positionKey, value: userData['position']);
    await _storage.write(key: _sectorKey, value: userData['sector']);
    await _storage.write(key: _activeKey, value: userData['active'].toString());
  }

  // ============= VERIFICAÇÕES =============

  /// Verifica se o usuário está autenticado
  Future<bool> isAuthenticated() async {
    final token = await getToken();
    if (token == null) return false;

    // Verifica se o token ainda é válido (não expirou)
    if (_isTokenExpired(token)) {
      await logout();
      return false;
    }

    return true;
  }

  /// Verifica se o token está expirado
  bool _isTokenExpired(String token) {
    try {
      final parts = token.split('.');
      if (parts.length != 3) return true;

      final payload = parts[1];
      final normalized = base64Url.normalize(payload);
      final decoded = utf8.decode(base64Url.decode(normalized));
      final payloadMap = json.decode(decoded);

      final exp = payloadMap['exp'];
      if (exp == null) return true;

      final expirationDate = DateTime.fromMillisecondsSinceEpoch(exp * 1000);
      return DateTime.now().isAfter(expirationDate);
    } catch (e) {
      return true;
    }
  }

  /// Retorna o token armazenado
  Future<String?> getToken() async {
    return await _storage.read(key: _tokenKey);
  }

  /// Retorna os dados do usuário logado
  Future<Map<String, String?>> getCurrentUser() async {
    return {
      'userId': await _storage.read(key: _userIdKey),
      'username': await _storage.read(key: _usernameKey),
      'email': await _storage.read(key: _emailKey),
      'position': await _storage.read(key: _positionKey),
      'sector': await _storage.read(key: _sectorKey),
      'active': await _storage.read(key: _activeKey),
    };
  }

  /// Retorna o ID do usuário logado
  Future<String?> getUserId() async {
    final user = await getCurrentUser();
    return user['userId'];
  }

  // ============= PERMISSÕES =============

  /// Verifica se o usuário tem permissão de administrador (acesso total)
  Future<bool> isAdmin() async {
    final position = await _storage.read(key: _positionKey);
    return position == 'ADMINISTRADOR';
  }

  /// Verifica se o usuário é gestor (acesso total)
  Future<bool> isManager() async {
    final position = await _storage.read(key: _positionKey);
    return position == 'GESTOR';
  }

  /// Verifica se o usuário tem permissões de gerenciamento
  /// (ADMINISTRADOR ou GESTOR podem criar, editar e excluir)
  Future<bool> hasManagementPermission() async {
    final position = await _storage.read(key: _positionKey);
    return position == 'ADMINISTRADOR' || position == 'GESTOR';
  }

  /// Verifica se o usuário pode apenas visualizar
  /// (todos os outros cargos)
  Future<bool> isViewOnly() async {
    final hasPermission = await hasManagementPermission();
    return !hasPermission;
  }

  /// Retorna o cargo do usuário
  Future<String?> getUserPosition() async {
    return await _storage.read(key: _positionKey);
  }

  // ============= LOGOUT =============

  /// Faz logout e limpa todos os dados armazenados
  Future<void> logout() async {
    await _storage.deleteAll();
  }

  // ============= REQUISIÇÕES AUTENTICADAS =============

  /// Helper para fazer requisições GET autenticadas
  Future<http.Response> authenticatedGet(String endpoint) async {
    final token = await getToken();
    if (token == null) {
      throw Exception('Usuário não autenticado');
    }

    return await http.get(
      Uri.parse('$baseUrl$endpoint'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );
  }

  /// Helper para fazer requisições POST autenticadas
  Future<http.Response> authenticatedPost(String endpoint, Map<String, dynamic> body) async {
    final token = await getToken();
    if (token == null) {
      throw Exception('Usuário não autenticado');
    }

    return await http.post(
      Uri.parse('$baseUrl$endpoint'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: json.encode(body),
    );
  }

  /// Helper para fazer requisições PUT autenticadas
  Future<http.Response> authenticatedPut(String endpoint, Map<String, dynamic> body) async {
    final token = await getToken();
    if (token == null) {
      throw Exception('Usuário não autenticado');
    }

    return await http.put(
      Uri.parse('$baseUrl$endpoint'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: json.encode(body),
    );
  }

  /// Helper para fazer requisições DELETE autenticadas
  Future<http.Response> authenticatedDelete(String endpoint) async {
    final token = await getToken();
    if (token == null) {
      throw Exception('Usuário não autenticado');
    }

    return await http.delete(
      Uri.parse('$baseUrl$endpoint'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );
  }
}