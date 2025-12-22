// Novo arquivo: non_compliance_detail_page.dart
// Coloque este arquivo no mesmo diretório que document_detail_page.dart

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';
import '../models/enums.dart'; // Importe os enums (adicione Priority se necessário)

class NonComplianceDetailPage extends StatefulWidget {
  final String nonComplianceId;

  const NonComplianceDetailPage({super.key, required this.nonComplianceId});

  @override
  State<NonComplianceDetailPage> createState() => _NonComplianceDetailPageState();
}

class _NonComplianceDetailPageState extends State<NonComplianceDetailPage>
    with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  final AuthService authService = AuthService();

  Map<String, dynamic>? nonCompliance;
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
    await loadNonComplianceDetails();
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  Future<void> loadNonComplianceDetails() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      var data = await api.getNonComplianceById(widget.nonComplianceId);

      setState(() {
        nonCompliance = data;
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

  IconData _getIconByPriority(String? priority) {
    // .toUpperCase() garante que funcione mesmo se o backend mudar a capitalização
    switch (priority?.toUpperCase()) {
      case 'URGENCY':
        return Icons.notification_important; // Ícone de alerta urgente
      case 'HIGH_PRIORITY':
        return Icons.priority_high;          // Ícone de exclamação (!)
      case 'REGULAR':
        return Icons.warning_amber_rounded;  // Ícone de aviso (triângulo)
      case 'LOW_PRIORITY':
        return Icons.low_priority;           // Ícone de baixa prioridade
      default:
        return Icons.help_outline;           // Ícone padrão para nulo ou desconhecido
    }
  }

  void _showEditDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) => _EditNonComplianceForm(
        api: api,
        nonCompliance: nonCompliance!,
        onSaved: loadNonComplianceDetails,
      ),
    );
  }

  void _deleteNonCompliance() async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Confirmar Exclusão'),
        content: const Text('Tem certeza que deseja deletar esta não-conformidade?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Cancelar')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('Deletar')),
        ],
      ),
    );

    if (confirm == true) {
      try {
        await api.deleteNonCompliance(widget.nonComplianceId);
        Navigator.pop(context); // Volta para a lista
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Não-Conformidade deletada com sucesso!')),
        );
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Erro ao deletar: $e')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(nonCompliance?['customer'] ?? 'Detalhes da Não-Conformidade'),
        actions: _hasManagementPermission && nonCompliance != null
            ? [
          IconButton(
            icon: const Icon(Icons.edit),
            onPressed: _showEditDialog,
          ),
          IconButton(
            icon: const Icon(Icons.delete),
            onPressed: _deleteNonCompliance,
          ),
        ]
            : null,
      ),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
          ? Center(child: Text('Erro: $_error'))
          : FadeTransition(
        opacity: _animationController,
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              ListTile(
                leading: Icon(_getIconByPriority(nonCompliance!['priority'])),
                title: const Text('Cliente'),
                subtitle: Text(nonCompliance!['customer'] ?? 'N/A'),
                trailing: IconButton(
                  icon: const Icon(Icons.copy),
                  onPressed: () => _copyToClipboard(nonCompliance!['customer'] ?? '', 'Cliente'),
                ),
              ),
              const Divider(),
              ListTile(
                leading: const Icon(Icons.description),
                title: const Text('Descrição'),
                subtitle: Text(nonCompliance!['description'] ?? 'N/A'),
              ),
              const Divider(),
              ListTile(
                leading: const Icon(Icons.priority_high),
                title: const Text('Prioridade'),
                subtitle: Text(nonCompliance!['priority'] ?? 'N/A'),
              ),
              const Divider(),
              ListTile(
                leading: const Icon(Icons.calendar_today),
                title: const Text('Data de Abertura'),
                subtitle: Text(nonCompliance!['dateOpening'] ?? 'N/A'),
              ),
              const Divider(),
              ListTile(
                leading: const Icon(Icons.calendar_today),
                title: const Text('Data de Previsão'),
                subtitle: Text(nonCompliance!['datePrevision'] ?? 'N/A'),
              ),
              const Divider(),
              ListTile(
                leading: const Icon(Icons.check_circle),
                title: const Text('Eficácia'),
                subtitle: Text(nonCompliance!['efficacy'] ? 'Sim' : 'Não'),
              ),
              // Adicione mais campos conforme necessário (sector, origin, etc.)
            ],
          ),
        ),
      ),
    );
  }
}

class _EditNonComplianceForm extends StatefulWidget {
  final ApiService api;
  final Map<String, dynamic> nonCompliance;
  final VoidCallback onSaved;

  const _EditNonComplianceForm({
    required this.api,
    required this.nonCompliance,
    required this.onSaved,
  });

  @override
  _EditNonComplianceFormState createState() => _EditNonComplianceFormState();
}

