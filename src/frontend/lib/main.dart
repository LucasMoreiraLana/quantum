import 'package:flutter/material.dart';
import 'pages/UsersPage.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    // Definindo uma cor base para o tema
    final Color seedColor = Colors.indigo;

    return MaterialApp(
      title: 'Quantum - Painel de Gestão',
      debugShowCheckedModeBanner: false,

      // Tema Claro (Light Theme)
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: seedColor,
          brightness: Brightness.light,
        ),
        useMaterial3: true,
        // Vamos dar um estilo mais limpo para os Cards
        // CORREÇÃO: Usando CardThemeData em vez de CardTheme
        cardTheme: CardThemeData(
          elevation: 1,
          margin: const EdgeInsets.symmetric(vertical: 8),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
            side: BorderSide(
              color: Colors.grey.shade300,
              width: 0.5,
            ),
          ),
        ),
      ),

      // Tema Escuro (Dark Theme)
      darkTheme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: seedColor,
          brightness: Brightness.dark,
        ),
        useMaterial3: true,
        // CORREÇÃO: Usando CardThemeData em vez de CardTheme
        cardTheme: CardThemeData(
          elevation: 1,
          margin: const EdgeInsets.symmetric(vertical: 8),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
            side: BorderSide(
              color: Colors.grey.shade800,
              width: 0.5,
            ),
          ),
        ),
      ),

      // O tema será escolhido pelo sistema (Claro ou Escuro)
      themeMode: ThemeMode.system,

      home: const UsersPage(),
    );
  }
}