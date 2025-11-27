import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';
import '../../models/enums.dart'; // Ajuste o caminho se necessário

class DocumentDetailPage extends StatefulWidget {
  final String documentId;

  const DocumentDetailPage({super.key, required this.documentId});

  @override
  State<DocumentDetailPage> createState() => _DocumentDetailPageState();
}

class _DocumentDetailPageState extends State<DocumentDetailPage>
    with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  final AuthService authService = AuthService();

  Map<String, dynamic>? document;
  bool _loading = true;
  String? _error;
  late AnimationController _animationController;
  bool _hasManagementPermission = false;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 600),
    );
    _loadData();
  }

  Future<void> _loadData() async {
    _hasManagementPermission = await authService.hasManagementPermission();
    await loadDocumentDetails();
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
      var data = await api.getDocumentById(widget.documentId);

      // ←←← CORREÇÃO TEMPORÁRIA (enquanto o backend não arrumar o GET /{id})
      if (data['tempoDeRetencao'] == null) {
        print('Campo tempoDeRetencao veio null → buscando da lista geral...');
        final allDocs = await api.getDocuments();
        final found = allDocs.firstWhere((d) => d['documentId'] == widget.documentId, orElse: () => null);
        if (found != null) {
          data = found;  // substitui pelo que veio certo da lista
        }
      }

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

  // Método helper para parsing seguro do tempo de retenção
  int _parseRetention(dynamic value) {
    if (value == null) return 0;
    if (value is int) return value;
    if (value is double) return value.toInt();
    if (value is String) return int.tryParse(value) ?? 0;
    return 0;
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
      case 'REGISTRO': return Icons.assignment_rounded;
      case 'PROCEDIMENTO': return Icons.list_alt_rounded;
      case 'INSTRUCAO_TECNICA': return Icons.engineering_rounded;
      case 'FORMULARIO': return Icons.description_rounded;
      case 'REGULAMENTO': return Icons.gavel_rounded;
      case 'SISTEMA_INFORMATIZADO': return Icons.computer_rounded;
      default: return Icons.description_rounded;
    }
  }

  void _showEditDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) => _EditDocumentForm(
        api: api,
        document: document!,
        onDocumentUpdated: () async {
          await loadDocumentDetails();
        },
      ),
    );
  }

  void _showToggleStatusDialog() {
    final bool isActive = document!['active'] == true;
    final String action = isActive ? 'desativar' : 'ativar';
    final String actionCap = isActive ? 'Desativar' : 'Ativar';

    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
        title: Row(
          children: [
            Icon(isActive ? Icons.archive_rounded : Icons.unarchive_rounded,
                color: isActive ? Colors.orange : Colors.green),
            const SizedBox(width: 12),
            Text('$actionCap Documento?'),
          ],
        ),
        content: Text('Tem certeza que deseja $action este documento?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancelar')),
          FilledButton(
            onPressed: () async {
              Navigator.pop(context);
              await _toggleDocumentStatus(!isActive);
            },
            style: FilledButton.styleFrom(backgroundColor: isActive ? Colors.orange : Colors.green),
            child: Text(actionCap),
          ),
        ],
      ),
    );
  }

  // Substitua o método _toggleDocumentStatus no document_detail_page.dart por este:

  Future<void> _toggleDocumentStatus(bool newStatus) async {
    print('\n╔════════════════════════════════════════════════════════╗');
    print('║      DEBUG: ALTERANDO STATUS DO DOCUMENTO             ║');
    print('╚════════════════════════════════════════════════════════╝');
    print('📄 Documento ID: ${widget.documentId}');
    print('📄 Nome: ${document!['nameDocument']}');
    print('🔄 Status ANTES: ${document!['active']}');
    print('🔄 Novo status: $newStatus');
    print('');

    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) => const Center(child: CircularProgressIndicator()),
    );

    try {
      // Chama a API para alterar o status
      await api.toggleDocumentStatus(widget.documentId, newStatus);
      print('✅ API toggleDocumentStatus chamada com sucesso!');

      if (mounted) Navigator.pop(context);

      // Recarrega o documento para pegar o status atualizado
      print('🔄 Recarregando documento...');
      await loadDocumentDetails();

      // Verifica se o status foi realmente alterado
      print('');
      print('📊 VERIFICAÇÃO PÓS-ALTERAÇÃO:');
      print('   Status DEPOIS do reload: ${document!['active']}');
      print('   Status esperado: $newStatus');

      if (document!['active'] == newStatus) {
        print('   ✅ Status CONFIRMADO como alterado!');
      } else {
        print('   ⚠️  ATENÇÃO: Status NÃO foi alterado no backend!');
        print('   Possível causa: O backend não está persistindo a alteração');
      }
      print('═══════════════════════════════════════════════════════════\n');

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Row(
              children: [
                const Icon(Icons.check_circle, color: Colors.white),
                const SizedBox(width: 12),
                Text(newStatus ? 'Documento ativado!' : 'Documento desativado!'),
              ],
            ),
            backgroundColor: newStatus ? Colors.green.shade600 : Colors.orange.shade600,
            behavior: SnackBarBehavior.floating,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
          ),
        );
      }
    } catch (e) {
      print('❌ ERRO ao alterar status: $e');
      print('═══════════════════════════════════════════════════════════\n');

      if (mounted) Navigator.pop(context);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Erro: $e'),
            backgroundColor: Colors.red.shade600,
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
              title: _loading || document == null
                  ? null
                  : Text(document!['nameDocument'] ?? 'Documento',
                  style: const TextStyle(fontWeight: FontWeight.w600)),
              background: Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [Colors.orange, Colors.orange.withOpacity(0.7)],
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
                        child: CircleAvatar(
                          radius: 40,
                          backgroundColor: Colors.white,
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
            ],
          ),
          SliverToBoxAdapter(child: _buildBody()),
        ],
      ),
    );
  }

  Widget _buildBody() {
    if (_loading) return const Center(child: Padding(padding: EdgeInsets.all(64), child: CircularProgressIndicator()));
    if (_error != null) return _buildErrorWidget();
    if (document == null) return _buildNotFoundWidget();

    return FadeTransition(
      opacity: _animationController,
      child: SlideTransition(
        position: Tween<Offset>(begin: const Offset(0, 0.1), end: Offset.zero)
            .animate(CurvedAnimation(parent: _animationController, curve: Curves.easeOutCubic)),
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
                    label: 'ID',
                    value: document!['documentId'] ?? '-',
                    onCopy: () => _copyToClipboard(document!['documentId'] ?? '', 'ID')
                ),
                _buildInfoTile(
                    icon: Icons.title_rounded,
                    label: 'Nome',
                    value: document!['nameDocument'] ?? '-'
                ),
                _buildInfoTile(
                    icon: Icons.subject_rounded,
                    label: 'Conteúdo',
                    value: document!['content'] ?? '-'
                ),
                _buildInfoTile(
                    icon: Icons.schedule_rounded,
                    label: 'Retenção',
                    value: '${_parseRetention(document!['tempoDeRetencao'])} anos'
                ),
              ]),
              const SizedBox(height: 24),
              _buildSectionTitle('Detalhes', Icons.more_horiz_rounded),
              const SizedBox(height: 12),
              _buildInfoCard([
                _buildInfoTile(icon: Icons.category_outlined, label: 'Tipo', value: _formatEnumName(document!['type'] ?? '-')),
                _buildInfoTile(icon: Icons.source_rounded, label: 'Origem', value: _formatEnumName(document!['origin'] ?? '-')),
                _buildInfoTile(icon: Icons.business_rounded, label: 'Setor', value: _formatEnumName(document!['sector'] ?? '-')),
              ]),
              const SizedBox(height: 32),
              if (_hasManagementPermission) _buildActionButtons(),
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
          border: Border.all(color: isActive ? Colors.green : Colors.grey, width: 2),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(width: 10, height: 10, decoration: BoxDecoration(color: isActive ? Colors.green : Colors.grey, shape: BoxShape.circle)),
            const SizedBox(width: 10),
            Text(isActive ? 'DOCUMENTO ATIVO' : 'DOCUMENTO INATIVO',
                style: TextStyle(color: isActive ? Colors.green.shade700 : Colors.grey.shade700, fontWeight: FontWeight.bold, fontSize: 13)),
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
        gradient: LinearGradient(colors: [Colors.orange.withOpacity(0.3), Colors.orange.withOpacity(0.1)]),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [BoxShadow(color: Colors.orange.withOpacity(0.1), blurRadius: 15, offset: const Offset(0, 5))],
      ),
      child: Column(
        children: [
          Icon(_getIconByType(type), size: 40, color: Colors.orange.shade700),
          const SizedBox(height: 12),
          Text(_formatEnumName(type), style: Theme.of(context).textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.bold, color: Colors.orange.shade700)),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(origin == 'INTERNO' ? Icons.business : Icons.public, size: 16, color: Colors.orange.shade600),
              const SizedBox(width: 8),
              Text(_formatEnumName(origin), style: TextStyle(color: Colors.orange.shade600)),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildSectionTitle(String title, IconData icon) => Row(
    children: [
      Container(padding: const EdgeInsets.all(8), decoration: BoxDecoration(color: Colors.orange.withOpacity(0.1), borderRadius: BorderRadius.circular(8)), child: Icon(icon, size: 20, color: Colors.orange.shade700)),
      const SizedBox(width: 12),
      Text(title, style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold)),
    ],
  );

  Widget _buildInfoCard(List<Widget> children) => Container(
    decoration: BoxDecoration(color: Theme.of(context).colorScheme.surface, borderRadius: BorderRadius.circular(16), boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.05), blurRadius: 10, offset: const Offset(0, 2))]),
    child: Column(children: [
      for (int i = 0; i < children.length; i++) ...[
        children[i],
        if (i < children.length - 1) Divider(height: 1, indent: 16, endIndent: 16, color: Theme.of(context).dividerColor.withOpacity(0.3)),
      ]
    ]),
  );

  Widget _buildInfoTile({required IconData icon, required String label, required String value, VoidCallback? onCopy}) => Material(
    color: Colors.transparent,
    child: InkWell(
      onTap: onCopy,
      borderRadius: BorderRadius.circular(16),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            Container(padding: const EdgeInsets.all(10), decoration: BoxDecoration(color: Colors.orange.withOpacity(0.1), borderRadius: BorderRadius.circular(10)), child: Icon(icon, size: 22, color: Colors.orange.shade700)),
            const SizedBox(width: 16),
            Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Text(label, style: TextStyle(color: Theme.of(context).colorScheme.onSurfaceVariant, fontWeight: FontWeight.w500)), const SizedBox(height: 4), Text(value, style: const TextStyle(fontWeight: FontWeight.w600))])),
            if (onCopy != null) Icon(Icons.copy_rounded, size: 18, color: Theme.of(context).colorScheme.onSurfaceVariant),
          ],
        ),
      ),
    ),
  );

  Widget _buildActionButtons() => Column(
    crossAxisAlignment: CrossAxisAlignment.stretch,
    children: [
      FilledButton.icon(
        onPressed: _showEditDialog,
        icon: const Icon(Icons.edit_rounded),
        label: const Text('Editar Documento'),
        style: FilledButton.styleFrom(padding: const EdgeInsets.symmetric(vertical: 16), shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12))),
      ),
      const SizedBox(height: 12),
      OutlinedButton.icon(
        onPressed: _showToggleStatusDialog,
        icon: Icon(document!['active'] ? Icons.archive_rounded : Icons.unarchive_rounded),
        label: Text(document!['active'] ? 'Desativar Documento' : 'Ativar Documento'),
        style: OutlinedButton.styleFrom(
          foregroundColor: document!['active'] ? Colors.orange : Colors.green,
          side: BorderSide(color: document!['active'] ? Colors.orange : Colors.green),
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        ),
      ),
    ],
  );

  String _formatEnumName(String name) => name.replaceAll('_', ' ').split(' ').map((w) => w[0] + w.substring(1).toLowerCase()).join(' ');

  Widget _buildErrorWidget() => Center(
    child: Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(Icons.error_outline, size: 64, color: Colors.red.shade300),
        const SizedBox(height: 16),
        Text('Erro ao carregar documento', style: Theme.of(context).textTheme.titleLarge),
        const SizedBox(height: 8),
        Text(_error ?? 'Erro desconhecido', style: Theme.of(context).textTheme.bodyMedium),
        const SizedBox(height: 24),
        FilledButton.icon(
          onPressed: loadDocumentDetails,
          icon: const Icon(Icons.refresh),
          label: const Text('Tentar novamente'),
        ),
      ],
    ),
  );

  Widget _buildNotFoundWidget() => Center(
    child: Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(Icons.search_off, size: 64, color: Colors.grey.shade300),
        const SizedBox(height: 16),
        Text('Documento não encontrado', style: Theme.of(context).textTheme.titleLarge),
      ],
    ),
  );
}

