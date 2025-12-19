import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';
import 'non_compliance_detail_page.dart';
import '../models/enums.dart';

class NonCompliancesPage extends StatefulWidget {
  const NonCompliancesPage({super.key});

  @override
  State<NonCompliancesPage> createState() => _NonCompliancesPageState();
}

class _NonCompliancesPageState extends State<NonCompliancesPage> with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  final AuthService authService = AuthService();
  List<dynamic> nonCompliances = [];
  List<dynamic> filteredNonCompliances = [];
  bool _loading = true;
  String? _error;
  final TextEditingController _searchController = TextEditingController();
  late AnimationController _fabAnimationController;
  String _statusFilter = 'all';
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
    if (mounted) setState(() {});
    await loadNonCompliances();
  }

  @override
  void dispose() {
    _searchController.dispose();
    _fabAnimationController.dispose();
    super.dispose();
  }

  Future<void> loadNonCompliances() async {
    if (mounted && !_loading) {
      setState(() {
        _loading = true;
        _error = null;
      });
    }
    try {
      final data = await api.getNonCompliances();
      if (mounted) {
        setState(() {
          nonCompliances = data;
          _applyFilters();
          _loading = false;
        });
        _fabAnimationController.forward();
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _loading = false;
          _error = e.toString();
        });
      }
    }
  }

  void _applyFilters() {
    List<dynamic> result = nonCompliances;
    if (_statusFilter == 'efficacious') {
      result = result.where((nc) => nc['efficacy'] == true).toList();
    } else if (_statusFilter == 'not_efficacious') {
      result = result.where((nc) => nc['efficacy'] == false).toList();
    }

    if (_searchController.text.isNotEmpty) {
      final searchLower = _searchController.text.toLowerCase();
      result = result.where((nc) {
        final customer = (nc['customer'] ?? '').toString().toLowerCase();
        final description = (nc['description'] ?? '').toString().toLowerCase();
        return customer.contains(searchLower) || description.contains(searchLower);
      }).toList();
    }
    setState(() {
      filteredNonCompliances = result;
    });
  }

  void _showCreateNonComplianceDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext dialogContext) {
        return _CreateNonComplianceForm(
          api: api,
          onNonComplianceCreated: () {
            loadNonCompliances();
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: const Text('Não-Conformidade criada com sucesso!'),
                backgroundColor: Colors.green.shade600,
                behavior: SnackBarBehavior.floating,
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
      appBar: AppBar(title: const Text('Não-Conformidades')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: TextField(
              controller: _searchController,
              onChanged: (_) => _applyFilters(),
              decoration: InputDecoration(
                labelText: 'Buscar...',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                FilterChip(
                  label: const Text('Todas'),
                  selected: _statusFilter == 'all',
                  onSelected: (_) => setState(() { _statusFilter = 'all'; _applyFilters(); }),
                ),
                FilterChip(
                  label: const Text('Eficazes'),
                  selected: _statusFilter == 'efficacious',
                  onSelected: (_) => setState(() { _statusFilter = 'efficacious'; _applyFilters(); }),
                ),
                FilterChip(
                  label: const Text('Não Eficazes'),
                  selected: _statusFilter == 'not_efficacious',
                  onSelected: (_) => setState(() { _statusFilter = 'not_efficacious'; _applyFilters(); }),
                ),
              ],
            ),
          ),
          Expanded(
            child: _loading
                ? const Center(child: CircularProgressIndicator())
                : _error != null
                ? Center(child: Text('Erro: $_error'))
                : ListView.builder(
              itemCount: filteredNonCompliances.length,
              itemBuilder: (context, index) {
                final nc = filteredNonCompliances[index];
                return Card(
                  margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  child: ListTile(
                    leading: Icon(
                      nc['efficacy'] ? Icons.check_circle : Icons.error,
                      color: nc['efficacy'] ? Colors.green : Colors.red,
                    ),
                    title: Text(nc['customer'] ?? 'Cliente Desconhecido'),
                    subtitle: Text(nc['description'] ?? ''),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => NonComplianceDetailPage(
                            nonComplianceId: nc['nonComplianceId'].toString(),
                          ),
                        ),
                      );
                    },
                  ),
                );
              },
            ),
          ),
        ],
      ),
      floatingActionButton: _hasManagementPermission
          ? ScaleTransition(
        scale: _fabAnimationController,
        child: FloatingActionButton(
          onPressed: _showCreateNonComplianceDialog,
          child: const Icon(Icons.add),
          backgroundColor: Colors.orange,
        ),
      )
          : null,
    );
  }
}

class _CreateNonComplianceForm extends StatefulWidget {
  final ApiService api;
  final VoidCallback onNonComplianceCreated;

  const _CreateNonComplianceForm({
    required this.api,
    required this.onNonComplianceCreated,
  });

  @override
  _CreateNonComplianceFormState createState() => _CreateNonComplianceFormState();
}

class _CreateNonComplianceFormState extends State<_CreateNonComplianceForm> {
  final _formKey = GlobalKey<FormState>();

  // Variáveis para carregar processos
  List<dynamic> _processes = [];
  bool _loadingProcesses = true;
  String? _selectedProcessId;

  // Campos do formulário
  DateTime? _dateOpening = DateTime.now();
  Sector? _selectedSector;
  DocumentOrigin? _selectedOrigin;
  Priority? _selectedPriority;
  String _customer = '';
  String _description = '';
  bool _efficacy = true;
  DateTime? _datePrevision;

