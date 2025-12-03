import 'package:flutter/material.dart';
import '../services/auth_service.dart';
import '../pages/users_page.dart';
import '../pages/documents_page.dart';// Adicionado: Import para DocumentsPage
import '../pages/processes_page.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final AuthService _authService = AuthService();
  Map<String, String?> _currentUser = {};
  bool _isLoading = true;
  bool _hasManagementPermission = false;

  // Índice para rastrear a página selecionada no menu
  int _selectedIndex = 0;

  @override
  void initState() {
    super.initState();
    _loadUserData();
  }

  Future<void> _loadUserData() async {
    final user = await _authService.getCurrentUser();
    final hasPermission = await _authService.hasManagementPermission();

    setState(() {
      _currentUser = user;
      _hasManagementPermission = hasPermission;
      _isLoading = false;
    });
  }

  Future<void> _handleLogout() async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: const Row(
          children: [
            Icon(Icons.logout_rounded, color: Colors.orange),
            SizedBox(width: 12),
            Text('Sair'),
          ],
        ),
        content: const Text('Tem certeza que deseja sair?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(false),
            child: const Text('Cancelar'),
          ),
          FilledButton(
            onPressed: () => Navigator.of(context).pop(true),
            style: FilledButton.styleFrom(backgroundColor: Colors.orange),
            child: const Text('Sair'),
          ),
        ],
      ),
    );

    if (confirmed == true && mounted) {
      await _authService.logout();
      Navigator.of(context).pushReplacementNamed('/login');
    }
  }

  String _formatPosition(String? position) {
    if (position == null) return '';
    return position
        .replaceAll('_', ' ')
        .split(' ')
        .map((word) => word[0].toUpperCase() + word.substring(1).toLowerCase())
        .join(' ');
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    }

    // Verificar se a tela é larga o suficiente para NavigationRail permanente
    final isWideScreen = MediaQuery.of(context).size.width > 600;

    return Scaffold(
      backgroundColor: Theme.of(context).colorScheme.surfaceContainerLowest,
      appBar: AppBar(
        title: const Text('Quantum'),
        backgroundColor: Theme.of(context).colorScheme.surface,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.logout_rounded),
            onPressed: _handleLogout,
            tooltip: 'Sair',
          ),
          const SizedBox(width: 8),
        ],
      ),
      drawer: isWideScreen ? null : _buildDrawer(),  // Drawer só em telas pequenas
      body: Row(
        children: [
          if (isWideScreen) _buildNavigationRail(),  // Menu lateral permanente em telas largas
          Expanded(
            child: _buildDashboardContent(),  // Conteúdo dinâmico baseado no índice selecionado
          ),
        ],
      ),
    );
  }

  // Drawer para telas pequenas (abre/fecha)
  Drawer _buildDrawer() {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          DrawerHeader(
            decoration: BoxDecoration(
              color: Theme.of(context).colorScheme.primary,
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                CircleAvatar(
                  radius: 28,
                  backgroundColor: Theme.of(context).colorScheme.surface,
                  child: Text(
                    (_currentUser['username'] ?? '?')[0].toUpperCase(),
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: Theme.of(context).colorScheme.primary,
                    ),
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  _currentUser['username'] ?? 'Usuário',
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Text(
                  _formatPosition(_currentUser['position']),
                  style: const TextStyle(
                    color: Colors.white70,
                    fontSize: 14,
                  ),
                ),
              ],
            ),
          ),
          _buildMenuItem(0, Icons.home_rounded, 'Home'),
          _buildMenuItem(1, Icons.people_rounded, 'Usuários'),
          _buildMenuItem(2, Icons.description_rounded, 'Documentos'),
          _buildMenuItem(3, Icons.assignment_rounded, 'Processos'),
          _buildMenuItem(4, Icons.warning_rounded, 'Não Conformidades'),
          _buildMenuItem(5, Icons.settings_rounded, 'Configurações'),
          const Divider(),
          ListTile(
            leading: const Icon(Icons.logout_rounded),
            title: const Text('Sair'),
            onTap: () {
              Navigator.pop(context);
              _handleLogout();
            },
          ),
        ],
      ),
    );
  }

  // NavigationRail para telas largas (menu permanente)
  Widget _buildNavigationRail() {
    return NavigationRail(
      selectedIndex: _selectedIndex,
      onDestinationSelected: (index) {
        setState(() => _selectedIndex = index);
      },
      labelType: NavigationRailLabelType.all,  // Mostra labels sempre
      destinations: const [
        NavigationRailDestination(
          icon: Icon(Icons.home_rounded),
          label: Text('Home'),
        ),
        NavigationRailDestination(
          icon: Icon(Icons.people_rounded),
          label: Text('Usuários'),
        ),
        NavigationRailDestination(
          icon: Icon(Icons.description_rounded),
          label: Text('Documentos'),
        ),
        NavigationRailDestination(
          icon: Icon(Icons.assignment_rounded),
          label: Text('Processos'),
        ),
        NavigationRailDestination(
          icon: Icon(Icons.warning_rounded),
          label: Text('Não Conformidades'),
        ),
        NavigationRailDestination(
          icon: Icon(Icons.settings_rounded),
          label: Text('Configurações'),
        ),
      ],
      trailing: Expanded(  // Adiciona logout no final do rail
        child: Align(
          alignment: Alignment.bottomCenter,
          child: Padding(
            padding: const EdgeInsets.only(bottom: 8.0),
            child: IconButton(
              icon: const Icon(Icons.logout_rounded),
              onPressed: _handleLogout,
              tooltip: 'Sair',
            ),
          ),
        ),
      ),
    );
  }

  // Conteúdo dinâmico do dashboard baseado no índice selecionado
  Widget _buildDashboardContent() {
    switch (_selectedIndex) {
      case 0:
        return _buildHomeContent();  // Conteúdo original da home (cards ou dashboard inicial)
      case 1:
        return const UsersPage();
      case 2:
        return const DocumentsPage();
      case 3:
        return const ProcessesPage(); // <--- ATUALIZADO AQUI  // Substitua por sua página
      case 4:
        return const Center(child: Text('Em desenvolvimento: Não Conformidades'));
      case 5:
        return const Center(child: Text('Em desenvolvimento: Configurações'));
      default:
        return const Center(child: Text('Página não encontrada'));
    }
  }

  // Conteúdo da home original (cards) - movido para um método separado
  Widget _buildHomeContent() {
    final screenWidth = MediaQuery.of(context).size.width;
    int crossAxisCount = 2; // Padrão para telas pequenas
    if (screenWidth > 600) crossAxisCount = 3; // Para telas médias (ex.: tablets)
    if (screenWidth > 900) crossAxisCount = 4; // Para telas grandes

    return CustomScrollView(
      slivers: [
        // Header com informações do usuário
        SliverToBoxAdapter(
          child: Container(
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                colors: [
                  Theme.of(context).colorScheme.primaryContainer,
                  Theme.of(context).colorScheme.surface,
                ],
              ),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    CircleAvatar(
                      radius: 30,
                      backgroundColor: Theme.of(context).colorScheme.primary,
                      child: Text(
                        (_currentUser['username'] ?? '?')[0].toUpperCase(),
                        style: const TextStyle(
                          fontSize: 24,
                          fontWeight: FontWeight.bold,
                          color: Colors.white,
                        ),
                      ),
                    ),
                    const SizedBox(width: 16),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            'Olá, ${_currentUser['username'] ?? 'Usuário'}!',
                            style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          const SizedBox(height: 4),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                            decoration: BoxDecoration(
                              color: Theme.of(context).colorScheme.primary.withOpacity(0.2),
                              borderRadius: BorderRadius.circular(20),
                            ),
                            child: Text(
                              _formatPosition(_currentUser['position']),
                              style: TextStyle(
                                fontSize: 12,
                                fontWeight: FontWeight.w600,
                                color: Theme.of(context).colorScheme.primary,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),

        // Módulos/Funcionalidades - Ajustado para ser mais compacto
        SliverPadding(
          padding: const EdgeInsets.all(16), // Reduzido de 24 para 16
          sliver: SliverGrid(
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: crossAxisCount, // Dinâmico: 2, 3 ou 4 colunas
              mainAxisSpacing: 12, // Reduzido de 16 para 12
              crossAxisSpacing: 12, // Reduzido de 16 para 12
              childAspectRatio: 1.5, // Mais largo (reduz altura, cabem mais na tela)
            ),
            delegate: SliverChildListDelegate([
              _buildModuleCard(
                context,
                icon: Icons.people_rounded,
                title: 'Usuários',
                subtitle: 'Gestão de usuários',
                color: Colors.green,
                onTap: () {
                  setState(() => _selectedIndex = 1);  // Muda o dashboard para Usuários
                },
              ),
              _buildModuleCard(
                context,
                icon: Icons.description_rounded,
                title: 'Documentos',
                subtitle: 'Documentos e dados',
                color: Colors.orange,
                onTap: () {
                  setState(() => _selectedIndex = 2);  // Muda o dashboard para Documentos
                },
              ),
              _buildModuleCard(
                context,
                icon: Icons.assignment_rounded,
                title: 'Processos',
                subtitle: 'Processos e Informações Adicionais',
                color: Colors.blue,
                onTap: () {
                  setState(() => _selectedIndex = 3);  // Muda o dashboard para Processos
                },
              ),
              _buildModuleCard(
                context,
                icon: Icons.warning_rounded,
                title: 'Não Conformidades',
                subtitle: 'Não Conformidades e Informações Adicionais',
                color: Colors.red,
                onTap: () {
                  setState(() => _selectedIndex = 4);  // Muda o dashboard para Não Conformidades
                },
              ),
              _buildModuleCard(
                context,
                icon: Icons.settings_rounded,
                title: 'Configurações',
                subtitle: 'Ajustes do sistema',
                color: Colors.purple,
                onTap: () {
                  setState(() => _selectedIndex = 5);  // Muda o dashboard para Configurações
                },
              ),
            ]),
          ),
        ),

        // Informações de permissão - Ajustado para ser mais compacto
        SliverToBoxAdapter(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12), // Reduzido
            child: Container(
              padding: const EdgeInsets.all(12), // Reduzido de 16 para 12
              decoration: BoxDecoration(
                color: _hasManagementPermission
                    ? Colors.green.withOpacity(0.1)
                    : Colors.blue.withOpacity(0.1),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(
                  color: _hasManagementPermission
                      ? Colors.green.withOpacity(0.3)
                      : Colors.blue.withOpacity(0.3),
                ),
              ),
              child: Row(
                children: [
                  Icon(
                    _hasManagementPermission
                        ? Icons.admin_panel_settings_rounded
                        : Icons.visibility_rounded,
                    color: _hasManagementPermission ? Colors.green : Colors.blue,
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          _hasManagementPermission
                              ? 'Acesso de Gerenciamento'
                              : 'Acesso de Visualização',
                          style: TextStyle(
                            fontWeight: FontWeight.w600,
                            color: _hasManagementPermission ? Colors.green.shade700 : Colors.blue.shade700,
                          ),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          _hasManagementPermission
                              ? 'Você pode criar, editar e excluir dados'
                              : 'Você pode visualizar dados, mas não editá-los',
                          style: TextStyle(
                            fontSize: 12,
                            color: (_hasManagementPermission ? Colors.green : Colors.blue).withOpacity(0.8),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }

  // Item de menu genérico para Drawer
  ListTile _buildMenuItem(int index, IconData icon, String title) {
    return ListTile(
      leading: Icon(icon),
      title: Text(title),
      selected: _selectedIndex == index,
      onTap: () {
        setState(() => _selectedIndex = index);
        Navigator.pop(context);  // Fecha o drawer em telas pequenas
      },
    );
  }

  // Método _buildModuleCard mantido igual ao anterior
  Widget _buildModuleCard(
      BuildContext context, {
        required IconData icon,
        required String title,
        required String subtitle,
        required Color color,
        required VoidCallback onTap,
      }) {
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
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          onTap: onTap,
          borderRadius: BorderRadius.circular(16),
          child: Padding(
            padding: const EdgeInsets.all(16), // Reduzido de 20 para 16
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Container(
                  padding: const EdgeInsets.all(12), // Reduzido de 16 para 12
                  decoration: BoxDecoration(
                    color: color.withOpacity(0.1),
                    shape: BoxShape.circle,
                  ),
                  child: Icon(
                    icon,
                    size: 28, // Reduzido de 32 para 28
                    color: color,
                  ),
                ),
                const SizedBox(height: 12), // Reduzido de 16 para 12
                Text(
                  title,
                  style: const TextStyle(
                    fontSize: 14, // Reduzido de 16 para 14
                    fontWeight: FontWeight.w600,
                  ),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 4),
                Text(
                  subtitle,
                  style: TextStyle(
                    fontSize: 11, // Reduzido de 12 para 11
                    color: Theme.of(context).colorScheme.onSurfaceVariant,
                  ),
                  textAlign: TextAlign.center,
                  maxLines: 2, // Para evitar overflow se subtitle for longa
                  overflow: TextOverflow.ellipsis,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}