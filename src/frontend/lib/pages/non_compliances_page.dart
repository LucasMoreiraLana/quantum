// Novo arquivo: non_compliances_page.dart
// Coloque este arquivo no mesmo diretório que documents_page.dart

import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';
import 'non_compliance_detail_page.dart'; // Importe a página de detalhes que criaremos
import '../models/enums.dart'; // Importe os enums (adicione Priority se necessário)

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
  // Filtro de status: 'all', 'active', 'inactive' (adaptado para efficacy se necessário)
  String _statusFilter = 'all';
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
          _error = null;
        });
        _fabAnimationController.forward();
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _loading = false;
          _error = e.toString();
          nonCompliances = [];
          filteredNonCompliances = [];
        });
      }
    }
  }

  void _applyFilters() {
    List<dynamic> result = nonCompliances;
    // Filtro por efficacy (adaptado de active)
    if (_statusFilter == 'efficacious') {
      result = result.where((nc) => nc['efficacy'] == true).toList();
    } else if (_statusFilter == 'not_efficacious') {
      result = result.where((nc) => nc['efficacy'] == false).toList();
    }
    // Filtro por busca
    if (_searchController.text.isNotEmpty) {
      final searchLower = _searchController.text.toLowerCase();
      result = result.where((nc) {
        final customer = (nc['customer'] ?? '').toString().toLowerCase();
        final description = (nc['description'] ?? '').toString().toLowerCase();
        final priority = (nc['priority'] ?? '').toString().toLowerCase();
        return customer.contains(searchLower) ||
            description.contains(searchLower) ||
            priority.contains(searchLower);
      }).toList();
    }
    setState(() {
      filteredNonCompliances = result;
    });
  }

  void _filterNonCompliances(String query) {
    _applyFilters();
  }

  void _changeStatusFilter(String filter) {
    setState(() {
      _statusFilter = filter;
      _applyFilters();
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
            if (mounted) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: const Row(
                    children: [
                      Icon(Icons.check_circle, color: Colors.white),
                      SizedBox(width: 12),
                      Text('Não-Conformidade criada com sucesso!'),
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
      appBar: AppBar(
        title: const Text('Não-Conformidades'),
        actions: [
          if (_hasManagementPermission)
            IconButton(
              icon: const Icon(Icons.refresh),
              onPressed: loadNonCompliances,
            ),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: TextField(
              controller: _searchController,
              onChanged: _filterNonCompliances,
              decoration: InputDecoration(
                labelText: 'Buscar não-conformidades...',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
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
                  onSelected: (_) => _changeStatusFilter('all'),
                ),
                FilterChip(
                  label: const Text('Eficazes'),
                  selected: _statusFilter == 'efficacious',
                  onSelected: (_) => _changeStatusFilter('efficacious'),
                ),
                FilterChip(
                  label: const Text('Não Eficazes'),
                  selected: _statusFilter == 'not_efficacious',
                  onSelected: (_) => _changeStatusFilter('not_efficacious'),
                ),
              ],
            ),
          ),
          Expanded(
            child: _loading
                ? const Center(child: CircularProgressIndicator())
                : _error != null
                ? Center(child: Text('Erro: $_error'))
                : RefreshIndicator(
              onRefresh: loadNonCompliances,
              child: ListView.builder(
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
                      subtitle: Text(nc['description']?.substring(0, 50) ?? 'Sem descrição'),
                      trailing: Text(nc['priority'] ?? ''),
                      onTap: () {
                        final String id = nc['nonComplianceId'].toString();
                        if (RegExp(r'^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$', caseSensitive: false).hasMatch(id)) {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => NonComplianceDetailPage(nonComplianceId: id),
                            ),
                          );
                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text('ID inválido: $id. Deve ser um UUID.')),
                          );
                        }
                      },
                    ),
                  );
                },
              ),
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
  String? _createdBy; // Obter do auth se possível
  DateTime? _dateOpening;
  String? _processId;
  Sector? _selectedSector;
  DocumentOrigin? _selectedOrigin;
  Priority? _selectedPriority; // Assumindo que Priority está em enums.dart
  String _customer = '';
  String _description = '';
  bool _efficacy = true;
  DateTime? _datePrevision;
  bool _isLoading = false;
  String? _errorMessage;

  InputDecoration _buildInputDecoration(String label, IconData icon) {
    return InputDecoration(
      labelText: label,
      prefixIcon: Icon(icon, color: Colors.orange),
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: const BorderSide(color: Colors.orange, width: 2),
      ),
    );
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      _formKey.currentState!.save();
      setState(() {
        _isLoading = true;
        _errorMessage = null;
      });

      try {
        // Obter createdBy do authService se necessário
        _createdBy ??= await AuthService().getUserId(); // Assumindo método getUserId()

        await widget.api.createNonCompliance(
          createdBy: _createdBy!,
          dateOpening: _dateOpening!.toIso8601String().split('T')[0],
          processId: _processId!,
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
        setState(() {
          _errorMessage = e.toString();
        });
      } finally {
        setState(() {
          _isLoading = false;
        });
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
          // Cabeçalho
          Container(
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              color: Colors.orange,
              borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
            ),
            child: const Row(
              children: [
                Icon(Icons.add_task_rounded, color: Colors.white, size: 28),
                SizedBox(width: 16),
                Text(
                  'Criar Não-Conformidade',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
          ),
          // Formulário
          SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(24.0),
              child: Form(
                key: _formKey,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Process ID (exemplo como texto, adapte se dropdown de processes)
                    TextFormField(
                      decoration: _buildInputDecoration('ID do Processo', Icons.business_center),
                      onSaved: (value) => _processId = value,
                      validator: (value) => value == null || value.isEmpty ? 'Campo obrigatório' : null,
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
                    DropdownButtonFormField<Priority>(
                      value: _selectedPriority,
                      decoration: _buildInputDecoration('Prioridade', Icons.priority_high),
                      items: Priority.values.map((priority) {
                        return DropdownMenuItem(
                          value: priority,
                          child: Text(priority.displayName), // Assumindo displayName em enum
                        );
                      }).toList(),
                      onChanged: (value) => setState(() => _selectedPriority = value),
                      validator: (value) => value == null ? 'Campo obrigatório' : null,
                    ),
                    const SizedBox(height: 16),
                    TextFormField(
                      decoration: _buildInputDecoration('Cliente', Icons.person),
                      onSaved: (value) => _customer = value ?? '',
                      validator: (value) => value == null || value.isEmpty ? 'Campo obrigatório' : null,
                    ),
                    const SizedBox(height: 16),
                    TextFormField(
                      decoration: _buildInputDecoration('Descrição', Icons.description),
                      maxLines: 3,
                      onSaved: (value) => _description = value ?? '',
                      validator: (value) => value == null || value.isEmpty ? 'Campo obrigatório' : null,
                    ),
                    const SizedBox(height: 16),
                    SwitchListTile(
                      title: const Text('Eficácia'),
                      value: _efficacy,
                      onChanged: (v) => setState(() => _efficacy = v),
                      secondary: Icon(_efficacy ? Icons.check_circle : Icons.remove_circle, color: _efficacy ? Colors.green : Colors.grey),
                    ),
                    const SizedBox(height: 16),
                    // Data de Abertura
                    ListTile(
                      title: Text(_dateOpening == null ? 'Data de Abertura' : _dateOpening.toString()),
                      trailing: const Icon(Icons.calendar_today),
                      onTap: () async {
                        final date = await showDatePicker(
                          context: context,
                          initialDate: DateTime.now(),
                          firstDate: DateTime(2000),
                          lastDate: DateTime(2100),
                        );
                        if (date != null) setState(() => _dateOpening = date);
                      },
                    ),
                    // Data de Previsão
                    ListTile(
                      title: Text(_datePrevision == null ? 'Data de Previsão' : _datePrevision.toString()),
                      trailing: const Icon(Icons.calendar_today),
                      onTap: () async {
                        final date = await showDatePicker(
                          context: context,
                          initialDate: DateTime.now(),
                          firstDate: DateTime(2000),
                          lastDate: DateTime(2100),
                        );
                        if (date != null) setState(() => _datePrevision = date);
                      },
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
                    // Botão de Submissão
                    const SizedBox(height: 24),
                    ElevatedButton.icon(
                      onPressed: _isLoading ? null : _submitForm,
                      icon: const Icon(Icons.add_task_rounded),
                      label: const Text('Criar Não-Conformidade'),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.orange,
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 16),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12),
                        ),
                        textStyle: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
                      ),
                    ),
                    const SizedBox(height: 8),
                    TextButton(
                      onPressed: () => Navigator.of(context).pop(),
                      child: const Text('Cancelar'),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}