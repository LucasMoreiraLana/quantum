import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';
import 'document_detail_page.dart';



// Definições dos enums (adicionadas aqui para corrigir os erros; mova para models/enums.dart se preferir)
enum DocumentType {
  REGISTRO,
  PROCEDIMENTO,
  INSTRUCAO_TECNICA,
  FORMULARIO,
  REGULAMENTO,
  SISTEMA_INFORMATIZADO;


  String get displayName {
    return name.replaceAll('_', ' ').toLowerCase().split(' ').map((word) => word[0].toUpperCase() + word.substring(1)).join(' ');
  }
}

enum DocumentOrigin {
  INTERNO,
  EXTERNO;

  String get displayName {
    return name.replaceAll('_', ' ').toLowerCase().split(' ').map((word) => word[0].toUpperCase() + word.substring(1)).join(' ');
  }
}

enum Sector {
  ADMINISTRATIVO,
  FINANCEIRO,
  RECURSOS_HUMANOS,
  OPERACIONAL,
  TI,
  VENDAS,
  MARKETING;

  String get displayName {
    return name.replaceAll('_', ' ').toLowerCase().split(' ').map((word) => word[0].toUpperCase() + word.substring(1)).join(' ');
  }
}

class DocumentsPage extends StatefulWidget {
  const DocumentsPage({super.key});

  @override
  State<DocumentsPage> createState() => _DocumentsPageState();
}

