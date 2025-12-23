import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';
import '../models/enums.dart';

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

  // Função auxiliar para converter o formato de data do Java (Lista ou String) em algo legível
  String formatDate(dynamic value) {
    if (value == null) return 'N/A';
    if (value is List) {
      // Se for [2025, 12, 22], retorna "22/12/2025"
      final year = value[0];
      final month = value.length > 1 ? value[1].toString().padLeft(2, '0') : '01';
      final day = value.length > 2 ? value[2].toString().padLeft(2, '0') : '01';
      return '$day/$month/$year';
    }
    return value.toString();
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
        content: Text('$label copiado!'),
        backgroundColor: Colors.green.shade600,
        behavior: SnackBarBehavior.floating,
      ),
    );
  }

  IconData _getIconByPriority(String? priority) {
    switch (priority?.toUpperCase()) {
      case 'URGENCY': return Icons.notification_important;
      case 'HIGH_PRIORITY': return Icons.priority_high;
      case 'REGULAR': return Icons.warning_amber_rounded;
      case 'LOW_PRIORITY': return Icons.low_priority;
      default: return Icons.help_outline;
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
        Navigator.pop(context);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Deletada com sucesso!')));
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Erro ao deletar: $e')));
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(nonCompliance?['customer'] ?? 'Detalhes'),
        actions: _hasManagementPermission && nonCompliance != null
            ? [
          IconButton(icon: const Icon(Icons.edit), onPressed: _showEditDialog),
          IconButton(icon: const Icon(Icons.delete), onPressed: _deleteNonCompliance),
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
                leading: Icon(_getIconByPriority(nonCompliance!['priority']?.toString())),
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
                leading: const Icon(Icons.calendar_today),
                title: const Text('Data de Abertura'),
                subtitle: Text(formatDate(nonCompliance!['dateOpening'])),
              ),
              const Divider(),
              ListTile(
                leading: const Icon(Icons.calendar_today),
                title: const Text('Data de Previsão'),
                subtitle: Text(formatDate(nonCompliance!['datePrevision'])),
              ),
              const Divider(),
              ListTile(
                leading: const Icon(Icons.check_circle),
                title: const Text('Eficácia'),
                subtitle: Text(nonCompliance!['efficacy'] == true ? 'Sim' : 'Não'),
              ),
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
    final data = widget.nonCompliance;
    _customer = data['customer'] ?? '';
    _description = data['description'] ?? '';
    _efficacy = data['efficacy'] ?? false;

    _dateOpening = _parseDate(data['dateOpening']);
    _datePrevision = _parseDate(data['datePrevision']);

    _sector = Sector.values.firstWhere((e) => e.name == data['sector']?.toString(), orElse: () => Sector.ADMINISTRATIVO);
    _origin = DocumentOrigin.values.firstWhere((e) => e.name == data['origin']?.toString(), orElse: () => DocumentOrigin.INTERNO);
    _priority = Priority.values.firstWhere((e) => e.name == data['priority']?.toString(), orElse: () => Priority.REGULAR);
    _processId = data['processId']?.toString();
  }

  DateTime _parseDate(dynamic value) {
    if (value == null) return DateTime.now();
    if (value is List) {
      return DateTime(value[0], value.length > 1 ? value[1] : 1, value.length > 2 ? value[2] : 1);
    }
    return DateTime.tryParse(value.toString()) ?? DateTime.now();
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
      title: const Text('Editar Não-Conformidade'),
      content: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                initialValue: _customer,
                decoration: const InputDecoration(labelText: 'Cliente'),
                onChanged: (v) => _customer = v,
              ),
              const SizedBox(height: 16),
              TextFormField(
                initialValue: _description,
                decoration: const InputDecoration(labelText: 'Descrição'),
                onChanged: (v) => _description = v,
              ),
              const SizedBox(height: 16),
              DropdownButtonFormField<Priority>(
                value: _priority,
                decoration: const InputDecoration(labelText: 'Prioridade'),
                items: Priority.values.map((e) => DropdownMenuItem(value: e, child: Text(e.displayName))).toList(),
                onChanged: (v) => setState(() => _priority = v),
              ),
              ListTile(
                title: Text('Abertura: ${_dateOpening.day}/${_dateOpening.month}/${_dateOpening.year}'),
                trailing: const Icon(Icons.calendar_today),
                onTap: () async {
                  final d = await showDatePicker(context: context, initialDate: _dateOpening, firstDate: DateTime(2000), lastDate: DateTime(2100));
                  if (d != null) setState(() => _dateOpening = d);
                },
              ),
              SwitchListTile(
                title: const Text('Eficaz'),
                value: _efficacy,
                onChanged: (v) => setState(() => _efficacy = v),
              ),
              if (_saving) const CircularProgressIndicator(),
            ],
          ),
        ),
      ),
      actions: [
        TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancelar')),
        ElevatedButton(onPressed: _saving ? null : _save, child: const Text('Salvar')),
      ],
    );
  }
}