package com.example.savethem.ui.Screens

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AppContent() {
	val context = LocalContext.current
	val isWearable = remember { isWearable(context) }

	if (isWearable) {
//		test()
	} else {
		SmartphoneScreen()
	}
}

@Composable
fun LoadingScreens() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		CircularProgressIndicator()
	}
}

@Composable
fun WearableScreen() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Text(text = "This is a Wearable device")
	}
}

@Composable
fun SmartphoneScreen() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Text(text = "This is a Smartphone device")
	}
}

fun isWearable(context: Context): Boolean {
	val packageManager = context.packageManager
	return packageManager.hasSystemFeature(PackageManager.FEATURE_WATCH)
}