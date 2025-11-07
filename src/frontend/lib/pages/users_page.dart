import 'package:flutter/material.dart';
import '../services/api_service.dart';

// --- NOVAS DEFINIÇÕES DE ENUM ---
// Baseado no seu Java record
enum Position {
  ADMINISTRADOR,
  DIRETOR,
  GESTOR,
  ENGENHEIRO,
  ANALISTA,
  ESTAGIARIO
}

// !! ATENÇÃO !!
// Você precisa substituir este enum placeholder
// pelos valores do seu enum `Sector` do backend.
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
// --- FIM DAS NOVAS DEFINIÇÕES ---

class UsersPage extends StatefulWidget {
  const UsersPage({super.key});

  @override
  State<UsersPage> createState() => _UsersPageState();
}

class _UsersPageState extends State<UsersPage> {
  final ApiService api = ApiService();
  List<dynamic> users = [];
  bool _loading = true;
  String? _error; // Para armazenar a mensagem de erro

  @override
  void initState() {
    super.initState();
    loadUsers();
  }

  Future<void> loadUsers() async {
    // Garante que o estado de loading seja ativado no início
    if (!_loading) {
      setState(() {
        _loading = true;
        _error = null;
      });
    }

    try {
      final data = await api.getUsers();
      setState(() {
        users = data;
        _loading = false;
        _error = null; // Limpa qualquer erro anterior
      });
    } catch (e) {
      setState(() {
        _loading = false;
        _error = e.toString(); // Armazena a mensagem de erro
        users = []; // Limpa os usuários em caso de erro
      });
      // O SnackBar pode ser mantido ou removido,
      // já que agora exibimos o erro na tela.
      // ScaffoldMessenger.of(context)
      //     .showSnackBar(SnackBar(content: Text('Erro: $_error')));
    }
  }

