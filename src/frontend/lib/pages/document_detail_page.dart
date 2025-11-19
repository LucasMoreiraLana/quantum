import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../services/api_service.dart';

class DocumentDetailPage extends StatefulWidget {
  final String documentId;

  const DocumentDetailPage({super.key, required this.documentId});

  @override
  State<DocumentDetailPage> createState() => _DocumentDetailPageState();
}

class _DocumentDetailPageState extends State<DocumentDetailPage> with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  Map<String, dynamic>? document;
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
    loadDocumentDetails();
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  Future<void> loadDocumentDetails() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      final data = await api.getDocumentById(widget.documentId);
      setState(() {
        document = data;
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

  IconData _getIconByType(String? type) {
    switch (type) {
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
              title: _loading || document == null
                  ? null
                  : Text(
                document!['nameDocument'] ?? 'Documento',
                style: const TextStyle(fontWeight: FontWeight.w600),
              ),
              background: Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [
                      Colors.orange,
                      Colors.orange.withOpacity(0.7),
                    ],
                  ),
                ),
                child: _loading || document == null
                    ? null
                    : Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const SizedBox(height: 40),
                      Hero(
                        tag: 'doc_${widget.documentId}',
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
                          child: Icon(
                            _getIconByType(document!['type']),
                            size: 40,
                            color: Colors.orange,
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
                onPressed: _loading ? null : loadDocumentDetails,
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
                onPressed: loadDocumentDetails,
              ),
            ],
          ),
        ),
      );
    }

    if (document == null) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(32.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                Icons.search_off_rounded,
                size: 60,
                color: Theme.of(context).colorScheme.onSurfaceVariant,
              ),
              const SizedBox(height: 16),
              Text(
                'Documento não encontrado',
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
              _buildTypeCard(),
              const SizedBox(height: 20),
              _buildSectionTitle('Informações do Documento', Icons.info_rounded),
              const SizedBox(height: 12),
              _buildInfoCard([
                _buildInfoTile(
                  icon: Icons.badge_outlined,
                  label: 'ID do Documento',
                  value: document!['documentId'] ?? 'Não disponível',
                  onCopy: () => _copyToClipboard(
                    document!['documentId'] ?? '',
                    'ID',
                  ),
                ),
                _buildInfoTile(
                  icon: Icons.title_rounded,
                  label: 'Nome do Documento',
                  value: document!['nameDocument'] ?? 'Não disponível',
                ),
                _buildInfoTile(
                  icon: Icons.subject_rounded,
                  label: 'Conteúdo',
                  value: document!['content'] ?? 'Não disponível',
                ),
                _buildInfoTile(
                  icon: Icons.schedule_rounded,
                  label: 'Tempo de Retenção',
                  value: '${document!['tempoDeRetencao'] ?? 0} anos',
                ),
              ]),
              const SizedBox(height: 24),
              _buildSectionTitle('Informações Adicionais', Icons.more_horiz_rounded),
              const SizedBox(height: 12),
              _buildInfoCard([
                _buildInfoTile(
                  icon: Icons.category_outlined,
                  label: 'Tipo',
                  value: _formatEnumName(document!['type'] ?? 'Não disponível'),
                ),
                _buildInfoTile(
                  icon: Icons.source_rounded,
                  label: 'Origem',
                  value: _formatEnumName(document!['origin'] ?? 'Não disponível'),
                ),
                _buildInfoTile(
                  icon: Icons.business_rounded,
                  label: 'Setor',
                  value: _formatEnumName(document!['sector'] ?? 'Não disponível'),
                ),
              ]),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildStatusBadge() {
    final bool isActive = document!['active'] == true;
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
              isActive ? 'DOCUMENTO ATIVO' : 'DOCUMENTO INATIVO',
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

  Widget _buildTypeCard() {
    final type = document!['type'] ?? 'REGISTRO';
    final origin = document!['origin'] ?? 'INTERNO';

    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [
            Colors.orange.withOpacity(0.3),
            Colors.orange.withOpacity(0.1),
          ],
        ),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.orange.withOpacity(0.1),
            blurRadius: 15,
            offset: const Offset(0, 5),
          ),
        ],
      ),
      child: Column(
        children: [
          Icon(
            _getIconByType(type),
            size: 40,
            color: Colors.orange.shade700,
          ),
          const SizedBox(height: 12),
          Text(
            _formatEnumName(type),
            style: Theme.of(context).textTheme.headlineSmall?.copyWith(
              fontWeight: FontWeight.bold,
              color: Colors.orange.shade700,
            ),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                origin == 'INTERNO' ? Icons.business : Icons.public,
                size: 16,
                color: Colors.orange.shade600,
              ),
              const SizedBox(width: 8),
              Text(
                _formatEnumName(origin),
                style: Theme.of(context).textTheme.titleMedium?.copyWith(
                  color: Colors.orange.shade600,
                ),
                textAlign: TextAlign.center,
              ),
            ],
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
            color: Colors.orange.withOpacity(0.1),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Icon(
            icon,
            size: 20,
            color: Colors.orange.shade700,
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
                  color: Colors.orange.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Icon(
                  icon,
                  size: 22,
                  color: Colors.orange.shade700,
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

  String _formatEnumName(String enumName) {
    return enumName
        .replaceAll('_', ' ')
        .split(' ')
        .map((word) => word[0].toUpperCase() + word.substring(1).toLowerCase())
        .join(' ');
  }
}