import 'package:flutter/material.dart';
import '../services/api_service.dart'; // Ajuste o path se necessário (ex.: '../../services/api_service.dart' se services estiver fora de pages)

class UserDetailPage extends StatefulWidget {
final String userId;

const UserDetailPage({super.key, required this.userId});

@override
State<UserDetailPage> createState() => _UserDetailPageState();
}

class _UserDetailPageState extends State<UserDetailPage> {
final ApiService api = ApiService();
Map<String, dynamic>? user;
bool _loading = true;
String? _error;

@override
void initState() {
super.initState();
loadUserDetails();
}

Future<void> loadUserDetails() async {
setState(() {
_loading = true;
_error = null;
});

try {
final data = await api.getUserById(widget.userId);
print('JSON dos detalhes: $data'); // Debug: Verifique o JSON aqui
setState(() {
user = data;
_loading = false;
});
} catch (e) {
setState(() {
_loading = false;
_error = e.toString();
});
}
}

@override
Widget build(BuildContext context) {
return Scaffold(
appBar: AppBar(
title: const Text('Detalhes do Usuário'),
backgroundColor: Theme.of(context).colorScheme.surfaceVariant,
actions: [
IconButton(
icon: const Icon(Icons.refresh),
onPressed: _loading ? null : loadUserDetails,
tooltip: 'Recarregar',
),
],
),
body: _buildBody(),
);
}

Widget _buildBody() {
if (_loading) {
return const Center(child: CircularProgressIndicator());
}

if (_error != null) {
return Center(
child: Column(
mainAxisAlignment: MainAxisAlignment.center,
children: [
const Icon(Icons.error_outline, color: Colors.red, size: 60),
const SizedBox(height: 16),
Text('Erro ao carregar detalhes', style: Theme.of(context).textTheme.headlineSmall),
const SizedBox(height: 8),
Text(_error!, textAlign: TextAlign.center),
const SizedBox(height: 20),
ElevatedButton.icon(
icon: const Icon(Icons.refresh),
label: const Text('Tentar Novamente'),
onPressed: loadUserDetails,
),
],
),
);
}

if (user == null) {
return const Center(child: Text('Usuário não encontrado'));
}

// Exibe os detalhes com chaves ajustadas do JSON
return ListView(
padding: const EdgeInsets.all(16.0),
children: [
ListTile(
leading: const Icon(Icons.person),
title: const Text('ID do Usuário'),
subtitle: Text(user!['userId'] ?? 'Não disponível'),
),
ListTile(
leading: const Icon(Icons.person_outline),
title: const Text('Nome de Usuário'),
subtitle: Text(user!['userName'] ?? 'Não disponível'), // Ajustado para 'userName'
),
ListTile(
leading: const Icon(Icons.email),
title: const Text('E-mail'),
subtitle: Text(user!['email'] ?? 'Não disponível'),
),
ListTile(
leading: const Icon(Icons.check_circle),
title: const Text('Ativo'),
subtitle: Text(user!['active'] == true ? 'Sim' : 'Não'),
),
ListTile(
leading: const Icon(Icons.business),
title: const Text('Setor'),
subtitle: Text(user!['sector'] ?? 'Não disponível'),
),
ListTile(
leading: const Icon(Icons.work),
title: const Text('Cargo'),
subtitle: Text(user!['position'] ?? 'Não disponível'),
),
// Adicione mais campos se o JSON tiver outros
],
);
}
}