class _DocumentsPageState extends State<DocumentsPage> with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  final AuthService authService = AuthService();
  List<dynamic> documents = [];
  List<dynamic> filteredDocuments = [];
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
    _loadData();
  }

  Future<void> _loadData() async {
    _hasManagementPermission = await authService.hasManagementPermission();
    setState(() {});
    await loadDocuments();
  }

  @override
  void dispose() {
    _searchController.dispose();
    _fabAnimationController.dispose();
    super.dispose();
  }

  Future<void> loadDocuments() async {
    if (mounted && !_loading) {
      setState(() {
        _loading = true;
        _error = null;
      });
    }

    try {
      final data = await api.getDocuments();
      if (mounted) {
        setState(() {
          documents = data;
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
          documents = [];
          filteredDocuments = [];
        });
      }
    }
  }

  void _applyFilters() {
    List<dynamic> result = documents;

    // Filtro por status
    if (_statusFilter == 'active') {
      result = result.where((doc) => doc['active'] == true).toList();
    } else if (_statusFilter == 'inactive') {
      result = result.where((doc) => doc['active'] == false).toList();
    }

    // Filtro por busca
    if (_searchController.text.isNotEmpty) {
      final searchLower = _searchController.text.toLowerCase();
      result = result.where((doc) {
        final name = (doc['nameDocument'] ?? '').toString().toLowerCase();
        final content = (doc['content'] ?? '').toString().toLowerCase();
        final type = (doc['type'] ?? '').toString().toLowerCase();

        return name.contains(searchLower) ||
            content.contains(searchLower) ||
            type.contains(searchLower);
      }).toList();
    }

    setState(() {
      filteredDocuments = result;
    });
  }

  void _filterDocuments(String query) {
    _applyFilters();
  }

  void _changeStatusFilter(String filter) {
    setState(() {
      _statusFilter = filter;
      _applyFilters();
    });
  }

  void _showCreateDocumentDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext dialogContext) {
        return _CreateDocumentForm(
          api: api,
          onDocumentCreated: () {
            loadDocuments();
            if (mounted) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: const Row(
                    children: [
                      Icon(Icons.check_circle, color: Colors.white),
                      SizedBox(width: 12),
                      Text('Documento criado com sucesso!'),
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
          SliverAppBar(
            floating: true,
            pinned: true,
            elevation: 0,
            backgroundColor: Theme.of(context).colorScheme.surface,
            expandedHeight: 120,
            flexibleSpace: FlexibleSpaceBar(
              title: const Text(
                'Gestão de Documentos',
                style: TextStyle(fontWeight: FontWeight.w600),
              ),
              background: Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [
                      Colors.orange.withOpacity(0.1),
                      Theme.of(context).colorScheme.surface,
                    ],
                  ),
                ),
              ),
            ),
            actions: [
              IconButton(
                icon: const Icon(Icons.refresh_rounded),
                onPressed: _loading ? null : loadDocuments,
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

          // Filtros de status
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              child: _buildStatusFilters(),
            ),
          ),

          // Estatísticas
          if (!_loading && _error == null && documents.isNotEmpty)
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
      floatingActionButton: _hasManagementPermission
          ? ScaleTransition(
        scale: CurvedAnimation(
          parent: _fabAnimationController,
          curve: Curves.easeOutBack,
        ),
        child: FloatingActionButton.extended(
          onPressed: _showCreateDocumentDialog,
          icon: const Icon(Icons.add_rounded),
          label: const Text('Novo Documento'),
          elevation: 4,
          backgroundColor: Colors.orange,
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
        onChanged: _filterDocuments,
        decoration: InputDecoration(
          hintText: 'Buscar documentos...',
          prefixIcon: const Icon(Icons.search_rounded),
          suffixIcon: _searchController.text.isNotEmpty
              ? IconButton(
            icon: const Icon(Icons.clear_rounded),
            onPressed: () {
              _searchController.clear();
              _filterDocuments('');
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
    final activeDocs = documents.where((d) => d['active'] == true).length;
    final inactiveDocs = documents.length - activeDocs;

    return Row(
      children: [
        Expanded(
          child: _buildStatCard(
            icon: Icons.description_rounded,
            label: 'Total',
            value: documents.length.toString(),
            color: Colors.orange,
            onTap: () => _changeStatusFilter('all'),
            isSelected: _statusFilter == 'all',
          ),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: _buildStatCard(
            icon: Icons.check_circle_rounded,
            label: 'Ativos',
            value: activeDocs.toString(),
            color: Colors.green,
            onTap: () => _changeStatusFilter('active'),
            isSelected: _statusFilter == 'active',
          ),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: _buildStatCard(
            icon: Icons.archive_rounded,
            label: 'Inativos',
            value: inactiveDocs.toString(),
            color: Colors.grey,
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
            icon: Icons.description_rounded,
            isSelected: _statusFilter == 'all',
            onTap: () => _changeStatusFilter('all'),
            color: Colors.orange,
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
            icon: Icons.archive_rounded,
            isSelected: _statusFilter == 'inactive',
            onTap: () => _changeStatusFilter('inactive'),
            color: Colors.grey,
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
            boxShadow: isSelected
                ? [
              BoxShadow(
                color: color.withOpacity(0.3),
                blurRadius: 8,
                offset: const Offset(0, 2),
              ),
            ]
                : [],
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
                'Erro ao carregar documentos',
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
                onPressed: loadDocuments,
              ),
            ],
          ),
        ),
      );
    }

    if (filteredDocuments.isEmpty) {
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
                  color: Colors.orange.withOpacity(0.1),
                  shape: BoxShape.circle,
                ),
                child: Icon(
                  isSearching ? Icons.search_off_rounded : Icons.description_outlined,
                  color: Colors.orange,
                  size: 48,
                ),
              ),
              const SizedBox(height: 24),
              Text(
                isSearching ? 'Nenhum resultado encontrado' : 'Nenhum documento cadastrado',
                style: Theme.of(context).textTheme.titleLarge?.copyWith(
                  fontWeight: FontWeight.w600,
                ),
              ),
              const SizedBox(height: 12),
              Text(
                isSearching
                    ? 'Tente ajustar os termos da busca'
                    : 'Ainda não há documentos cadastrados',
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
      onRefresh: loadDocuments,
      child: ListView.builder(
        shrinkWrap: true,
        physics: const NeverScrollableScrollPhysics(),
        padding: const EdgeInsets.fromLTRB(16, 8, 16, 100),
        itemCount: filteredDocuments.length,
        itemBuilder: (context, index) {
          final doc = filteredDocuments[index];
          return _buildDocumentCard(doc, index);
        },
      ),
    );
  }

  Widget _buildDocumentCard(Map<String, dynamic> doc, int index) {
    final name = doc['nameDocument'] ?? 'Documento sem nome';
    final content = doc['content'] ?? '';
    final type = doc['type'] ?? 'REGISTRO';
    final isActive = doc['active'] == true;
    final origin = doc['origin'] ?? 'INTERNO';

    // Ícone baseado no tipo de documento
    IconData getIconByType(String docType) {
      switch (docType) {
        case 'REGISTRO':
          return Icons.assignment_rounded;
        case 'PROCEDIMENTO':
          return Icons.list_alt_rounded;
        case 'INSTRUCAO_TECNICA':
          return Icons.engineering_rounded;
        case 'FORMULARIO':
          return Icons.description_rounded;
        case 'REGULAMENTO':
          return Icons.gavel_rounded;
        case 'SISTEMA_INFORMATIZADO':
          return Icons.computer_rounded;
        default:
          return Icons.description_rounded;
      }
    }

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
            onTap: () async {
              final docId = doc['documentId'];
              if (docId == null || docId.toString().isEmpty) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: const Row(
                      children: [
                        Icon(Icons.error_outline, color: Colors.white),
                        SizedBox(width: 12),
                        Expanded(child: Text('ID do documento não encontrado!')),
                      ],
                    ),
                    backgroundColor: Colors.red.shade600,
                    behavior: SnackBarBehavior.floating,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                  ),
                );
                return;
              }

              // Navega e recarrega ao voltar
              await Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => DocumentDetailPage(documentId: docId.toString()),
                ),
              );
              loadDocuments();
            },
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Row(
                children: [
                  // Ícone do documento com status
                  Stack(
                    children: [
                      Container(
                        width: 56,
                        height: 56,
                        decoration: BoxDecoration(
                          gradient: LinearGradient(
                            colors: [
                              Colors.orange.withOpacity(0.3),
                              Colors.orange.withOpacity(0.1),
                            ],
                          ),
                          borderRadius: BorderRadius.circular(16),
                        ),
                        child: Icon(
                          getIconByType(type),
                          color: Colors.orange.shade700,
                          size: 28,
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
                  // Informações do documento
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          name,
                          style: const TextStyle(
                            fontWeight: FontWeight.w600,
                            fontSize: 16,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 4),
                        Row(
                          children: [
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                              decoration: BoxDecoration(
                                color: Colors.orange.withOpacity(0.1),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Text(
                                type.replaceAll('_', ' '),
                                style: TextStyle(
                                  color: Colors.orange.shade700,
                                  fontSize: 11,
                                  fontWeight: FontWeight.w500,
                                ),
                              ),
                            ),
                            const SizedBox(width: 8),
                            Icon(
                              origin == 'INTERNO' ? Icons.business : Icons.public,
                              size: 14,
                              color: Theme.of(context).colorScheme.onSurfaceVariant,
                            ),
                            const SizedBox(width: 4),
                            Text(
                              origin,
                              style: TextStyle(
                                fontSize: 12,
                                color: Theme.of(context).colorScheme.onSurfaceVariant,
                              ),
                            ),
                          ],
                        ),
                        if (content.isNotEmpty) ...[
                          const SizedBox(height: 4),
                          Text(
                            content,
                            style: TextStyle(
                              color: Theme.of(context).colorScheme.onSurfaceVariant,
                              fontSize: 12,
                            ),
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
                          ),
                        ],
                      ],
                    ),
                  ),
                  // Ícone de navegação
                  Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: Colors.orange.withOpacity(0.1),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Icon(
                      Icons.arrow_forward_ios_rounded,
                      size: 16,
                      color: Colors.orange.shade700,
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

// ==================== FORMULÁRIO DE CRIAÇÃO DE DOCUMENTO ====================

class _CreateDocumentForm extends StatefulWidget {
  final ApiService api;
  final VoidCallback onDocumentCreated;

  const _CreateDocumentForm({
    required this.api,
    required this.onDocumentCreated,
  });

  @override
  State<_CreateDocumentForm> createState() => __CreateDocumentFormState();
}

class __CreateDocumentFormState extends State<_CreateDocumentForm> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _contentController = TextEditingController();
  final _retentionController = TextEditingController();

  DocumentType? _selectedType;
  DocumentOrigin? _selectedOrigin;
  Sector? _selectedSector;

  bool _isLoading = false;
  String? _errorMessage;

  final AuthService _authService = AuthService();
  String? _currentUserId;

  @override
  void initState() {
    super.initState();
    _loadCurrentUser();
  }

  Future<void> _loadCurrentUser() async {
    final user = await _authService.getCurrentUser();
    setState(() {
      _currentUserId = user['userId'];
    });
  }

  @override
  void dispose() {
    _nameController.dispose();
    _contentController.dispose();
    _retentionController.dispose();
    super.dispose();
  }

  Future<void> _submitForm() async {
    if (!(_formKey.currentState?.validate() ?? false)) {
      return;
    }

    if (_currentUserId == null) {
      setState(() {
        _errorMessage = 'Erro ao identificar usuário. Faça login novamente.';
      });
      return;
    }

    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      await widget.api.createDocument(
        createdBy: _currentUserId!,
        nameDocument: _nameController.text,
        content: _contentController.text,
        tempoDeRetencao: int.parse(_retentionController.text),
        type: _selectedType!.name,
        origin: _selectedOrigin!.name,
        sector: _selectedSector!.name,
      );

      if (mounted) {
        Navigator.of(context).pop();
      }
      widget.onDocumentCreated();
    } catch (e) {
      setState(() {
        _isLoading = false;
        _errorMessage = e.toString().replaceFirst('Exception: ', '');
      });
    }
  }

  InputDecoration _buildInputDecoration(String label, IconData icon, {Widget? suffix, String? hint}) {
    return InputDecoration(
      labelText: label,
      hintText: hint,
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
        borderSide: BorderSide(color: Colors.orange, width: 2),
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
        constraints: const BoxConstraints(maxWidth: 600),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            // Header do Dialog
            Container(
              padding: const EdgeInsets.all(24),
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [
                    Colors.orange.withOpacity(0.3),
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
                      color: Colors.orange.withOpacity(0.2),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Icon(
                      Icons.description_rounded,
                      color: Colors.orange.shade700,
                      size: 28,
                    ),
                  ),
                  const SizedBox(width: 16),
                  Text(
                    'Novo Documento',
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
                        controller: _nameController,
                        decoration: _buildInputDecoration(
                          'Nome do Documento',
                          Icons.title_rounded,
                          hint: 'Ex: Manual de Procedimentos',
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Campo obrigatório';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _contentController,
                        decoration: _buildInputDecoration(
                          'Descrição/Conteúdo',
                          Icons.subject_rounded,
                          hint: 'Descreva o conteúdo do documento',
                        ),
                        maxLines: 3,
                        maxLength: 5000,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Campo obrigatório';
                          }
                          if (value.length > 5000) {
                            return 'Máximo de 5000 caracteres';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        controller: _retentionController,
                        decoration: _buildInputDecoration(
                          'Tempo de Retenção (anos)',
                          Icons.schedule_rounded,
                          hint: 'Ex: 5',
                        ),
                        keyboardType: TextInputType.number,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Campo obrigatório';
                          }
                          final number = int.tryParse(value);
                          if (number == null || number <= 0) {
                            return 'Deve ser um número maior que zero';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<DocumentType>(
                        value: _selectedType,
                        decoration: _buildInputDecoration('Tipo de Documento', Icons.category_outlined),
                        items: DocumentType.values.map((type) {
                          return DropdownMenuItem(
                            value: type,
                            child: Text(type.displayName),
                          );
                        }).toList(),
                        onChanged: (value) => setState(() => _selectedType = value),
                        validator: (value) => value == null ? 'Campo obrigatório' : null,
                      ),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<DocumentOrigin>(
                        value: _selectedOrigin,
                        decoration: _buildInputDecoration('Origem', Icons.source_rounded),
                        items: DocumentOrigin.values.map((origin) {
                          return DropdownMenuItem(
                            value: origin,
                            child: Row(
                              children: [
                                Icon(
                                  origin == DocumentOrigin.INTERNO ? Icons.business : Icons.public,
                                  size: 20,
                                  color: Colors.orange,
                                ),
                                const SizedBox(width: 8),
                                Text(origin.displayName),
                              ],
                            ),
                          );
                        }).toList(),
                        onChanged: (value) => setState(() => _selectedOrigin = value),
                        validator: (value) => value == null ? 'Campo obrigatório' : null,
                      ),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<Sector>(
                        value: _selectedSector,
                        decoration: _buildInputDecoration('Setor', Icons.business_rounded),
                        items: Sector.values.map((sector) {
                          return DropdownMenuItem(
                            value: sector,
                            child: Text(sector.displayName),
                          );
                        }).toList(),
                        onChanged: (value) => setState(() => _selectedSector = value),
                        validator: (value) => value == null ? 'Campo obrigatório' : null,
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
                    label: const Text('Criar Documento'),
                    style: FilledButton.styleFrom(
                      backgroundColor: Colors.orange,
                    ),
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