  /// Mostra o diálogo para criar um novo usuário
  void _showCreateUserDialog() {
    showDialog(
      context: context,
      barrierDismissible: false, // O usuário não pode fechar clicando fora
      builder: (BuildContext dialogContext) {
        return _CreateUserForm(
          api: api,
          // Passamos uma função de callback para recarregar a lista
          onUserCreated: () {
            loadUsers(); // Recarrega a lista de usuários
            // Mostra uma mensagem de sucesso
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text('Usuário criado com sucesso!'),
                backgroundColor: Colors.green,
              ),
            );
          },
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Usando uma cor do tema Material 3 para a AppBar
      appBar: AppBar(
        title: const Text('Gestão de Usuários'),
        // Um tom sutil que combina com o body
        backgroundColor: Theme.of(context).colorScheme.surfaceVariant,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _loading ? null : loadUsers, // Só permite recarregar se não estiver carregando
            tooltip: 'Recarregar',
          ),
        ],
      ),
      body: _buildBody(), // Usamos um método auxiliar para limpar o build
      floatingActionButton: FloatingActionButton(
        onPressed: _showCreateUserDialog, // ATUALIZADO: Chama o diálogo
        tooltip: 'Adicionar Usuário',
        child: const Icon(Icons.add),
      ),
    );
  }

  /// Método auxiliar para construir o corpo da página
  /// tratando os diferentes estados (loading, error, empty, data)
  Widget _buildBody() {
    if (_loading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (_error != null) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.error_outline, color: Colors.red, size: 60),
              const SizedBox(height: 16),
              Text(
                'Erro ao carregar usuários',
                style: Theme.of(context).textTheme.headlineSmall,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 8),
              Text(
                _error!,
                style: Theme.of(context).textTheme.bodyMedium,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 20),
              ElevatedButton.icon(
                icon: const Icon(Icons.refresh),
                label: const Text('Tentar Novamente'),
                onPressed: loadUsers,
              )
            ],
          ),
        ),
      );
    }

    if (users.isEmpty) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.people_outline, color: Colors.grey, size: 60),
              const SizedBox(height: 16),
              Text(
                'Nenhum usuário encontrado',
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 8),
              Text(
                'Tente adicionar um novo usuário.',
                style: Theme.of(context).textTheme.bodyMedium,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 20),
              ElevatedButton.icon(
                icon: const Icon(Icons.refresh),
                label: const Text('Verificar Novamente'),
                onPressed: loadUsers,
              )
            ],
          ),
        ),
      );
    }

    // Se temos dados, exibimos a lista
    return RefreshIndicator(
      onRefresh: loadUsers, // Permite "puxar para recarregar"
      child: ListView.builder(
        // Adiciona um padding geral na lista
        padding: const EdgeInsets.all(16.0),
        itemCount: users.length,
        itemBuilder: (context, index) {
          final user = users[index];
          final username = user['username'] ?? 'Usuário sem nome';
          final email = user['email'] ?? 'Sem email';

          return Card(
            // O margin já está sendo definido no tema (main.dart)
            // margin: const EdgeInsets.only(bottom: 16), // Apenas um exemplo
            clipBehavior: Clip.antiAlias, // Para o InkWell funcionar bem
            child: InkWell(
              onTap: () {
                // TODO: Navegar para a página de detalhes do usuário
                // Navigator.push(context, MaterialPageRoute(builder: (context) => UserDetailPage(userId: user['id'])));
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Clicou em $username')),
                );
              },
              child: ListTile(
                // Ícone principal
                leading: CircleAvatar(
                  backgroundColor: Theme.of(context).colorScheme.primaryContainer,
                  child: Text(
                    username.isNotEmpty ? username[0].toUpperCase() : '?',
                    style: TextStyle(
                      color: Theme.of(context).colorScheme.onPrimaryContainer,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                // Título
                title: Text(
                  username,
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
                // Subtítulo
                subtitle: Text(
                  email,
                  style: TextStyle(color: Colors.grey.shade600),
                ),
                // Ícone à direita (indica que é clicável)
                trailing: const Icon(Icons.arrow_forward_ios, size: 16),
              ),
            ),
          );
        },
      ),
    );
  }
}

// NOVO WIDGET: Formulário de Criação de Usuário
// Este é um widget com seu-próprio-estado para ser usado dentro do diálogo
class _CreateUserForm extends StatefulWidget {
  final ApiService api;
  final VoidCallback onUserCreated; // Callback para recarregar a lista

  const _CreateUserForm({
    required this.api,
    required this.onUserCreated,
  });

  @override
  State<_CreateUserForm> createState() => __CreateUserFormState();
}

class __CreateUserFormState extends State<_CreateUserForm> {
  final _formKey = GlobalKey<FormState>(); // Chave para validar o formulário
  final _usernameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();

  // NOVOS CAMPOS DE ESTADO
  bool _isActive = true; // Padrão: usuário é criado ativo
  Sector? _selectedSector;
  Position? _selectedPosition;

  bool _isLoading = false;
  String? _errorMessage;

  @override
  void dispose() {
    // Limpa os controllers quando o widget é removido
    _usernameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  Future<void> _submitForm() async {
    // Verifica se o formulário é válido
    if (_formKey.currentState?.validate() ?? false) {
      setState(() {
        _isLoading = true;
        _errorMessage = null;
      });

      try {
        // Chama a API
        await widget.api.createUser(
          _usernameController.text,
          _emailController.text,
          _passwordController.text,
          _isActive, // Enviando o valor do switch
          _selectedSector!.name, // Enviando o nome do enum
          _selectedPosition!.name, // Enviando o nome do enum
        );

        // Se deu certo, fecha o diálogo
        if (mounted) {
          Navigator.of(context).pop();
        }
        // Chama o callback para atualizar a lista na UsersPage
        widget.onUserCreated();

      } catch (e) {
        // Se deu erro, exibe a mensagem no diálogo
        setState(() {
          _isLoading = false;
          _errorMessage = e.toString().replaceFirst('Exception: ', '');
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Novo Usuário'),
      // SingleChildScrollView evita que o teclado quebre o layout
      content: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min, // Ocupa o mínimo de espaço
            children: [
              TextFormField(
                controller: _usernameController,
                decoration: const InputDecoration(
                  labelText: 'Nome de Usuário',
                  icon: Icon(Icons.person),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Campo obrigatório';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16), // Espaçamento
              TextFormField(
                controller: _emailController,
                decoration: const InputDecoration(
                  labelText: 'E-mail',
                  icon: Icon(Icons.email),
                ),
                keyboardType: TextInputType.emailAddress,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Campo obrigatório';
                  }
                  if (!value.contains('@')) {
                    return 'E-mail inválido';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16), // Espaçamento
              TextFormField(
                controller: _passwordController,
                decoration: const InputDecoration(
                  labelText: 'Senha',
                  icon: Icon(Icons.lock),
                ),
                obscureText: true, // Esconde a senha
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Campo obrigatório';
                  }
                  if (value.length < 6) {
                    return 'Senha deve ter pelo menos 6 caracteres';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16), // Espaçamento

              // NOVO CAMPO: CARGO (POSITION)
              DropdownButtonFormField<Position>(
                value: _selectedPosition,
                decoration: const InputDecoration(
                  labelText: 'Cargo',
                  icon: Icon(Icons.work),
                ),
                items: Position.values.map((pos) {
                  return DropdownMenuItem(
                    value: pos,
                    child: Text(pos.name), // Exibe 'ADMINISTRADOR', 'DIRETOR', etc.
                  );
                }).toList(),
                onChanged: (Position? newValue) {
                  setState(() {
                    _selectedPosition = newValue;
                  });
                },
                validator: (value) => value == null ? 'Campo obrigatório' : null,
              ),
              const SizedBox(height: 16), // Espaçamento

              // NOVO CAMPO: SETOR (SECTOR)
              DropdownButtonFormField<Sector>(
                value: _selectedSector,
                decoration: const InputDecoration(
                  labelText: 'Setor',
                  icon: Icon(Icons.business),
                ),
                items: Sector.values.map((sec) {
                  return DropdownMenuItem(
                    value: sec,
                    child: Text(sec.name), // Exibe 'TI', 'FINANCEIRO', etc.
                  );
                }).toList(),
                onChanged: (Sector? newValue) {
                  setState(() {
                    _selectedSector = newValue;
                  });
                },
                validator: (value) => value == null ? 'Campo obrigatório' : null,
              ),
              const SizedBox(height: 16), // Espaçamento

              // NOVO CAMPO: ATIVO (ACTIVE)
              SwitchListTile(
                title: const Text('Usuário Ativo'),
                value: _isActive,
                onChanged: (bool value) {
                  setState(() {
                    _isActive = value;
                  });
                },
                secondary: Icon(_isActive
                    ? Icons.check_circle
                    : Icons.remove_circle_outline),
              ),

              const SizedBox(height: 16),
              // Exibe um indicador de loading enquanto cria o usuário
              if (_isLoading)
                const CircularProgressIndicator()
              // Exibe uma mensagem de erro se a criação falhar
              else if (_errorMessage != null)
                Text(
                  _errorMessage!,
                  style: TextStyle(color: Theme.of(context).colorScheme.error),
                ),
            ],
          ),
        ),
      ),
      actions: [
        // Botão de Cancelar
        TextButton(
          onPressed: _isLoading ? null : () => Navigator.of(context).pop(),
          child: const Text('Cancelar'),
        ),
        // Botão de Salvar
        ElevatedButton(
          onPressed: _isLoading ? null : _submitForm, // Desabilitado se estiver carregando
          child: const Text('Salvar'),
        ),
      ],
    );
  }
}