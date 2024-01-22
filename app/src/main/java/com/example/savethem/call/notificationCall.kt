package com.example.savethem.call

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

@Composable
fun CallButton() {
	val context = LocalContext.current

	Button(
		onClick = {
			notificationCall()
			// Aquí puedes realizar otras acciones relacionadas con la llamada
		},
		modifier = Modifier.padding(16.dp)
	) {
		Text(text = "Iniciar llamada")
	}
}

fun notificationCall() {
	FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
		if (task.isSuccessful) {
			val token = task.result
			// Aquí tienes el token del dispositivo receptor
			// Asigna el token a la variable receiverToken
			val receiverToken = token
			if (task.isSuccessful) {
				// La notificación se envió exitosamente
				val data = HashMap<String, String>()
				data["title"] = "Llamada entrante"
				data["body"] = "¡Tienes una nueva llamada entrante!"

				val notificationMessage = RemoteMessage.Builder(receiverToken)
					.setData(data)
					.build()

				FirebaseMessaging.getInstance().send(notificationMessage)
			} else {
				// Error al obtener el token del dispositivo
			}
		} else {
			// Error al obtener el token del dispositivo
		}
	}
}