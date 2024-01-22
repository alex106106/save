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
		message.data.let { data ->
			title
			body
//			Toast.makeText(this, "$title: $body", Toast.LENGTH_SHORT).show()
		}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				mayorqueoreo(title, body)
			}
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
				menorqueoreo(title, body)
			}

	}

	private fun menorqueoreo(titulo: String?, detalle: String?) {
		val id = "Mensaje"
		val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		val builder = NotificationCompat.Builder(this, id)
		builder.setAutoCancel(true)
			.setWhen(System.currentTimeMillis())
			.setContentTitle(titulo)
			.setSmallIcon(R.mipmap.sym_def_app_icon)
			.setContentText(detalle)
//			.setContentIntent(clicknoti())
			.setContentInfo("nuevo")
		val random = Random()
		val idNotify: Int = random.nextInt(8000)
		assert(nm != null)
		nm.notify(idNotify, builder.build())
	}

	private fun mayorqueoreo(titulo: String?, detalle: String?) {
		val id = "Mensaje"
		val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		val builder = NotificationCompat.Builder(this, id)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val nc = NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH)
			nc.setShowBadge(true)
			assert(nm != null)
			nm.createNotificationChannel(nc)
		}
		builder.setAutoCancel(true)
			.setWhen(System.currentTimeMillis())
			.setContentTitle(titulo)
			.setSmallIcon(R.mipmap.sym_def_app_icon)
			.setContentText(detalle)
//			.setContentIntent(clicknoti())
			.setContentInfo("nuevo")
		val random = Random()
		val idNotify: Int = random.nextInt(8000)
		assert(nm != null)
		nm.notify(idNotify, builder.build())
	}

//	fun clicknoti(): PendingIntent {
//		val nf = Intent(applicationContext, verNotificaciones::class.java)
//		nf.putExtra("color", "rojo")
//		return if (equals(nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP))) {
//			PendingIntent.getActivity(this, 0, nf, 0)
//		} else {
//			PendingIntent.getActivity(
//				this,
//				0,
//				nf,
//				PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_MUTABLE
//			)
//		}
//		//        dialog.show();
////        return null;
//	} //    AlertDialog dialog = new AlertDialog.Builder(notificacion.this).setPositiveButton("Notificaciones", new DialogInterface.OnClickListener() {
	//        @Override
	//        public void onClick(DialogInterface dialogInterface, int i) {
	//
	//        }
	//    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	//        @Override
	//        public void onClick(DialogInterface dialogInterface, int i) {
	//            dialogInterface.dismiss();
	//        }
	//    }).setTitle("confirmar").setMessage("¿Ir a notificaciones?").create();
}
