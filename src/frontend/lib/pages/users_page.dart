import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../pages/user_detail_page.dart';
import '../../models/enums.dart';  // Ajuste o caminho relativo se necessário
import '../services/auth_service.dart';

class UsersPage extends StatefulWidget {
  const UsersPage({super.key});

  @override
  State<UsersPage> createState() => _UsersPageState();
}

class _UsersPageState extends State<UsersPage> with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  final AuthService authService = AuthService();
  List<dynamic> users = [];
  List<dynamic> filteredUsers = [];
  bool _loading = true;
  String? _error;
  final TextEditingController _searchController = TextEditingController();
  late AnimationController _fabAnimationController;

  // Filtro de status: 'all', 'active', 'inactive'
  String _statusFilter = 'active';

  // Permissões do usuário
  bool _hasManagementPermission = false;

  @override
  void initState() {
    super.initState();
    _fabAnimationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 300),
    );
    _loadData();  // Carrega permissões e usuários
  }

  Future<void> _loadData() async {
    _hasManagementPermission = await authService.hasManagementPermission();
    setState(() {});  // Atualiza o estado para refletir a permissão
    await loadUsers();
  }

  @override
  void dispose() {
    _searchController.dispose();
    _fabAnimationController.dispose();
    super.dispose();
  }

  Future<void> loadUsers() async {
    if (mounted && !_loading) {
      setState(() {
        _loading = true;
        _error = null;
      });
    }

    try {
      final data = await api.getUsers();
      if (mounted) {
        setState(() {
          users = data;
          _applyFilters();
          _loading = false;
          _error = null;
        });
        _fabAnimationController.forward();
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _loading = false;
          _error = e.toString();
          users = [];
          filteredUsers = [];
        });
      }
    }
  }

  void _applyFilters() {
    List<dynamic> result = users;

    // Filtro por status
    if (_statusFilter == 'active') {
      result = result.where((user) => user['active'] == true).toList();
    } else if (_statusFilter == 'inactive') {
      result = result.where((user) => user['active'] == false).toList();
    }

    // Filtro por busca
    if (_searchController.text.isNotEmpty) {
      final searchLower = _searchController.text.toLowerCase();
      result = result.where((user) {
        final username = (user['username'] ?? '').toString().toLowerCase();
        final email = (user['email'] ?? '').toString().toLowerCase();
        final position = (user['position'] ?? '').toString().toLowerCase();

        return username.contains(searchLower) ||
            email.contains(searchLower) ||
            position.contains(searchLower);
      }).toList();
    }

    setState(() {
      filteredUsers = result;
    });
  }

  void _filterUsers(String query) {
    _applyFilters();
  }

  void _changeStatusFilter(String filter) {
    setState(() {
      _statusFilter = filter;
      _applyFilters();
    });
  }

  void _showCreateUserDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext dialogContext) {
        return _CreateUserForm(
          api: api,
          onUserCreated: () {
            loadUsers();
            if (mounted) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: const Row(
                    children: [
                      Icon(Icons.check_circle, color: Colors.white),
                      SizedBox(width: 12),
                      Text('Usuário criado com sucesso!'),
                    ],
                  ),
                  backgroundColor: Colors.green.shade600,
                  behavior: SnackBarBehavior.floating,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                ),
              );
            }
          },
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).colorScheme.surfaceContainerLowest,
      body: CustomScrollView(
        slivers: [
          // AppBar com busca integrada
          SliverAppBar(
            floating: true,
            pinned: true,
            elevation: 0,
            backgroundColor: Theme.of(context).colorScheme.surface,
            expandedHeight: 120,
            flexibleSpace: FlexibleSpaceBar(
              title: const Text(
                'Gestão de Usuários',
                style: TextStyle(fontWeight: FontWeight.w600),
              ),
              background: Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [
                      Theme.of(context).colorScheme.primary.withOpacity(0.1),
                      Theme.of(context).colorScheme.surface,
                    ],
                  ),
                ),
              ),
            ),
            actions: [
              IconButton(
                icon: const Icon(Icons.refresh_rounded),
                onPressed: _loading ? null : loadUsers,
                tooltip: 'Recarregar',
              ),
              const SizedBox(width: 8),
            ],
          ),

          // Barra de busca
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
              child: _buildSearchBar(),
            ),
          ),

          // Filtros de status (Todos, Ativos, Inativos)
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              child: _buildStatusFilters(),
            ),
          ),

          // Estatísticas rápidas
          if (!_loading && _error == null && users.isNotEmpty)
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                child: _buildStatsCards(),
              ),
            ),

          // Conteúdo principal
          SliverToBoxAdapter(
            child: _buildBody(),
          ),
        ],
      ),
      floatingActionButton: _hasManagementPermission  // Só mostra se tiver permissão
          ? ScaleTransition(
        scale: CurvedAnimation(
          parent: _fabAnimationController,
          curve: Curves.easeOutBack,
        ),
        child: FloatingActionButton.extended(
          onPressed: _showCreateUserDialog,
          icon: const Icon(Icons.person_add_rounded),
          label: const Text('Novo Usuário'),
          elevation: 4,
        ),
      )
          : null,
    );
  }

  Widget _buildSearchBar() {
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.surface,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.05),
            blurRadius: 10,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: TextField(
        controller: _searchController,
        onChanged: _filterUsers,
        decoration: InputDecoration(
          hintText: 'Buscar usuários...',
          prefixIcon: const Icon(Icons.search_rounded),
          suffixIcon: _searchController.text.isNotEmpty
              ? IconButton(
            icon: const Icon(Icons.clear_rounded),
            onPressed: () {
              _searchController.clear();
              _filterUsers('');
            },
          )
              : null,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(16),
            borderSide: BorderSide.none,
          ),
          filled: true,
          fillColor: Theme.of(context).colorScheme.surface,
          contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
        ),
      ),
    );
  }

  Widget _buildStatsCards() {
    final activeUsers = users.where((u) => u['active'] == true).length;
    final inactiveUsers = users.length - activeUsers;

    return Row(
      children: [
        Expanded(
          child: _buildStatCard(
            icon: Icons.people_rounded,
            label: 'Total',
            value: users.length.toString(),
            color: Colors.blue,
            onTap: () => _changeStatusFilter('all'),
            isSelected: _statusFilter == 'all',
          ),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: _buildStatCard(
            icon: Icons.check_circle_rounded,
            label: 'Ativos',
            value: activeUsers.toString(),
            color: Colors.green,
            onTap: () => _changeStatusFilter('active'),
            isSelected: _statusFilter == 'active',
          ),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: _buildStatCard(
            icon: Icons.remove_circle_rounded,
            label: 'Inativos',
            value: inactiveUsers.toString(),
            color: Colors.orange,
            onTap: () => _changeStatusFilter('inactive'),
            isSelected: _statusFilter == 'inactive',
          ),
        ),
      ],
    );
  }

  Widget _buildStatCard({
    required IconData icon,
    required String label,
    required String value,
    required Color color,
    required VoidCallback onTap,
    required bool isSelected,
  }) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: isSelected ? color.withOpacity(0.2) : color.withOpacity(0.1),
            borderRadius: BorderRadius.circular(12),
            border: Border.all(
              color: isSelected ? color : color.withOpacity(0.3),
              width: isSelected ? 2 : 1,
            ),
          ),
          child: Column(
            children: [
              Icon(icon, color: color, size: 28),
              const SizedBox(height: 8),
              Text(
                value,
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                  color: color,
                ),
              ),
              Text(
                label,
                style: TextStyle(
                  fontSize: 12,
                  color: color.withOpacity(0.8),
                  fontWeight: isSelected ? FontWeight.w600 : FontWeight.normal,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildStatusFilters() {
    return Row(
      children: [
        Expanded(
          child: _buildFilterChip(
            label: 'Todos',
            icon: Icons.people_rounded,
            isSelected: _statusFilter == 'all',
            onTap: () => _changeStatusFilter('all'),
            color: Colors.blue,
          ),
        ),
        const SizedBox(width: 8),
        Expanded(
          child: _buildFilterChip(
            label: 'Ativos',
            icon: Icons.check_circle_rounded,
            isSelected: _statusFilter == 'active',
            onTap: () => _changeStatusFilter('active'),
            color: Colors.green,
          ),
        ),
        const SizedBox(width: 8),
        Expanded(
          child: _buildFilterChip(
            label: 'Inativos',
            icon: Icons.remove_circle_rounded,
            isSelected: _statusFilter == 'inactive',
            onTap: () => _changeStatusFilter('inactive'),
            color: Colors.orange,
          ),
        ),
      ],
    );
  }

  Widget _buildFilterChip({
    required String label,
    required IconData icon,
    required bool isSelected,
    required VoidCallback onTap,
    required Color color,
  }) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(20),
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 12),
          decoration: BoxDecoration(
            color: isSelected ? color : Theme.of(context).colorScheme.surface,
            borderRadius: BorderRadius.circular(20),
            border: Border.all(
              color: isSelected ? color : Theme.of(context).colorScheme.outline.withOpacity(0.3),
              width: isSelected ? 2 : 1,
            ),
            boxShadow: isSelected ? [
              BoxShadow(
                color: color.withOpacity(0.3),
                blurRadius: 8,
                offset: const Offset(0, 2),
              ),
            ] : [],
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                icon,
                size: 18,
                color: isSelected ? Colors.white : color,
              ),
              const SizedBox(width: 6),
              Text(
                label,
                style: TextStyle(
                  color: isSelected ? Colors.white : Theme.of(context).colorScheme.onSurface,
                  fontWeight: isSelected ? FontWeight.w600 : FontWeight.w500,
                  fontSize: 13,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildBody() {
    if (_loading) {
      return const Center(
        child: Padding(
          padding: EdgeInsets.all(64.0),
          child: CircularProgressIndicator(),
        ),
      );
    }

    if (_error != null) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.errorContainer,
                  shape: BoxShape.circle,
                ),
                child: Icon(
                  Icons.error_outline_rounded,
                  color: Theme.of(context).colorScheme.error,
                  size: 48,
                ),
              ),
              const SizedBox(height: 24),
              Text(
                'Erro ao carregar usuários',
                style: Theme.of(context).textTheme.titleLarge?.copyWith(
                  fontWeight: FontWeight.w600,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 12),
              Text(
                _error!,
                style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                  color: Theme.of(context).colorScheme.onSurfaceVariant,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 32),
              FilledButton.icon(
                icon: const Icon(Icons.refresh_rounded),
                label: const Text('Tentar Novamente'),
                onPressed: loadUsers,
              ),
            ],
          ),
        ),
      );
    }

    if (filteredUsers.isEmpty) {
      final isSearching = _searchController.text.isNotEmpty;
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.primaryContainer,
                  shape: BoxShape.circle,
                ),
                child: Icon(
                  isSearching ? Icons.search_off_rounded : Icons.people_outline_rounded,
                  color: Theme.of(context).colorScheme.primary,
                  size: 48,
                ),
              ),
              const SizedBox(height: 24),
              Text(
                isSearching ? 'Nenhum resultado encontrado' : 'Nenhum usuário cadastrado',
                style: Theme.of(context).textTheme.titleLarge?.copyWith(
                  fontWeight: FontWeight.w600,
                ),
              ),
              const SizedBox(height: 12),
              Text(
                isSearching
                    ? 'Tente ajustar os termos da busca'
                    : 'Clique em "Novo Usuário" para começar',
                style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                  color: Theme.of(context).colorScheme.onSurfaceVariant,
                ),
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      );
    }

    return RefreshIndicator(
      onRefresh: loadUsers,
      child: ListView.builder(
        shrinkWrap: true,
        physics: const NeverScrollableScrollPhysics(),
        padding: const EdgeInsets.fromLTRB(16, 8, 16, 100),
        itemCount: filteredUsers.length,
        itemBuilder: (context, index) {
          final user = filteredUsers[index];
          return _buildUserCard(user, index);
        },
      ),
    );
  }

  Widget _buildUserCard(Map<String, dynamic> user, int index) {
    final username = user['username'] ?? 'Usuário sem nome';
    final email = user['email'] ?? '';
    final position = user['position'] ?? 'Cargo não definido';
    final isActive = user['active'] == true;

    return TweenAnimationBuilder<double>(
      tween: Tween(begin: 0.0, end: 1.0),
      duration: Duration(milliseconds: 300 + (index * 50)),
      curve: Curves.easeOutCubic,
      builder: (context, value, child) {
        return Transform.translate(
          offset: Offset(0, 20 * (1 - value)),
          child: Opacity(
            opacity: value,
            child: child,
          ),
        );
      },
      child: Container(
        margin: const EdgeInsets.only(bottom: 12),
        decoration: BoxDecoration(
          color: Theme.of(context).colorScheme.surface,
          borderRadius: BorderRadius.circular(16),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.05),
              blurRadius: 10,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Material(
          color: Colors.transparent,
          child: InkWell(
            borderRadius: BorderRadius.circular(16),
            onTap: () {
              final userId = user['userId'];
              if (userId == null || userId.toString().isEmpty) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: const Row(
                      children: [
                        Icon(Icons.error_outline, color: Colors.white),
                        SizedBox(width: 12),
                        Expanded(child: Text('ID do usuário não encontrado!')),
                      ],
                    ),
                    backgroundColor: Colors.red.shade600,
                    behavior: SnackBarBehavior.floating,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                  ),
                );
                return;
              }
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => UserDetailPage(userId: userId.toString()),
                ),
              );
            },
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Row(
                children: [
                  // Avatar com status indicator
                  Stack(
                    children: [
                      Container(
                        width: 56,
                        height: 56,
                        decoration: BoxDecoration(
                          gradient: LinearGradient(
                            colors: [
                              Theme.of(context).colorScheme.primaryContainer,
                              Theme.of(context).colorScheme.primary.withOpacity(0.3),
                            ],
                          ),
                          borderRadius: BorderRadius.circular(16),
                        ),
                        child: Center(
                          child: Text(
                            username.isNotEmpty ? username[0].toUpperCase() : '?',
                            style: TextStyle(
                              color: Theme.of(context).colorScheme.onPrimaryContainer,
                              fontWeight: FontWeight.bold,
                              fontSize: 24,
                            ),
                          ),
                        ),
                      ),
                      Positioned(
                        right: 0,
                        bottom: 0,
                        child: Container(
                          width: 16,
                          height: 16,
                          decoration: BoxDecoration(
                            color: isActive ? Colors.green : Colors.grey,
                            shape: BoxShape.circle,
                            border: Border.all(
                              color: Theme.of(context).colorScheme.surface,
                              width: 2,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(width: 16),
                  // Informações do usuário
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          username,
                          style: const TextStyle(
                            fontWeight: FontWeight.w600,
                            fontSize: 16,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 4),
                        Text(
                          position,
                          style: TextStyle(
                            color: Theme.of(context).colorScheme.primary,
                            fontSize: 13,
                            fontWeight: FontWeight.w500,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        if (email.isNotEmpty) ...[
                          const SizedBox(height: 4),
                          Row(
                            children: [
                              Icon(
                                Icons.email_outlined,
                                size: 14,
                                color: Theme.of(context).colorScheme.onSurfaceVariant,
                              ),
                              const SizedBox(width: 4),
                              Expanded(
                                child: Text(
                                  email,
                                  style: TextStyle(
                                    color: Theme.of(context).colorScheme.onSurfaceVariant,
                                    fontSize: 12,
                                  ),
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                              ),
                            ],
                          ),
                        ],
                      ],
                    ),
                  ),
                  // Ícone de navegação
                  Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: Theme.of(context).colorScheme.primaryContainer.withOpacity(0.5),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Icon(
                      Icons.arrow_forward_ios_rounded,
                      size: 16,
                      color: Theme.of(context).colorScheme.primary,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

// WIDGET: Formulário de Criação de Usuário
class _CreateUserForm extends StatefulWidget {
  final ApiService api;
  final VoidCallback onUserCreated;

  const _CreateUserForm({
    required this.api,
    required this.onUserCreated,
  });

  @override
  State<_CreateUserForm> createState() => __CreateUserFormState();
}

class __CreateUserFormState extends State<_CreateUserForm> {
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();

  bool _isActive = true;
  bool _obscurePassword = true;
  Sector? _selectedSector;
  Position? _selectedPosition;

  bool _isLoading = false;
  String? _errorMessage;

  @override
  void dispose() {
    _usernameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  Future<void> _submitForm() async {
    if (!(_formKey.currentState?.validate() ?? false)) {
      return;
    }

    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      await widget.api.createUser(
        _usernameController.text,
        _emailController.text,
        _passwordController.text,
        _isActive,
        _selectedSector!.name,
        _selectedPosition!.name,
      );

      if (mounted) {
        Navigator.of(context).pop();
      }
      widget.onUserCreated();
    } catch (e) {
      setState(() {
        _isLoading = false;
        _errorMessage = e.toString().replaceFirst('Exception: ', '');
      });
    }
  }

  InputDecoration _buildInputDecoration(String label, IconData icon, {Widget? suffix}) {
    return InputDecoration(
      labelText: label,
      prefixIcon: Icon(icon),
      suffixIcon: suffix,
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
      ),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: BorderSide(color: Theme.of(context).colorScheme.outline.withOpacity(0.5)),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: BorderSide(color: Theme.of(context).colorScheme.primary, width: 2),
      ),
      filled: true,
      fillColor: Theme.of(context).colorScheme.surfaceContainerHighest,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      child: Container(
        constraints: const BoxConstraints(maxWidth: 500),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            // Header do Dialog
            Container(
              padding: const EdgeInsets.all(24),
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [
                    Theme.of(context).colorScheme.primaryContainer,
                    Theme.of(context).colorScheme.surface,
                  ],
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                ),
                borderRadius: const BorderRadius.only(
                  topLeft: Radius.circular(20),
                  topRight: Radius.circular(20),
                ),
              ),
              child: Row(
                children: [
                  Container(
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: Theme.of(context).colorScheme.primary.withOpacity(0.2),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Icon(
                      Icons.person_add_rounded,
                      color: Theme.of(context).colorScheme.primary,
                      size: 28,
                    ),
                  ),
                  const SizedBox(width: 16),
                  Text(
                    'Novo Usuário',
                    style: Theme.of(context).textTheme.titleLarge?.copyWith(
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
              ),
            ),

            // Formulário
            Flexible(
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(24),
                child: Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      TextFormField(
                        controller: _usernameController,
                        decoration: _buildInputDecoration('Nome de Usuário', Icons.person_outlined),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Campo obrigatório';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _emailController,
                        decoration: _buildInputDecoration('E-mail', Icons.email_outlined),
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
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _passwordController,
                        decoration: _buildInputDecoration(
                          'Senha',
                          Icons.lock_outlined,
                          suffix: IconButton(
                            icon: Icon(_obscurePassword ? Icons.visibility_outlined : Icons.visibility_off_outlined),
                            onPressed: () => setState(() => _obscurePassword = !_obscurePassword),
                          ),
                        ),
                        obscureText: _obscurePassword,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Campo obrigatório';
                          }
                          if (value.length < 6) {
                            return 'Mínimo de 6 caracteres';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<Position>(
                        value: _selectedPosition,
                        decoration: _buildInputDecoration('Cargo', Icons.work_outlined),
                        items: Position.values.map((pos) {
                          return DropdownMenuItem(
                            value: pos,
                            child: Text(pos.displayName),
                          );
                        }).toList(),
                        onChanged: (value) => setState(() => _selectedPosition = value),
                        validator: (value) => value == null ? 'Campo obrigatório' : null,
                      ),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<Sector>(
                        value: _selectedSector,
                        decoration: _buildInputDecoration('Setor', Icons.business_outlined),
                        items: Sector.values.map((sec) {
                          return DropdownMenuItem(
                            value: sec,
                            child: Text(sec.displayName),
                          );
                        }).toList(),
                        onChanged: (value) => setState(() => _selectedSector = value),
                        validator: (value) => value == null ? 'Campo obrigatório' : null,
                      ),
                      const SizedBox(height: 20),
                      Container(
                        decoration: BoxDecoration(
                          color: _isActive
                              ? Colors.green.withOpacity(0.1)
                              : Colors.grey.withOpacity(0.1),
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: _isActive
                                ? Colors.green.withOpacity(0.3)
                                : Colors.grey.withOpacity(0.3),
                          ),
                        ),
                        child: SwitchListTile(
                          title: const Text('Usuário Ativo'),
                          subtitle: Text(
                            _isActive ? 'Este usuário poderá acessar o sistema' : 'Este usuário não poderá acessar',
                            style: const TextStyle(fontSize: 12),
                          ),
                          value: _isActive,
                          onChanged: (value) => setState(() => _isActive = value),
                          secondary: Icon(
                            _isActive ? Icons.check_circle_rounded : Icons.remove_circle_rounded,
                            color: _isActive ? Colors.green : Colors.grey,
                          ),
                        ),
                      ),
                      if (_isLoading) ...[
                        const SizedBox(height: 24),
                        const Center(child: CircularProgressIndicator()),
                      ],
                      if (_errorMessage != null) ...[
                        const SizedBox(height: 16),
                        Container(
                          padding: const EdgeInsets.all(12),
                          decoration: BoxDecoration(
                            color: Theme.of(context).colorScheme.errorContainer,
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Row(
                            children: [
                              Icon(Icons.error_outline, color: Theme.of(context).colorScheme.error),
                              const SizedBox(width: 12),
                              Expanded(
                                child: Text(
                                  _errorMessage!,
                                  style: TextStyle(color: Theme.of(context).colorScheme.error),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ],
                  ),
                ),
              ),
            ),

            // Botões de ação
            Container(
              padding: const EdgeInsets.all(24),
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.surfaceContainerHighest,
                borderRadius: const BorderRadius.only(
                  bottomLeft: Radius.circular(20),
                  bottomRight: Radius.circular(20),
                ),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  TextButton(
                    onPressed: _isLoading ? null : () => Navigator.of(context).pop(),
                    child: const Text('Cancelar'),
                  ),
                  const SizedBox(width: 12),
                  FilledButton.icon(
                    onPressed: _isLoading ? null : _submitForm,
                    icon: const Icon(Icons.save_rounded),
                    label: const Text('Salvar'),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}