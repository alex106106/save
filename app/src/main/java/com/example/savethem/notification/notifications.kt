package com.example.savethem.notification

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class Notification : FirebaseMessagingService() {
	override fun onMessageReceived(message: RemoteMessage) {
		super.onMessageReceived(message)
		val title = message.data["titulo"]
		val body = message.data["detalle"]
		val iconName = message.data["icon"]

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			mayorqueoreo(title, body, iconName)
		} else {
			menorqueoreo(title, body, iconName)
		}
	}

	private fun menorqueoreo(titulo: String?, detalle: String?, iconName: String?) {
		val id = "Mensaje"
		val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		val resourceId = resources.getIdentifier(iconName, "drawable", packageName)
		val builder = NotificationCompat.Builder(this, id)

		builder.setAutoCancel(true)
			.setWhen(System.currentTimeMillis())
			.setContentTitle(titulo)
			.setSmallIcon(resourceId) // Usar el resource ID
			.setContentText(detalle)
			.setContentInfo("nuevo")

		val random = Random()
		val idNotify: Int = random.nextInt(8000)
		nm.notify(idNotify, builder.build())
	}

	private fun mayorqueoreo(titulo: String?, detalle: String?, iconName: String?) {
		val id = "Mensaje"
		val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		val resourceId = resources.getIdentifier(iconName, "drawable", packageName)
		val builder = NotificationCompat.Builder(this, id)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val nc = NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH)
			nc.setShowBadge(true)
			nm.createNotificationChannel(nc)
		}

		builder.setAutoCancel(true)
			.setWhen(System.currentTimeMillis())
			.setContentTitle(titulo)
			.setSmallIcon(resourceId) // Usar el resource ID
			.setContentText(detalle)
			.setContentInfo("nuevo")

		val random = Random()
		val idNotify: Int = random.nextInt(8000)
		nm.notify(idNotify, builder.build())
	}
}

