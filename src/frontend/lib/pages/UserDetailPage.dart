import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../services/api_service.dart';

// Enums (devem estar no mesmo arquivo ou importados)
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

extension EnumFormatting on Enum {
  String get displayName {
    return name
        .replaceAll('_', ' ')
        .split(' ')
        .map((word) => word[0].toUpperCase() + word.substring(1).toLowerCase())
        .join(' ');
  }
}

class UserDetailPage extends StatefulWidget {
  final String userId;

  const UserDetailPage({super.key, required this.userId});

  @override
  State<UserDetailPage> createState() => _UserDetailPageState();
}

class _UserDetailPageState extends State<UserDetailPage> with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  Map<String, dynamic>? user;
  bool _loading = true;
  String? _error;
  late AnimationController _animationController;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 600),
    );
    loadUserDetails();
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  Future<void> loadUserDetails() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      final data = await api.getUserById(widget.userId);
      setState(() {
        user = data;
        _loading = false;
      });
      _animationController.forward();
    } catch (e) {
      setState(() {
        _loading = false;
        _error = e.toString();
      });
    }
  }

  void _copyToClipboard(String text, String label) {
    Clipboard.setData(ClipboardData(text: text));
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Row(
          children: [
            const Icon(Icons.check_circle, color: Colors.white, size: 20),
            const SizedBox(width: 12),
            Text('$label copiado!'),
          ],
        ),
        backgroundColor: Colors.green.shade600,
        behavior: SnackBarBehavior.floating,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
        duration: const Duration(seconds: 2),
      ),
    );
  }

  void _showEditDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext dialogContext) {
        return _EditUserForm(
          api: api,
          user: user!,
          onUserUpdated: () {
            loadUserDetails();
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: const Row(
                  children: [
                    Icon(Icons.check_circle, color: Colors.white),
                    SizedBox(width: 12),
                    Text('Usuário atualizado com sucesso!'),
                  ],
                ),
                backgroundColor: Colors.green.shade600,
                behavior: SnackBarBehavior.floating,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
            );
          },
        );
      },
    );
  }

  void _showToggleStatusDialog() {
    final bool isActive = user!['active'] == true;
    final String action = isActive ? 'desativar' : 'ativar';
    final String actionCapitalized = isActive ? 'Desativar' : 'Ativar';

    showDialog(
      context: context,
      builder: (BuildContext dialogContext) {
        return AlertDialog(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
          title: Row(
            children: [
              Icon(
                isActive ? Icons.block_rounded : Icons.check_circle_rounded,
                color: isActive ? Colors.orange : Colors.green,
              ),
              const SizedBox(width: 12),
              Text('$actionCapitalized Usuário?'),
            ],
          ),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Tem certeza que deseja $action o usuário:',
                style: Theme.of(context).textTheme.bodyMedium,
              ),
              const SizedBox(height: 12),
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.primaryContainer.withOpacity(0.3),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Row(
                  children: [
                    CircleAvatar(
                      backgroundColor: Theme.of(context).colorScheme.primary,
                      child: Text(
                        (user!['username'] ?? '?')[0].toUpperCase(),
                        style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            user!['username'] ?? '',
                            style: const TextStyle(fontWeight: FontWeight.w600),
                          ),
                          Text(
                            user!['email'] ?? '',
                            style: TextStyle(
                              fontSize: 12,
                              color: Theme.of(context).colorScheme.onSurfaceVariant,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 16),
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: (isActive ? Colors.orange : Colors.green).withOpacity(0.1),
                  borderRadius: BorderRadius.circular(8),
                  border: Border.all(
                    color: (isActive ? Colors.orange : Colors.green).withOpacity(0.3),
                  ),
                ),
                child: Row(
                  children: [
                    Icon(
                      Icons.info_outline_rounded,
                      color: isActive ? Colors.orange : Colors.green,
                      size: 20,
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Text(
                        isActive
                            ? 'O usuário será desativado e não poderá acessar o sistema.'
                            : 'O usuário será ativado e poderá acessar o sistema novamente.',
                        style: TextStyle(
                          fontSize: 12,
                          color: isActive ? Colors.orange.shade800 : Colors.green.shade800,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(dialogContext).pop(),
              child: const Text('Cancelar'),
            ),
            FilledButton.icon(
              onPressed: () async {
                Navigator.of(dialogContext).pop();
                await _toggleUserStatus(!isActive);
              },
              icon: Icon(isActive ? Icons.block_rounded : Icons.check_circle_rounded),
              label: Text(actionCapitalized),
              style: FilledButton.styleFrom(
                backgroundColor: isActive ? Colors.orange : Colors.green,
              ),
            ),
          ],
        );
      },
    );
  }

  Future<void> _toggleUserStatus(bool newStatus) async {
    // Mostra loading
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => const Center(child: CircularProgressIndicator()),
    );

    try {
      await api.toggleUserStatus(widget.userId, newStatus);

      if (mounted) {
        Navigator.of(context).pop(); // Remove loading

        // Se desativou, volta para a página anterior
        if (!newStatus) {
          Navigator.of(context).pop();
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: const Row(
                children: [
                  Icon(Icons.check_circle, color: Colors.white),
                  SizedBox(width: 12),
                  Text('Usuário desativado com sucesso!'),
                ],
              ),
              backgroundColor: Colors.orange.shade600,
              behavior: SnackBarBehavior.floating,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            ),
          );
        } else {
          // Se ativou, recarrega os dados
          loadUserDetails();
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: const Row(
                children: [
                  Icon(Icons.check_circle, color: Colors.white),
                  SizedBox(width: 12),
                  Text('Usuário ativado com sucesso!'),
                ],
              ),
              backgroundColor: Colors.green.shade600,
              behavior: SnackBarBehavior.floating,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            ),
          );
        }
      }
    } catch (e) {
      if (mounted) {
        Navigator.of(context).pop(); // Remove loading
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Row(
              children: [
                const Icon(Icons.error_outline, color: Colors.white),
                const SizedBox(width: 12),
                Expanded(child: Text('Erro: ${e.toString()}')),
              ],
            ),
            backgroundColor: Colors.red.shade600,
            behavior: SnackBarBehavior.floating,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
          ),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).colorScheme.surfaceContainerLowest,
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: 200,
            pinned: true,
            elevation: 0,
            backgroundColor: Theme.of(context).colorScheme.surface,
            flexibleSpace: FlexibleSpaceBar(
              title: _loading || user == null
                  ? null
                  : Text(
                user!['username'] ?? 'Usuário',
                style: const TextStyle(fontWeight: FontWeight.w600),
              ),
              background: Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [
                      Theme.of(context).colorScheme.primary,
                      Theme.of(context).colorScheme.primary.withOpacity(0.7),
                    ],
                  ),
                ),
                child: _loading || user == null
                    ? null
                    : Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const SizedBox(height: 40),
                      Hero(
                        tag: 'avatar_${widget.userId}',
                        child: Container(
                          width: 80,
                          height: 80,
                          decoration: BoxDecoration(
                            color: Colors.white,
                            shape: BoxShape.circle,
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withOpacity(0.2),
                                blurRadius: 15,
                                offset: const Offset(0, 5),
                              ),
                            ],
                          ),
                          child: Center(
                            child: Text(
                              (user!['username'] ?? '?')[0].toUpperCase(),
                              style: TextStyle(
                                fontSize: 36,
                                fontWeight: FontWeight.bold,
                                color: Theme.of(context).colorScheme.primary,
                              ),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            actions: [
              IconButton(
                icon: const Icon(Icons.refresh_rounded),
                onPressed: _loading ? null : loadUserDetails,
                tooltip: 'Recarregar',
              ),
              const SizedBox(width: 8),
            ],
          ),
          SliverToBoxAdapter(
            child: _buildBody(),
          ),
        ],
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
              const SizedBox(height: 48),
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
                'Ocorreu um Erro',
                style: Theme.of(context).textTheme.headlineSmall?.copyWith(
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
                onPressed: loadUserDetails,
              ),
            ],
          ),
        ),
      );
    }

    if (user == null) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(32.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                Icons.person_off_rounded,
                size: 60,
                color: Theme.of(context).colorScheme.onSurfaceVariant,
              ),
              const SizedBox(height: 16),
              Text(
                'Usuário não encontrado',
                style: Theme.of(context).textTheme.titleLarge,
              ),
            ],
          ),
        ),
      );
    }

    return FadeTransition(
      opacity: _animationController,
      child: SlideTransition(
        position: Tween<Offset>(
          begin: const Offset(0, 0.1),
          end: Offset.zero,
        ).animate(CurvedAnimation(
          parent: _animationController,
          curve: Curves.easeOutCubic,
        )),
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildStatusBadge(),
              const SizedBox(height: 24),
              _buildPositionCard(),
              const SizedBox(height: 20),
              _buildSectionTitle('Informações Pessoais', Icons.person_rounded),
              const SizedBox(height: 12),
              _buildInfoCard([
                _buildInfoTile(
                  icon: Icons.badge_outlined,
                  label: 'ID do Usuário',
                  value: user!['userId'] ?? 'Não disponível',
                  onCopy: () => _copyToClipboard(
                    user!['userId'] ?? '',
                    'ID',
                  ),
                ),
                _buildInfoTile(
                  icon: Icons.person_outline_rounded,
                  label: 'Nome de Usuário',
                  value: user!['username'] ?? 'Não disponível',
                ),
                _buildInfoTile(
                  icon: Icons.email_outlined,
                  label: 'E-mail',
                  value: user!['email'] ?? 'Não disponível',
                  onCopy: () => _copyToClipboard(
                    user!['email'] ?? '',
                    'E-mail',
                  ),
                ),
              ]),
              const SizedBox(height: 24),
              _buildSectionTitle('Informações Organizacionais', Icons.business_rounded),
              const SizedBox(height: 12),
              _buildInfoCard([
                _buildInfoTile(
                  icon: Icons.business_center_outlined,
                  label: 'Setor',
                  value: _formatEnumName(user!['sector'] ?? 'Não disponível'),
                ),
                _buildInfoTile(
                  icon: Icons.work_outline_rounded,
                  label: 'Cargo',
                  value: _formatEnumName(user!['position'] ?? 'Não disponível'),
                ),
              ]),
              const SizedBox(height: 32),
              _buildActionButtons(),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildStatusBadge() {
    final bool isActive = user!['active'] == true;
    return Center(
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
        decoration: BoxDecoration(
          color: isActive ? Colors.green.withOpacity(0.15) : Colors.grey.withOpacity(0.15),
          borderRadius: BorderRadius.circular(30),
          border: Border.all(
            color: isActive ? Colors.green : Colors.grey,
            width: 2,
          ),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 10,
              height: 10,
              decoration: BoxDecoration(
                color: isActive ? Colors.green : Colors.grey,
                shape: BoxShape.circle,
              ),
            ),
            const SizedBox(width: 10),
            Text(
              isActive ? 'USUÁRIO ATIVO' : 'USUÁRIO INATIVO',
              style: TextStyle(
                color: isActive ? Colors.green.shade700 : Colors.grey.shade700,
                fontWeight: FontWeight.bold,
                fontSize: 13,
                letterSpacing: 0.5,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildPositionCard() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [
            Theme.of(context).colorScheme.primaryContainer,
            Theme.of(context).colorScheme.primaryContainer.withOpacity(0.5),
          ],
        ),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Theme.of(context).colorScheme.primary.withOpacity(0.1),
            blurRadius: 15,
            offset: const Offset(0, 5),
          ),
        ],
      ),
      child: Column(
        children: [
          Icon(
            Icons.workspace_premium_rounded,
            size: 40,
            color: Theme.of(context).colorScheme.primary,
          ),
          const SizedBox(height: 12),
          Text(
            _formatEnumName(user!['position'] ?? 'Cargo não disponível'),
            style: Theme.of(context).textTheme.headlineSmall?.copyWith(
              fontWeight: FontWeight.bold,
              color: Theme.of(context).colorScheme.onPrimaryContainer,
            ),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 4),
          Text(
            _formatEnumName(user!['sector'] ?? 'Setor não disponível'),
            style: Theme.of(context).textTheme.titleMedium?.copyWith(
              color: Theme.of(context).colorScheme.onPrimaryContainer.withOpacity(0.7),
            ),
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }

  Widget _buildSectionTitle(String title, IconData icon) {
    return Row(
      children: [
        Container(
          padding: const EdgeInsets.all(8),
          decoration: BoxDecoration(
            color: Theme.of(context).colorScheme.primaryContainer,
            borderRadius: BorderRadius.circular(8),
          ),
          child: Icon(
            icon,
            size: 20,
            color: Theme.of(context).colorScheme.primary,
          ),
        ),
        const SizedBox(width: 12),
        Text(
          title,
          style: Theme.of(context).textTheme.titleLarge?.copyWith(
            fontWeight: FontWeight.bold,
          ),
        ),
      ],
    );
  }

  Widget _buildInfoCard(List<Widget> children) {
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
      child: Column(
        children: [
          for (int i = 0; i < children.length; i++) ...[
            children[i],
            if (i < children.length - 1)
              Divider(
                height: 1,
                indent: 16,
                endIndent: 16,
                color: Theme.of(context).dividerColor.withOpacity(0.3),
              ),
          ],
        ],
      ),
    );
  }

  Widget _buildInfoTile({
    required IconData icon,
    required String label,
    required String value,
    VoidCallback? onCopy,
  }) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onCopy,
        borderRadius: BorderRadius.circular(16),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Row(
            children: [
              Container(
                padding: const EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.secondaryContainer,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Icon(
                  icon,
                  size: 22,
                  color: Theme.of(context).colorScheme.secondary,
                ),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      label,
                      style: Theme.of(context).textTheme.bodySmall?.copyWith(
                        color: Theme.of(context).colorScheme.onSurfaceVariant,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      value,
                      style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ),
              if (onCopy != null)
                Icon(
                  Icons.copy_rounded,
                  size: 18,
                  color: Theme.of(context).colorScheme.onSurfaceVariant,
                ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildActionButtons() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        FilledButton.icon(
          onPressed: _showEditDialog,
          icon: const Icon(Icons.edit_rounded),
          label: const Text('Editar Usuário'),
          style: FilledButton.styleFrom(
            padding: const EdgeInsets.symmetric(vertical: 16),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          ),
        ),
        const SizedBox(height: 12),
        OutlinedButton.icon(
          onPressed: _showToggleStatusDialog,
          icon: Icon(
            user!['active'] == true ? Icons.block_rounded : Icons.check_circle_rounded,
          ),
          label: Text(
            user!['active'] == true ? 'Desativar Usuário' : 'Ativar Usuário',
          ),
          style: OutlinedButton.styleFrom(
            foregroundColor: user!['active'] == true ? Colors.orange : Colors.green,
            padding: const EdgeInsets.symmetric(vertical: 16),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            side: BorderSide(
              color: user!['active'] == true ? Colors.orange : Colors.green,
            ),
          ),
        ),
      ],
    );
  }

  String _formatEnumName(String enumName) {
    return enumName
        .replaceAll('_', ' ')
        .split(' ')
        .map((word) => word[0].toUpperCase() + word.substring(1).toLowerCase())
        .join(' ');
  }
}

// ==================== DIALOG DE EDIÇÃO ====================

class _EditUserForm extends StatefulWidget {
  final ApiService api;
  final Map<String, dynamic> user;
  final VoidCallback onUserUpdated;

  const _EditUserForm({
    required this.api,
    required this.user,
    required this.onUserUpdated,
  });

  @override
  State<_EditUserForm> createState() => __EditUserFormState();
}

class __EditUserFormState extends State<_EditUserForm> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _usernameController;
  late TextEditingController _emailController;
  final _passwordController = TextEditingController();

  late bool _isActive;
  bool _changePassword = false;
  bool _obscurePassword = true;
  Sector? _selectedSector;
  Position? _selectedPosition;

  bool _isLoading = false;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    // Pré-preenche os campos com os dados atuais
    _usernameController = TextEditingController(text: widget.user['username']);
    _emailController = TextEditingController(text: widget.user['email']);
    _isActive = widget.user['active'] == true;

    // Converte as strings dos enums para os valores corretos
    try {
      _selectedSector = Sector.values.firstWhere(
            (e) => e.name == widget.user['sector'],
      );
    } catch (e) {
      _selectedSector = null;
    }

    try {
      _selectedPosition = Position.values.firstWhere(
            (e) => e.name == widget.user['position'],
      );
    } catch (e) {
      _selectedPosition = null;
    }
  }

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
      await widget.api.updateUser(
        widget.user['userId'],
        username: _usernameController.text,
        email: _emailController.text,
        password: _changePassword ? _passwordController.text : null,
        active: _isActive,
        sector: _selectedSector!.name,
        position: _selectedPosition!.name,
      );

      if (mounted) {
        Navigator.of(context).pop();
      }
      widget.onUserUpdated();
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
            // Header
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
                      color: Colors.blue.withOpacity(0.2),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Icon(
                      Icons.edit_rounded,
                      color: Theme.of(context).colorScheme.primary,
                      size: 28,
                    ),
                  ),
                  const SizedBox(width: 16),
                  Text(
                    'Editar Usuário',
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

                      // Checkbox para alterar senha
                      Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: Theme.of(context).colorScheme.surfaceContainerHighest,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: Theme.of(context).colorScheme.outline.withOpacity(0.5),
                          ),
                        ),
                        child: Row(
                          children: [
                            Checkbox(
                              value: _changePassword,
                              onChanged: (value) {
                                setState(() {
                                  _changePassword = value ?? false;
                                  if (!_changePassword) {
                                    _passwordController.clear();
                                  }
                                });
                              },
                            ),
                            Expanded(
                              child: Text(
                                'Alterar senha',
                                style: Theme.of(context).textTheme.bodyMedium,
                              ),
                            ),
                          ],
                        ),
                      ),

                      if (_changePassword) ...[
                        const SizedBox(height: 16),
                        TextFormField(
                          controller: _passwordController,
                          decoration: _buildInputDecoration(
                            'Nova Senha',
                            Icons.lock_outlined,
                            suffix: IconButton(
                              icon: Icon(_obscurePassword
                                  ? Icons.visibility_outlined
                                  : Icons.visibility_off_outlined),
                              onPressed: () => setState(() => _obscurePassword = !_obscurePassword),
                            ),
                          ),
                          obscureText: _obscurePassword,
                          validator: (value) {
                            if (_changePassword) {
                              if (value == null || value.isEmpty) {
                                return 'Campo obrigatório';
                              }
                              if (value.length < 6) {
                                return 'Mínimo de 6 caracteres';
                              }
                            }
                            return null;
                          },
                        ),
                      ],

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
                    label: const Text('Salvar Alterações'),
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