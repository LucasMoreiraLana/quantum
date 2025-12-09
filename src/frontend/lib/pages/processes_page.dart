import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';
import '../models/enums.dart';
import 'process_detail_page.dart';

class ProcessesPage extends StatefulWidget {
  const ProcessesPage({super.key});

  @override
  State<ProcessesPage> createState() => _ProcessesPageState();
}

class _ProcessesPageState extends State<ProcessesPage> with SingleTickerProviderStateMixin {
  final ApiService api = ApiService();
  final AuthService authService = AuthService();

  List<dynamic> processes = [];
  List<dynamic> filteredProcesses = [];
  bool _loading = true;
  String? _error;

  final TextEditingController _searchController = TextEditingController();
  late AnimationController _fabAnimationController;

  // Filtro de Ciclo: 'ALL', 'P', 'D', 'C', 'A'
  String _cycleFilter = 'ALL';
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
    await loadProcesses();
  }

  @override
  void dispose() {
    _searchController.dispose();
    _fabAnimationController.dispose();
    super.dispose();
  }

  Future<void> loadProcesses() async {
    if (mounted && !_loading) {
      setState(() {
        _loading = true;
        _error = null;
      });
    }

    try {
      final data = await api.getProcesses();
      if (mounted) {
        setState(() {
          processes = data;
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
          processes = [];
          filteredProcesses = [];
        });
      }
    }
  }

  void _applyFilters() {
    List<dynamic> result = processes;

    // Filtro por Ciclo PDCA
    if (_cycleFilter != 'ALL') {
      result = result.where((proc) => proc['cyclePDCA'] == _cycleFilter).toList();
    }

    // Filtro por busca
    if (_searchController.text.isNotEmpty) {
      final searchLower = _searchController.text.toLowerCase();
      result = result.where((proc) {
        final name = (proc['nameProcess'] ?? '').toString().toLowerCase();
        final sector = (proc['sector'] ?? '').toString().toLowerCase();
        return name.contains(searchLower) || sector.contains(searchLower);
      }).toList();
    }

    setState(() {
      filteredProcesses = result;
    });
  }

  void _filterProcesses(String query) => _applyFilters();

  void _changeCycleFilter(String filter) {
    setState(() {
      _cycleFilter = filter;
      _applyFilters();
    });
  }

  void _showCreateProcessDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext dialogContext) {
        return _CreateProcessForm(
          api: api,
          onProcessCreated: () {
            loadProcesses();
            if (mounted) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: const Row(
                    children: [
                      Icon(Icons.check_circle, color: Colors.white),
                      SizedBox(width: 12),
                      Text('Processo criado com sucesso!'),
                    ],
                  ),
                  backgroundColor: Colors.blue.shade600,
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
                'Gestão de Processos',
                style: TextStyle(fontWeight: FontWeight.w600),
              ),
              background: Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [
                      Colors.blue.withOpacity(0.1),
                      Theme.of(context).colorScheme.surface,
                    ],
                  ),
                ),
              ),
            ),
            actions: [
              IconButton(
                icon: const Icon(Icons.refresh_rounded),
                onPressed: _loading ? null : loadProcesses,
                tooltip: 'Recarregar',
              ),
              const SizedBox(width: 8),
            ],
          ),

          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
              child: _buildSearchBar(),
            ),
          ),

          SliverToBoxAdapter(
            child: SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              child: _buildCycleFilters(),
            ),
          ),

          if (!_loading && _error == null && processes.isNotEmpty)
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                child: Text(
                  '${filteredProcesses.length} Processos encontrados',
                  style: TextStyle(color: Theme.of(context).colorScheme.secondary),
                ),
              ),
            ),

          SliverToBoxAdapter(child: _buildBody()),
        ],
      ),
      floatingActionButton: _hasManagementPermission
          ? ScaleTransition(
        scale: CurvedAnimation(parent: _fabAnimationController, curve: Curves.easeOutBack),
        child: FloatingActionButton.extended(
          onPressed: _showCreateProcessDialog,
          icon: const Icon(Icons.add_rounded),
          label: const Text('Novo Processo'),
          elevation: 4,
          backgroundColor: Colors.blue,
          foregroundColor: Colors.white,
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
        boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.05), blurRadius: 10, offset: const Offset(0, 2))],
      ),
      child: TextField(
        controller: _searchController,
        onChanged: _filterProcesses,
        decoration: InputDecoration(
          hintText: 'Buscar processos...',
          prefixIcon: const Icon(Icons.search_rounded),
          suffixIcon: _searchController.text.isNotEmpty
              ? IconButton(onPressed: () { _searchController.clear(); _filterProcesses(''); }, icon: const Icon(Icons.clear_rounded))
              : null,
          border: OutlineInputBorder(borderRadius: BorderRadius.circular(16), borderSide: BorderSide.none),
          filled: true,
          fillColor: Theme.of(context).colorScheme.surface,
          contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
        ),
      ),
    );
  }

  Widget _buildCycleFilters() {
    return Row(
      children: [
        _buildFilterChip('Todos', 'ALL', Colors.blue),
        const SizedBox(width: 8),
        _buildFilterChip('Plan (P)', 'P', Colors.indigo),
        const SizedBox(width: 8),
        _buildFilterChip('Do (D)', 'D', Colors.orange),
        const SizedBox(width: 8),
        _buildFilterChip('Check (C)', 'C', Colors.green),
        const SizedBox(width: 8),
        _buildFilterChip('Act (A)', 'A', Colors.red),
      ],
    );
  }

  Widget _buildFilterChip(String label, String value, Color color) {
    final isSelected = _cycleFilter == value;
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: () => _changeCycleFilter(value),
        borderRadius: BorderRadius.circular(20),
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 200),
          padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
          decoration: BoxDecoration(
            color: isSelected ? color : Theme.of(context).colorScheme.surface,
            borderRadius: BorderRadius.circular(20),
            border: Border.all(color: isSelected ? color : Colors.grey.withOpacity(0.3)),
            boxShadow: isSelected ? [BoxShadow(color: color.withOpacity(0.3), blurRadius: 8, offset: const Offset(0, 2))] : [],
          ),
          child: Text(
            label,
            style: TextStyle(
              color: isSelected ? Colors.white : Theme.of(context).colorScheme.onSurface,
              fontWeight: isSelected ? FontWeight.bold : FontWeight.w500,
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildBody() {
    if (_loading) return const Center(child: Padding(padding: EdgeInsets.all(64), child: CircularProgressIndicator()));
    if (_error != null) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            children: [
              Icon(Icons.error_outline_rounded, color: Theme.of(context).colorScheme.error, size: 48),
              const SizedBox(height: 16),
              Text('Erro ao carregar processos', style: Theme.of(context).textTheme.titleLarge),
              Text(_error!, textAlign: TextAlign.center),
              const SizedBox(height: 24),
              FilledButton.icon(icon: const Icon(Icons.refresh_rounded), label: const Text('Tentar Novamente'), onPressed: loadProcesses),
            ],
          ),
        ),
      );
    }
    if (filteredProcesses.isEmpty) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(48),
          child: Column(
            children: [
              Icon(Icons.assignment_outlined, color: Colors.blue.withOpacity(0.5), size: 64),
              const SizedBox(height: 16),
              Text('Nenhum processo encontrado', style: Theme.of(context).textTheme.titleMedium),
            ],
          ),
        ),
      );
    }
    return ListView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      padding: const EdgeInsets.fromLTRB(16, 8, 16, 100),
      itemCount: filteredProcesses.length,
      itemBuilder: (context, index) => _buildProcessCard(filteredProcesses[index], index),
    );
  }

  Widget _buildProcessCard(Map<String, dynamic> proc, int index) {
    final name = proc['nameProcess'] ?? 'Sem Nome';
    final sector = proc['sector'] ?? 'N/A';
    final cycle = proc['cyclePDCA'] ?? 'P';

    // Configurações visuais baseadas no ciclo
    Color cycleColor;
    IconData cycleIcon;
    switch(cycle) {
      case 'P': cycleColor = Colors.indigo; cycleIcon = Icons.architecture_rounded; break;
      case 'D': cycleColor = Colors.orange; cycleIcon = Icons.engineering_rounded; break;
      case 'C': cycleColor = Colors.green; cycleIcon = Icons.fact_check_rounded; break;
      case 'A': cycleColor = Colors.red; cycleIcon = Icons.published_with_changes_rounded; break;
      default: cycleColor = Colors.blue; cycleIcon = Icons.assignment;
    }

    return TweenAnimationBuilder<double>(
      tween: Tween(begin: 0.0, end: 1.0),
      duration: Duration(milliseconds: 300 + (index * 50)),
      curve: Curves.easeOutCubic,
      builder: (context, value, child) {
        return Transform.translate(
          offset: Offset(0, 20 * (1 - value)),
          child: Opacity(opacity: value, child: child),
        );
      },
      child: Container(
        margin: const EdgeInsets.only(bottom: 12),
        decoration: BoxDecoration(
          color: Theme.of(context).colorScheme.surface,
          borderRadius: BorderRadius.circular(16),
          boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.05), blurRadius: 10, offset: const Offset(0, 2))],
        ),
        child: Material(
          color: Colors.transparent,
          child: InkWell(
            borderRadius: BorderRadius.circular(16),
            onTap: () async {
              final id = proc['processId'];
              if (id == null) return;
              await Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => ProcessDetailPage(processId: id.toString()))
              );
              loadProcesses();
            },
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Row(
                children: [
                  Container(
                    width: 56,
                    height: 56,
                    decoration: BoxDecoration(
                      color: cycleColor.withOpacity(0.1),
                      borderRadius: BorderRadius.circular(16),
                    ),
                    child: Center(child: Text(cycle, style: TextStyle(color: cycleColor, fontWeight: FontWeight.bold, fontSize: 24))),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(name, style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 16), maxLines: 1, overflow: TextOverflow.ellipsis),
                        const SizedBox(height: 4),
                        Row(
                          children: [
                            Icon(Icons.business, size: 14, color: Theme.of(context).colorScheme.onSurfaceVariant),
                            const SizedBox(width: 4),
                            Text(sector.toString().replaceAll('_', ' '), style: TextStyle(fontSize: 12, color: Theme.of(context).colorScheme.onSurfaceVariant)),
                          ],
                        ),
                      ],
                    ),
                  ),
                  Icon(Icons.arrow_forward_ios_rounded, size: 16, color: Colors.blue.shade700),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