  bool _isLoading = false;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _fetchProcesses();
  }

  Future<void> _fetchProcesses() async {
    try {
      final data = await widget.api.getProcesses();
      setState(() {
        _processes = data;
        _loadingProcesses = false;
      });
    } catch (e) {
      setState(() {
        _errorMessage = "Erro ao carregar processos: $e";
        _loadingProcesses = false;
      });
    }
  }

  InputDecoration _buildInputDecoration(String label, IconData icon) {
    return InputDecoration(
      labelText: label,
      prefixIcon: Icon(icon, color: Colors.orange),
      border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
    );
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      _formKey.currentState!.save();
      setState(() { _isLoading = true; _errorMessage = null; });

      try {
        final String? userId = await AuthService().getUserId();

        await widget.api.createNonCompliance(
          createdBy: userId!,
          dateOpening: _dateOpening!.toIso8601String().split('T')[0],
          processId: _selectedProcessId!, // Usa o ID selecionado no Dropdown
          sector: _selectedSector!.name,
          origin: _selectedOrigin!.name,
          priority: _selectedPriority!.name,
          customer: _customer,
          description: _description,
          efficacy: _efficacy,
          datePrevision: _datePrevision!.toIso8601String().split('T')[0],
        );

        widget.onNonComplianceCreated();
        Navigator.of(context).pop();
      } catch (e) {
        setState(() => _errorMessage = e.toString());
      } finally {
        setState(() => _isLoading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      contentPadding: EdgeInsets.zero,
      content: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              padding: const EdgeInsets.all(24),
              decoration: const BoxDecoration(
                color: Colors.orange,
                borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
              ),
              child: const Row(
                children: [
                  Icon(Icons.add_task_rounded, color: Colors.white, size: 28),
                  SizedBox(width: 16),
                  Text('Criar Não-Conformidade', style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold)),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(24.0),
              child: Form(
                key: _formKey,
                child: Column(
                  children: [
                    // --- DROPDOWN DE PROCESSOS ---
                    _loadingProcesses
                        ? const CircularProgressIndicator()
                        : DropdownButtonFormField<String>(
                      value: _selectedProcessId,
                      decoration: _buildInputDecoration('Vincular ao Processo', Icons.account_tree),
                      items: _processes.map((proc) {
                        // O segredo está aqui: verifique se no seu JSON o Java envia 'processId' ou 'id'
                        return DropdownMenuItem<String>(
                          value: proc['processId']?.toString() ?? proc['id']?.toString(),
                          child: Text(proc['nameProcess'] ?? 'Sem nome'),
                        );
                      }).toList(),
                      onChanged: (val) {
                        setState(() => _selectedProcessId = val);
                        print("Processo selecionado ID: $val"); // Para seu debug
                      },
                      validator: (v) => v == null ? 'Obrigatório' : null,
                    ),
                    const SizedBox(height: 16),

                    DropdownButtonFormField<Sector>(
                      value: _selectedSector,
                      decoration: _buildInputDecoration('Setor', Icons.business),
                      items: Sector.values.map((s) => DropdownMenuItem(value: s, child: Text(s.displayName))).toList(),
                      onChanged: (v) => setState(() => _selectedSector = v),
                      validator: (v) => v == null ? 'Obrigatório' : null,
                    ),
                    const SizedBox(height: 16),

                    DropdownButtonFormField<DocumentOrigin>(
                      value: _selectedOrigin,
                      decoration: _buildInputDecoration('Origem', Icons.source),
                      items: DocumentOrigin.values.map((o) => DropdownMenuItem(value: o, child: Text(o.displayName))).toList(),
                      onChanged: (v) => setState(() => _selectedOrigin = v),
                      validator: (v) => v == null ? 'Obrigatório' : null,
                    ),
                    const SizedBox(height: 16),

                    DropdownButtonFormField<Priority>(
                      value: _selectedPriority,
                      decoration: _buildInputDecoration('Prioridade', Icons.priority_high),
                      items: Priority.values.map((p) => DropdownMenuItem(value: p, child: Text(p.displayName))).toList(),
                      onChanged: (v) => setState(() => _selectedPriority = v),
                      validator: (v) => v == null ? 'Obrigatório' : null,
                    ),
                    const SizedBox(height: 16),

                    TextFormField(
                      decoration: _buildInputDecoration('Cliente', Icons.person),
                      onSaved: (v) => _customer = v ?? '',
                      validator: (v) => v!.isEmpty ? 'Obrigatório' : null,
                    ),
                    const SizedBox(height: 16),

                    TextFormField(
                      decoration: _buildInputDecoration('Descrição', Icons.description),
                      maxLines: 3,
                      onSaved: (v) => _description = v ?? '',
                      validator: (v) => v!.isEmpty ? 'Obrigatório' : null,
                    ),
                    const SizedBox(height: 16),

                    // Seleção de Datas
                    ListTile(
                      title: Text(_datePrevision == null ? 'Selecionar Previsão' : 'Previsão: ${_datePrevision!.day}/${_datePrevision!.month}/${_datePrevision!.year}'),
                      leading: const Icon(Icons.calendar_month, color: Colors.orange),
                      onTap: () async {
                        final date = await showDatePicker(context: context, initialDate: DateTime.now(), firstDate: DateTime.now(), lastDate: DateTime(2100));
                        if (date != null) setState(() => _datePrevision = date);
                      },
                    ),

                    if (_errorMessage != null)
                      Padding(
                        padding: const EdgeInsets.only(top: 16),
                        child: Text(_errorMessage!, style: const TextStyle(color: Colors.red)),
                      ),

                    const SizedBox(height: 24),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: _isLoading ? null : _submitForm,
                        style: ElevatedButton.styleFrom(backgroundColor: Colors.orange, foregroundColor: Colors.white, padding: const EdgeInsets.all(16)),
                        child: _isLoading ? const CircularProgressIndicator(color: Colors.white) : const Text('CRIAR NÃO-CONFORMIDADE'),
                      ),
                    ),
                    TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancelar')),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}