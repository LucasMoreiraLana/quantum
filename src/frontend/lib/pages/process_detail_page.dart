import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';
import '../models/enums.dart'; // Certifique-se que o caminho para enums está correto

class ProcessDetailPage extends StatefulWidget {
  final String processId;
  const ProcessDetailPage({super.key, required this.processId});

  @override
  State<ProcessDetailPage> createState() => _ProcessDetailPageState();
}

class _ProcessDetailPageState extends State<ProcessDetailPage> with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  final AuthService authService = AuthService();

  Map<String, dynamic>? process;
  bool _loading = true;
  String? _error;
  late AnimationController _animationController;
  bool _hasManagementPermission = false;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(vsync: this, duration: const Duration(milliseconds: 600));
    _loadData();
  }

  Future<void> _loadData() async {
    _hasManagementPermission = await authService.hasManagementPermission();
    await loadProcessDetails();
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  Future<void> loadProcessDetails() async {
    setState(() { _loading = true; _error = null; });
    try {
      final data = await api.getProcessById(widget.processId);
      setState(() { process = data; _loading = false; });
      _animationController.forward();
    } catch (e) {
      setState(() { _loading = false; _error = e.toString(); });
    }
  }

  void _showEditDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) => _EditProcessForm(
        api: api,
        process: process!,
        onProcessUpdated: loadProcessDetails,
      ),
    );
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
            backgroundColor: Theme.of(context).colorScheme.surface,
            flexibleSpace: FlexibleSpaceBar(
              title: _loading || process == null ? null : Text(process!['nameProcess'] ?? 'Processo'),
              background: Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [Colors.blue, Colors.blue.withOpacity(0.7)],
                  ),
                ),
                child: _loading || process == null ? null : Center(
                  child: Hero(
                    tag: 'proc_${widget.processId}',
                    child: CircleAvatar(
                      radius: 40,
                      backgroundColor: Colors.white,
                      child: Text(
                        process!['cyclePDCA'] ?? '?',
                        style: const TextStyle(fontSize: 40, fontWeight: FontWeight.bold, color: Colors.blue),
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
          SliverToBoxAdapter(child: _buildBody()),
        ],
      ),
    );
  }

  Widget _buildBody() {
    if (_loading) return const Center(child: Padding(padding: EdgeInsets.all(64), child: CircularProgressIndicator()));
    if (_error != null) return Center(child: Text(_error!));
    if (process == null) return const Center(child: Text('Processo não encontrado'));

    return FadeTransition(
      opacity: _animationController,
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildSectionTitle('Informações', Icons.info_rounded),
            const SizedBox(height: 12),
            _buildInfoCard([
              _buildInfoTile(icon: Icons.badge, label: 'ID', value: widget.processId),
              _buildInfoTile(icon: Icons.title, label: 'Nome', value: process!['nameProcess']),
              _buildInfoTile(icon: Icons.calendar_today, label: 'Aprovação', value: process!['dateApproval']),
              _buildInfoTile(icon: Icons.event_available, label: 'Conclusão', value: process!['dateConclusion']),
            ]),
            const SizedBox(height: 24),
            _buildSectionTitle('Classificação', Icons.category),
            const SizedBox(height: 12),
            _buildInfoCard([
              _buildInfoTile(icon: Icons.business, label: 'Setor', value: process!['sector']),
              _buildInfoTile(icon: Icons.loop, label: 'Ciclo PDCA', value: process!['cyclePDCA']),
            ]),
            const SizedBox(height: 32),
            if (_hasManagementPermission)
              FilledButton.icon(
                onPressed: _showEditDialog,
                icon: const Icon(Icons.edit),
                label: const Text('Editar Processo'),
                style: FilledButton.styleFrom(
                  minimumSize: const Size(double.infinity, 50),
                  backgroundColor: Colors.blue,
                ),
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildSectionTitle(String title, IconData icon) {
    return Row(
      children: [
        Icon(icon, color: Colors.blue.shade700),
        const SizedBox(width: 8),
        Text(title, style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold)),
      ],
    );
  }

  Widget _buildInfoCard(List<Widget> children) {
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.surface,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.05), blurRadius: 10)],
      ),
      child: Column(
        children: [
          for (int i = 0; i < children.length; i++) ...[
            children[i],
            if (i < children.length - 1) const Divider(height: 1, indent: 16, endIndent: 16),
          ]
        ],
      ),
    );
  }

  Widget _buildInfoTile({required IconData icon, required String label, required dynamic value}) {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Row(
        children: [
          Icon(icon, color: Colors.blue.shade300),
          const SizedBox(width: 16),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(label, style: const TextStyle(fontWeight: FontWeight.w500, color: Colors.grey)),
                // Corrigido: Verifica se o valor é null antes de chamar toString()
                Text(
                    value == null ? 'Não informado' : value.toString(),
                    style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 16)
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _EditProcessForm extends StatefulWidget {
  final ApiService api;
  final Map<String, dynamic> process;
  final VoidCallback onProcessUpdated;

  const _EditProcessForm({required this.api, required this.process, required this.onProcessUpdated});

  @override
  State<_EditProcessForm> createState() => _EditProcessFormState();
}

class _EditProcessFormState extends State<_EditProcessForm> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _nameCtrl;
  late TextEditingController _approvalCtrl;
  late TextEditingController _conclusionCtrl;
  late Sector _sector;
  late Cycle _cycle;
  bool _saving = false;

  // Arquivo: process_detail_page.dart (Dentro de _EditProcessFormState)

  @override
  void initState() {
    super.initState();

    final processId = widget.process['processId'] as String? ?? '';
    final createdBy = widget.process['createdBy'] as String? ?? '';

    _nameCtrl = TextEditingController(text: widget.process['nameProcess'] ?? '');
    _approvalCtrl = TextEditingController(text: widget.process['dateApproval'] ?? '');
    _conclusionCtrl = TextEditingController(text: widget.process['dateConclusion'] ?? '');

    final sectorName = widget.process['sector'] as String? ?? 'ADMINISTRATIVO';
    _sector = Sector.values.firstWhere((e) => e.name == sectorName, orElse: () => Sector.ADMINISTRATIVO);

    final cycleName = widget.process['cyclePDCA'] as String? ?? 'P';
    _cycle = Cycle.values.firstWhere((e) => e.name == cycleName, orElse: () => Cycle.P);
  }

  Future<void> _selectDate(TextEditingController controller) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.tryParse(controller.text) ?? DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() => controller.text = picked.toIso8601String().split('T')[0]);
    }
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _saving = true);
    try {
      await widget.api.updateProcess(
        processId: widget.process['processId'],
        nameProcess: _nameCtrl.text,
        dateApproval: _approvalCtrl.text,
        dateConclusion: _conclusionCtrl.text,
        sector: _sector.name,
        cyclePDCA: _cycle.name,
      );
      if (mounted) {
        Navigator.pop(context);
        widget.onProcessUpdated();
      }
    } catch (e) {
      setState(() => _saving = false);
      if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Erro: $e')));
    }
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
            Container(
              padding: const EdgeInsets.all(24),
              decoration: BoxDecoration(
                color: Colors.blue.withOpacity(0.1),
                borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
              ),
              child: const Center(child: Text('Editar Processo', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold))),
            ),
            Flexible(
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(24),
                child: Form(
                  key: _formKey,
                  child: Column(
                    children: [
                      TextFormField(controller: _nameCtrl, decoration: const InputDecoration(labelText: 'Nome', border: OutlineInputBorder())),
                      const SizedBox(height: 16),
                      TextFormField(controller: _approvalCtrl, readOnly: true, onTap: () => _selectDate(_approvalCtrl), decoration: const InputDecoration(labelText: 'Aprovação', border: OutlineInputBorder())),
                      const SizedBox(height: 16),
                      TextFormField(controller: _conclusionCtrl, readOnly: true, onTap: () => _selectDate(_conclusionCtrl), decoration: const InputDecoration(labelText: 'Conclusão', border: OutlineInputBorder())),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<Sector>(
                        value: _sector,
                        decoration: const InputDecoration(labelText: 'Setor', border: OutlineInputBorder()),
                        items: Sector.values.map((s) => DropdownMenuItem(value: s, child: Text(s.displayName))).toList(),
                        onChanged: (v) => setState(() => _sector = v!),
                      ),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<Cycle>(
                        value: _cycle,
                        decoration: const InputDecoration(labelText: 'Ciclo', border: OutlineInputBorder()),
                        items: Cycle.values.map((c) => DropdownMenuItem(value: c, child: Text(c.name))).toList(),
                        onChanged: (v) => setState(() => _cycle = v!),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(24),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancelar')),
                  FilledButton(onPressed: _saving ? null : _save, child: Text(_saving ? 'Salvando...' : 'Salvar')),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}