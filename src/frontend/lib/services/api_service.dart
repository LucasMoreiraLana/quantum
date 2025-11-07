import 'package:http/http.dart' as http;
import 'dart:convert';

class ApiService {
  static const String baseUrl = "http://localhost:8080/v1";

  /// Busca a lista de usuários
  Future<List<dynamic>> getUsers() async {
    final response = await http.get(Uri.parse('$baseUrl/users'));

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Erro ao carregar usuários: ${response.statusCode}');
    }
  }

  /// Cria um novo usuário
  /// Retorna o usuário criado como um Map
  Future<Map<String, dynamic>> createUser(
      String username,
      String email,
      String password,
      bool active,
      String sector, // Enviado como String
      String position, // Enviado como String
      ) async {
    final response = await http.post(
      Uri.parse('$baseUrl/users'),
      headers: <String, String>{
        // Informa à API que estamos enviando JSON
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, dynamic>{ // Alterado para dynamic
        'username': username,
        'email': email,
        'password': password,
        'active': active,
        'sector': sector,
        'position': position,
      }),
    );

    if (response.statusCode == 201) { // 201 (Created) é o status comum para POST
      return jsonDecode(response.body);
    } else {
      // Tenta extrair uma mensagem de erro do corpo da resposta
      String errorMessage = 'Erro ao criar usuário: ${response.statusCode}';
      try {
        final errorBody = jsonDecode(response.body);
        if (errorBody['message'] != null) {
          errorMessage = errorBody['message'];
        }
      } catch (e) {
        // Ignora se o corpo não for um JSON válido
      }
      throw Exception(errorMessage);
    }
  }
}