// ====================== FORMULÁRIO DE EDIÇÃO CORRIGIDO ======================
class _EditDocumentForm extends StatefulWidget {
  final ApiService api;
  final Map<String, dynamic> document;
  final Future<void> Function() onDocumentUpdated;

  const _EditDocumentForm({
    required this.api,
    required this.document,
    required this.onDocumentUpdated,
  });

  @override
  State<_EditDocumentForm> createState() => _EditDocumentFormState();
}

class _EditDocumentFormState extends State<_EditDocumentForm> {
  final _formKey = GlobalKey<FormState>();

  late final TextEditingController _nameCtrl;
  late final TextEditingController _contentCtrl;
  late final TextEditingController _retentionCtrl;

  late DocumentType _type;
  late DocumentOrigin _origin;
  late Sector _sector;
  late bool _active;

  bool _saving = false;

  @override
  void initState() {
    super.initState();

    _nameCtrl = TextEditingController(text: widget.document['nameDocument'] ?? '');
    _contentCtrl = TextEditingController(text: widget.document['content'] ?? '');

    // Parsing seguro do tempo de retenção
    final int retention = _parseRetention(widget.document['tempoDeRetencao']);
    _retentionCtrl = TextEditingController(text: retention.toString());

    // Enums com fallback seguro
    _type = DocumentType.values.firstWhere(
          (e) => e.name == widget.document['type'],
      orElse: () => DocumentType.REGISTRO,
    );
    _origin = DocumentOrigin.values.firstWhere(
          (e) => e.name == widget.document['origin'],
      orElse: () => DocumentOrigin.INTERNO,
    );
    _sector = Sector.values.firstWhere(
          (e) => e.name == widget.document['sector'],
      orElse: () => Sector.ADMINISTRATIVO,
    );
    _active = widget.document['active'] == true;
  }

