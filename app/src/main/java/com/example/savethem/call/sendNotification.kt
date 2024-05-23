package com.example.savethem.call

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.savethem.Model.registerModel
import com.example.savethem.ViewModel.FriendsViewModel
import com.example.savethem.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun boton(friendsViewModel: FriendsViewModel, navController: NavController) {
	val context = LocalContext.current
	val asd = ""
	val tokenn = FirebaseMessaging.getInstance().token

	var idToFriend = ""
	val friends by friendsViewModel.friends.collectAsState()
	var selectedFriend by remember { mutableStateOf("") }
	friendsViewModel.getAllFriends()
	LazyColumn{
		friends.forEach { friend ->
			item {
				Card(
					onClick = {
							  selectedFriend = friend.UUID!!
						true
					},
					modifier = Modifier
						.padding(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 14.dp)
						.fillMaxSize(),
					elevation = 8.dp,
					shape = RoundedCornerShape(8.dp),
					backgroundColor = Color.White,) {
					Column() {
						Text(
							modifier = Modifier
								.padding(3.dp),
							text = friend.name.toString(),
							color = Color.Black)
						Text(
							modifier = Modifier
								.padding(3.dp),
							text = friend.email.toString(),
							color = Color.Black)
					}
				}
			}
		}
	}
//
	LaunchedEffect(selectedFriend){
		if (selectedFriend.isNotEmpty()){
			navController.navigate(Screens.Chat.route + "/$selectedFriend")
		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun tokensFriends(friendsViewModel: FriendsViewModel, navController: NavController) {

	val friends by friendsViewModel.friends.collectAsState()
	var selectedFriend by remember { mutableStateOf("") }
	friendsViewModel.getAllFriends()
	Column(modifier = Modifier.fillMaxSize()) {
		Button(onClick = {
			Log.d("TOKENS", friends.map { it.token }.toString())
		}) {
			Text(text = "tokens")
		}

		friends.forEach { friend ->
			Text(text = "Friend: ${friend.name}, Token: ${friend.token}")
		}
	}
}

private fun enviarNotificacion(token: String) {
	val TAGS = "SUCC"
	val TAGE = "ERR"
	val url = "https://fcm.googleapis.com/fcm/send"
	val serverKey = "AAAAYZxEHaw:APA91bEiMfej-oGG4d1AZQy80bHBHGCEwUMvpQVPgj48J7WFvOAwu23Mz_VLoJqnfT7s9Gv3UdAIePwyA_o_KRjBEE2UsfhCfDduYljGQ_32Fd7j64i4ymlexVuBXD8Iu7tgFmDOPRog" // Reemplaza con tu clave de servidor de FCM

	val mediaType = "application/json".toMediaTypeOrNull()
	val client = OkHttpClient()

	val json = JSONObject()
	json.put("to", token) // Reemplaza con el token de registro del usuario
	json.put("priority", "high")

	val notificationJson = JSONObject()
	notificationJson.put("title", "Título de la notificación")
	notificationJson.put("body", "Contenido de la notificación")
	json.put("notification", notificationJson)

	val body = json.toString().toRequestBody(mediaType)
	val request = Request.Builder()
		.url(url)
		.post(body)
		.addHeader("Authorization", "key=$serverKey")
		.build()

	// Ejecutar la solicitud en un contexto de Coroutine
	CoroutineScope(Dispatchers.IO).launch {
		client.newCall(request).execute().use { response ->
			// Manejar la respuesta del servidor
			if (response.isSuccessful) {
				// Notificación enviada exitosamente
				Log.d(TAGS, "FUNCIONA CORRECTAMENTE")
			} else {
				// Error al enviar la notificación
				Log.d(TAGE, "NO FUNCIONA")
			}
		}
	}
}

fun enviar(context: Context, token: String) {
	val requestQueue = Volley.newRequestQueue(context)
	val json = JSONObject()
	try {
		json.put("to", token)
		val notificacion = JSONObject()
		notificacion.put("titulo", "hola")
		notificacion.put("detalle", "usuatrio")
		json.put("data", notificacion)
		val url = "https://fcm.googleapis.com/fcm/send"
		val request = object : JsonObjectRequest(
			Method.POST, url, json,
			com.android.volley.Response.Listener { response ->
				Log.d("FCM_RESPONSE", "Response: $response")
			},
			com.android.volley.Response.ErrorListener { error ->
				Log.e("FCM_ERROR", "Error: ${error.message}")
			}
		) {
			override fun getHeaders(): MutableMap<String, String> {
				val headers: MutableMap<String, String> = HashMap()
				headers["content-type"] = "application/json"
				headers["authorization"] = "key=AAAAYZxEHaw:APA91bEiMfej-oGG4d1AZQy80bHBHGCEwUMvpQVPgj48J7WFvOAwu23Mz_VLoJqnfT7s9Gv3UdAIePwyA_o_KRjBEE2UsfhCfDduYljGQ_32Fd7j64i4ymlexVuBXD8Iu7tgFmDOPRog"
				return headers
			}
		}
		requestQueue.add(request)
	} catch (e: JSONException) {
		e.printStackTrace()
	}
}

fun enviars(context: Context, token: String) {
	val requestQueue = Volley.newRequestQueue(context)
	val json = JSONObject()
	try {
		json.put("to", token)
		val notificacion = JSONObject()
		notificacion.put("titulo", "hola")
		notificacion.put("detalle", "usuatrio")
		json.put("data", notificacion)
		val url = "https://fcm.googleapis.com/fcm/send"
		val request: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, json, null, null) {
			override fun getHeaders(): MutableMap<String, String> {
				val headers: MutableMap<String, String> = HashMap()
				headers["content-type"] = "application/json"
				headers["authorization"] = "key=AAAAYZxEHaw:APA91bEiMfej-oGG4d1AZQy80bHBHGCEwUMvpQVPgj48J7WFvOAwu23Mz_VLoJqnfT7s9Gv3UdAIePwyA_o_KRjBEE2UsfhCfDduYljGQ_32Fd7j64i4ymlexVuBXD8Iu7tgFmDOPRog"
				return headers
			}
		}
		requestQueue.add(request)
	} catch (e: JSONException) {
		e.printStackTrace()
	}
}

fun send(to: String?, body: String?): String? {
	try {
		val apiKey = "AAAAYZxEHaw:APA91bEiMfej-oGG4d1AZQy80bHBHGCEwUMvpQVPgj48J7WFvOAwu23Mz_VLoJqnfT7s9Gv3UdAIePwyA_o_KRjBEE2UsfhCfDduYljGQ_32Fd7j64i4ymlexVuBXD8Iu7tgFmDOPRog" // Reemplaza con tu clave de servidor de FCM

		val url = URL("https://fcm.googleapis.com/fcm/send")
		val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
		conn.setDoOutput(true)
		conn.setRequestMethod("POST")
		conn.setRequestProperty("Content-Type", "application/json")
		conn.setRequestProperty("Authorization", "key=$apiKey")
		conn.setDoOutput(true)
		val message = JSONObject()
		message.put("to", to)
		message.put("priority", "high")
		val notification = JSONObject()
		// notification.put("title", title);
		notification.put("body", body)
		message.put("data", notification)
		val os: OutputStream = conn.getOutputStream()
		os.write(message.toString().toByteArray())
		os.flush()
		os.close()
		val responseCode: Int = conn.getResponseCode()
		println("\nSending 'POST' request to URL : $url")
		println("Post parameters : $message")
		println("Response Code : $responseCode")
		System.out.println("Response Code : " + conn.getResponseMessage())
		val `in` = BufferedReader(InputStreamReader(conn.getInputStream()))
		var inputLine: String?
		val response = StringBuffer()
		while (`in`.readLine().also { inputLine = it } != null) {
			response.append(inputLine)
		}
		`in`.close()

		// print result
		println(response.toString())
		return response.toString()
	} catch (e: Exception) {
		e.printStackTrace()
	}
	return "error"
}