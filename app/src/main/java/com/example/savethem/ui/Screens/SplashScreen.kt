package com.example.savethem.ui.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
	// Aquí puedes agregar la lógica para cargar datos o realizar tareas de inicialización

	// Simulamos un tiempo de espera con LaunchedEffect
	LaunchedEffect(Unit) {
		delay(2000) // Tiempo de espera de 2 segundos
		// Navega a la siguiente pantalla después del tiempo de espera
		// Puedes cambiar esto según la lógica de navegación de tu app
		// Por ejemplo, puedes navegar a la pantalla principal de tu app
	}

	// La pantalla del Splash, aquí puedes personalizarla según tus necesidades
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = "Mi App",
			style = TextStyle(
				fontSize = 30.sp,
				fontWeight = FontWeight.Bold
			)
		)
	}
}