  int _parseRetention(dynamic value) {
    if (value == null) return 0;
    if (value is int) return value;
    if (value is double) return value.toInt();
    if (value is String) return int.tryParse(value) ?? 0;
    return 0;
  }

  @override
  void dispose() {
    _nameCtrl.dispose();
    _contentCtrl.dispose();
    _retentionCtrl.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _saving = true);

    try {
      // Parse seguro do campo de retenção
      final int retention = int.tryParse(_retentionCtrl.text.trim()) ?? 0;

      // Debug: verificar o valor antes de enviar
      print('Salvando documento com retenção: $retention');

      await widget.api.updateDocument(
        documentId: widget.document['documentId'],
        nameDocument: _nameCtrl.text.trim(),
        content: _contentCtrl.text.trim(),
        tempoDeRetencao: retention,
        active: _active,
        type: _type.name,
        origin: _origin.name,
        sector: _sector.name,
      );

      if (mounted) {
        Navigator.of(context).pop();

        // Aguardar o reload completar
        await widget.onDocumentUpdated();

        // Mostrar sucesso apenas após reload
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: const Row(
                children: [
                  Icon(Icons.check_circle, color: Colors.white),
                  SizedBox(width: 12),
                  Text('Documento atualizado com sucesso!'),
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
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Erro ao salvar: $e'),
            backgroundColor: Colors.red.shade600,
          ),
        );
      }
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  InputDecoration _inputDecoration(String label, IconData icon) {
    return InputDecoration(
      labelText: label,
      prefixIcon: Icon(icon, color: Colors.orange.shade700),
      border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: BorderSide(color: Colors.orange.withOpacity(0.3)),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: const BorderSide(color: Colors.orange, width: 2),
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
            // Header
            Container(
              padding: const EdgeInsets.all(24),
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [Colors.orange.withOpacity(0.3), Theme.of(context).colorScheme.surface],
                ),
                borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
              ),
              child: Row(
                children: [
                  Container(
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: Colors.orange.withOpacity(0.2),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: const Icon(Icons.edit_document, color: Colors.orange, size: 28),
                  ),
                  const SizedBox(width: 16),
                  Text(
                    'Editar Documento',
                    style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
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
                    children: [
                      TextFormField(
                        controller: _nameCtrl,
                        decoration: _inputDecoration('Nome do Documento', Icons.title_rounded),
                        validator: (v) => v?.trim().isEmpty ?? true ? 'Obrigatório' : null,
                      ),
                      const SizedBox(height: 16),

                      TextFormField(
                        controller: _contentCtrl,
                        decoration: _inputDecoration('Conteúdo', Icons.subject_rounded),
                        maxLines: 5,
                        validator: (v) => v?.trim().isEmpty ?? true ? 'Obrigatório' : null,
                      ),
                      const SizedBox(height: 16),

                      TextFormField(
                        controller: _retentionCtrl,
                        decoration: _inputDecoration('Tempo de Retenção (anos)', Icons.schedule_rounded),
                        keyboardType: TextInputType.number,
                        inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                        validator: (v) {
                          if (v == null || v.trim().isEmpty) return 'Obrigatório';
                          final n = int.tryParse(v);
                          if (n == null || n < 0) return 'Digite um número válido';
                          return null;
                        },
                      ),
                      const SizedBox(height: 16),

                      DropdownButtonFormField<DocumentType>(
                        value: _type,
                        decoration: _inputDecoration('Tipo de Documento', Icons.category_outlined),
                        items: DocumentType.values
                            .map((e) => DropdownMenuItem(value: e, child: Text(e.displayName)))
                            .toList(),
                        onChanged: (v) => setState(() => _type = v!),
                        validator: (v) => v == null ? 'Selecione um tipo' : null,
                      ),
                      const SizedBox(height: 16),

                      DropdownButtonFormField<DocumentOrigin>(
                        value: _origin,
                        decoration: _inputDecoration('Origem', Icons.source_rounded),
                        items: DocumentOrigin.values
                            .map((e) => DropdownMenuItem(value: e, child: Text(e.displayName)))
                            .toList(),
                        onChanged: (v) => setState(() => _origin = v!),
                      ),
                      const SizedBox(height: 16),

                      DropdownButtonFormField<Sector>(
                        value: _sector,
                        decoration: _inputDecoration('Setor', Icons.business_rounded),
                        items: Sector.values
                            .map((e) => DropdownMenuItem(value: e, child: Text(e.displayName)))
                            .toList(),
                        onChanged: (v) => setState(() => _sector = v!),
                        validator: (v) => v == null ? 'Selecione um setor' : null,
                      ),
                      const SizedBox(height: 24),

                      SwitchListTile(
                        title: const Text('Documento Ativo'),
                        subtitle: Text(_active ? 'Visível e utilizável' : 'Arquivado e oculto'),
                        value: _active,
                        onChanged: (v) => setState(() => _active = v),
                        secondary: Icon(_active ? Icons.check_circle : Icons.remove_circle, color: _active ? Colors.green : Colors.grey),
                      ),

                      if (_saving)
                        const Padding(
                          padding: EdgeInsets.only(top: 24),
                          child: CircularProgressIndicator(),
                        ),
                    ],
                  ),
                ),
              ),
            ),

            // Botões
            Container(
              padding: const EdgeInsets.all(24),
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.surfaceContainerHighest,
                borderRadius: const BorderRadius.vertical(bottom: Radius.circular(20)),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  TextButton(
                    onPressed: _saving ? null : () => Navigator.pop(context),
                    child: const Text('Cancelar'),
                  ),
                  const SizedBox(width: 12),
                  FilledButton.icon(
                    onPressed: _saving ? null : _save,
                    icon: _saving ? const SizedBox(width: 16, height: 16, child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white)) : const Icon(Icons.save_rounded),
                    label: Text(_saving ? 'Salvando...' : 'Salvar Alterações'),
                    style: FilledButton.styleFrom(backgroundColor: Colors.orange),
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