class _EditNonComplianceFormState extends State<_EditNonComplianceForm> {
  final _formKey = GlobalKey<FormState>();
  late String _customer;
  late String _description;
  late bool _efficacy;
  late DateTime _dateOpening;
  late DateTime _datePrevision;
  Sector? _sector;
  DocumentOrigin? _origin;
  Priority? _priority;
  String? _processId;
  bool _saving = false;

  @override
  void initState() {
    super.initState();
    _customer = widget.nonCompliance['customer'] ?? '';
    _description = widget.nonCompliance['description'] ?? '';
    _efficacy = widget.nonCompliance['efficacy'] ?? true;
    _dateOpening = DateTime.parse(widget.nonCompliance['dateOpening'] ?? DateTime.now().toString());
    _datePrevision = DateTime.parse(widget.nonCompliance['datePrevision'] ?? DateTime.now().toString());
    _sector = Sector.values.firstWhere((e) => e.name == widget.nonCompliance['sector'], orElse: () => Sector.ADMINISTRATIVO); // Exemplo
    _origin = DocumentOrigin.values.firstWhere((e) => e.name == widget.nonCompliance['origin'], orElse: () => DocumentOrigin.INTERNO);
    _priority = Priority.values.firstWhere((e) => e.name == widget.nonCompliance['priority'], orElse: () => Priority.REGULAR);
    _processId = widget.nonCompliance['processId'].toString();
  }

  InputDecoration _inputDecoration(String label, IconData icon) {
    return InputDecoration(
      labelText: label,
      prefixIcon: Icon(icon),
      border: OutlineInputBorder(borderRadius: BorderRadius.circular(10)),
    );
  }

  Future<void> _save() async {
    if (_formKey.currentState!.validate()) {
      setState(() => _saving = true);
      try {
        await widget.api.updateNonCompliance(
          nonComplianceId: widget.nonCompliance['nonComplianceId'].toString(),
          createdBy: widget.nonCompliance['createdBy'].toString(),
          dateOpening: _dateOpening.toIso8601String().split('T')[0],
          processId: _processId!,
          sector: _sector!.name,
          origin: _origin!.name,
          priority: _priority!.name,
          customer: _customer,
          description: _description,
          efficacy: _efficacy,
          datePrevision: _datePrevision.toIso8601String().split('T')[0],
        );
        widget.onSaved();
        Navigator.pop(context);
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Erro: $e')));
      } finally {
        setState(() => _saving = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      contentPadding: EdgeInsets.zero,
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              color: Colors.orange,
              borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
            ),
            width: double.infinity,
            child: const Text(
              'Editar Não-Conformidade',
              style: TextStyle(color: Colors.white, fontSize: 20, fontWeight: FontWeight.bold),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(24),
            child: Form(
              key: _formKey,
              child: Column(
                children: [
                  TextFormField(
                    initialValue: _customer,
                    decoration: _inputDecoration('Cliente', Icons.person),
                    onChanged: (v) => _customer = v,
                    validator: (v) => v!.isEmpty ? 'Obrigatório' : null,
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    initialValue: _description,
                    decoration: _inputDecoration('Descrição', Icons.description),
                    onChanged: (v) => _description = v,
                    validator: (v) => v!.isEmpty ? 'Obrigatório' : null,
                  ),
                  const SizedBox(height: 16),
                  DropdownButtonFormField<Priority>(
                    value: _priority,
                    decoration: _inputDecoration('Prioridade', Icons.priority_high),
                    items: Priority.values
                        .map((e) => DropdownMenuItem(value: e, child: Text(e.displayName)))
                        .toList(),
                    onChanged: (v) => setState(() => _priority = v!),
                    validator: (v) => v == null ? 'Selecione uma prioridade' : null,
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
                  const SizedBox(height: 16),
                  // Data de Abertura
                  ListTile(
                    title: Text('Data de Abertura: ${_dateOpening.toString()}'),
                    trailing: const Icon(Icons.calendar_today),
                    onTap: () async {
                      final date = await showDatePicker(
                        context: context,
                        initialDate: _dateOpening,
                        firstDate: DateTime(2000),
                        lastDate: DateTime(2100),
                      );
                      if (date != null) setState(() => _dateOpening = date);
                    },
                  ),
                  // Data de Previsão
                  ListTile(
                    title: Text('Data de Previsão: ${_datePrevision.toString()}'),
                    trailing: const Icon(Icons.calendar_today),
                    onTap: () async {
                      final date = await showDatePicker(
                        context: context,
                        initialDate: _datePrevision,
                        firstDate: DateTime(2000),
                        lastDate: DateTime(2100),
                      );
                      if (date != null) setState(() => _datePrevision = date);
                    },
                  ),
                  const SizedBox(height: 24),
                  SwitchListTile(
                    title: const Text('Eficácia'),
                    subtitle: Text(_efficacy ? 'Eficaz' : 'Não Eficaz'),
                    value: _efficacy,
                    onChanged: (v) => setState(() => _efficacy = v),
                    secondary: Icon(_efficacy ? Icons.check_circle : Icons.remove_circle, color: _efficacy ? Colors.green : Colors.grey),
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
    );
  }
}