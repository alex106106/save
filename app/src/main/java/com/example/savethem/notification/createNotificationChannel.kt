package com.example.savethem.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.savethem.R
import com.example.savethem.ui.Main.MainActivity

fun createNotificationChannel(channelId: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Mi Canal de Prueba"
        val descriptionText = "Canal de prueba para mis notificaciones importantes"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
@SuppressLint("InlinedApi") // Esta anotación es necesaria para usar el modo de vibración `EFFECT_HEAVY_CLICK`
fun showSimpleNotificationWithTapAction(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    val defaults = NotificationCompat.DEFAULT_LIGHTS // Establecer un patrón de vibración predeterminado

    val vibrationEffect = VibrationEffect.createWaveform(longArrayOf(0, 250), intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE), -1) // Crear un objeto VibrationEffect para una vibración de 10 segundos

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.danger)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(priority)
        .setContentIntent(pendingIntent)
        .setDefaults(defaults) // Establecer los valores predeterminados, incluyendo la vibración
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        vibrator.vibrate(vibrationEffect) // Iniciar la vibración si el dispositivo está en Android 12 o superior
    } else {
//        @Suppress("DEPRECATION")
//        vibrator.vibrate(vibrationEffect.pattern, -1) // Iniciar la vibración si el dispositivo está en una versión anterior de Android
    }
}