// ================= FORMULÁRIO DE CRIAÇÃO =================

class _CreateProcessForm extends StatefulWidget {
  final ApiService api;
  final VoidCallback onProcessCreated;

  const _CreateProcessForm({required this.api, required this.onProcessCreated});

  @override
  State<_CreateProcessForm> createState() => _CreateProcessFormState();
}

class _CreateProcessFormState extends State<_CreateProcessForm> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _approvalDateController = TextEditingController();
  final _conclusionDateController = TextEditingController();

  Sector? _selectedSector;
  Cycle? _selectedCycle;
  bool _isLoading = false;
  String? _currentUserId;

  @override
  void initState() {
    super.initState();
    AuthService().getCurrentUser().then((u) => setState(() => _currentUserId = u['userId']));
  }

  Future<void> _selectDate(TextEditingController controller) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        controller.text = picked.toIso8601String().split('T')[0];
      });
    }
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate() || _currentUserId == null) return;
    setState(() => _isLoading = true);

    try {
      await widget.api.createProcess(
        nameProcess: _nameController.text,
        dateApproval: _approvalDateController.text,
        dateConclusion: _conclusionDateController.text,
        sector: _selectedSector!.name,
        cyclePDCA: _selectedCycle!.name,
      );
      if (mounted) Navigator.pop(context);
      widget.onProcessCreated();
    } catch (e) {
      setState(() => _isLoading = false);
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
                gradient: LinearGradient(colors: [Colors.blue.withOpacity(0.3), Theme.of(context).colorScheme.surface]),
                borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
              ),
              child: Row(
                children: [
                  const Icon(Icons.add_task_rounded, color: Colors.blue, size: 28),
                  const SizedBox(width: 16),
                  Text('Novo Processo', style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold)),
                ],
              ),
            ),
            Flexible(
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(24),
                child: Form(
                  key: _formKey,
                  child: Column(
                    children: [
                      TextFormField(
                        controller: _nameController,
                        decoration: const InputDecoration(labelText: 'Nome do Processo', border: OutlineInputBorder(), prefixIcon: Icon(Icons.title)),
                        validator: (v) => v!.isEmpty ? 'Obrigatório' : null,
                      ),
                      const SizedBox(height: 16),
                      Row(
                        children: [
                          Expanded(
                            child: TextFormField(
                              controller: _approvalDateController,
                              readOnly: true,
                              onTap: () => _selectDate(_approvalDateController),
                              decoration: const InputDecoration(labelText: 'Data Aprovação', border: OutlineInputBorder(), prefixIcon: Icon(Icons.calendar_today)),
                              validator: (v) => v!.isEmpty ? 'Obrigatório' : null,
                            ),
                          ),
                          const SizedBox(width: 16),
                          Expanded(
                            child: TextFormField(
                              controller: _conclusionDateController,
                              readOnly: true,
                              onTap: () => _selectDate(_conclusionDateController),
                              decoration: const InputDecoration(labelText: 'Data Conclusão', border: OutlineInputBorder(), prefixIcon: Icon(Icons.event_available)),
                              validator: (v) => v!.isEmpty ? 'Obrigatório' : null,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<Sector>(
                        value: _selectedSector,
                        decoration: const InputDecoration(labelText: 'Setor', border: OutlineInputBorder(), prefixIcon: Icon(Icons.business)),
                        items: Sector.values.map((s) => DropdownMenuItem(value: s, child: Text(s.displayName))).toList(),
                        onChanged: (v) => setState(() => _selectedSector = v),
                        validator: (v) => v == null ? 'Obrigatório' : null,
                      ),
                      const SizedBox(height: 16),
                      DropdownButtonFormField<Cycle>(
                        value: _selectedCycle,
                        decoration: const InputDecoration(labelText: 'Ciclo PDCA', border: OutlineInputBorder(), prefixIcon: Icon(Icons.loop)),
                        items: Cycle.values.map((c) => DropdownMenuItem(value: c, child: Text('${c.name} - ${c.name == 'P' ? 'Plan' : c.name == 'D' ? 'Do' : c.name == 'C' ? 'Check' : 'Act'}'))).toList(),
                        onChanged: (v) => setState(() => _selectedCycle = v),
                        validator: (v) => v == null ? 'Obrigatório' : null,
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
                  const SizedBox(width: 12),
                  FilledButton(
                    onPressed: _isLoading ? null : _submit,
                    child: Text(_isLoading ? 'Salvando...' : 'Criar'),
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