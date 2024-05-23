package com.example.savethem.ui.Screens

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savethem.Model.ChatModel
import com.example.savethem.Model.LatLngWrapper
import com.example.savethem.Model.LocationModel
import com.example.savethem.R
import com.example.savethem.ViewModel.ChatViewModel
import com.example.savethem.call.enviar
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ChatMainScreen(id: String, chatViewModel: ChatViewModel, navController: NavController) {
	Scaffold(
		topBar = { TopAppBarChat(id = id, chatViewModel = chatViewModel, navController)},
		content = { ChatScreen(id = id, chatViewModel = chatViewModel)}
	)
}

@Composable
fun TopAppBarChat(id: String, chatViewModel: ChatViewModel, navController: NavController) {
	val selectedFriend by chatViewModel.selectedFriend.collectAsState(null)
	chatViewModel.getUserData(id)


	androidx.compose.material.TopAppBar(
		navigationIcon = {
			IconButton(
				onClick = {
					navController.popBackStack()
				}) {
				Icon(painter = painterResource(id = R.drawable.arrowback), contentDescription = "")
			}
		},
		title = {
			Text(
				text = selectedFriend?.name.toString(),
				textAlign = TextAlign.End,

				)
		},
		actions = {
			IconButton(onClick = { /* Acción del segundo IconButton */ }) {
				Icon(painter = painterResource(id = R.drawable.menu), contentDescription = "")
			}
		},
		modifier = Modifier
			.height(52.dp),
		backgroundColor = colorResource(id = R.color.transparent),
		elevation = 0.dp
	)

}

@Composable
fun ChatScreen(id: String, chatViewModel: ChatViewModel) {
	val selectedFriend by chatViewModel.selectedFriend.collectAsState(null)
	val loc by chatViewModel.addMessage.collectAsState()
	val message by chatViewModel.addLocation.collectAsState(null)
	val location by  chatViewModel.locationById.collectAsState(null)
//	val idlocation = location?.IDMessage

	val asda = message?.forEach { message ->
		message.IDMessage
	}
	var idToFriend = FirebaseAuth.getInstance().currentUser?.uid
	chatViewModel.getLocation(idToFriend.toString(), id)
	chatViewModel.getAllMessage(idToFriend.toString(), id)
	chatViewModel.getUserData(id)
	val context = LocalContext.current
	LaunchedEffect(Unit){
		chatViewModel.getAllMessage(idToFriend.toString(), id)
	}
	LaunchedEffect(id){
		chatViewModel.friendID(id)
		val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
		fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
			if (location != null) {
				val latitude = location.latitude
				val longitude = location.longitude
				chatViewModel.locationID(id, idToFriend.toString(), latitude, longitude,context, selectedFriend?.token.toString())
			}
		}
	}
	var isButtonVisible by remember { mutableStateOf(false) }
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {

		Column() {


			message?.let {
				ExpandableCard(chatViewModel = chatViewModel, context)
			}

			ChatList(id = id, chatViewModel = chatViewModel, location = LatLngWrapper())
		}

	}
	var comment by remember { mutableStateOf("") }

	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.BottomCenter
	) {
		Card() {
			Column() {
				OutlinedTextField(
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 15.dp, end = 15.dp),
					value = comment,
					onValueChange = {
						comment = it
						isButtonVisible = it.isNotEmpty() // Muestra el botón solo si hay texto
					},
					label = { Text("Comment") },
					trailingIcon = {
						Row() {
							IconButton(
								onClick = {
									message?.forEach { message ->
										println("*********** ${location?.IDMessage.toString()}")
									}
									var idToFriend = FirebaseAuth.getInstance().currentUser?.uid
									val messageID = UUID.randomUUID().toString()
//										chatViewModel.addFriend(registerModel(),  selectedFriend?.UUID.toString())
									val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
									fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
										if (location != null) {
											val latitude = location.latitude
											val longitude = location.longitude

											if (latitude != 0.0 && longitude != 0.0) {
												val locationWrapper = LatLngWrapper(latitude, longitude)
												chatViewModel.addLocation(
													LocationModel(
														"",
														"",
														location = locationWrapper
													),
													selectedFriend?.UUID.toString(),
													messageID,
													idToFriend.toString()
												)
												chatViewModel.addLocation2(LocationModel(
													"",
													"",
													location = locationWrapper
												), idToFriend.toString(), selectedFriend?.UUID.toString(), messageID)
												chatViewModel.updateLocation(
													LocationModel(
														"",
														"",
														location = locationWrapper
													),
													id,
													idToFriend.toString(),
													messageID

												)
											} else {

											}

										} else {
										}
									}.addOnFailureListener { exception: Exception ->
									}
								}
							) {
								Icon(
									painter = painterResource(id = R.drawable.location),
									contentDescription = "SEND"
								)
							}
							val lat = LatLngWrapper(longitude = 0.0, latitude = 0.0)
							AnimatedVisibility(
								visible = isButtonVisible,
								enter = fadeIn(),
								exit = fadeOut()
							) {
								IconButton(
									onClick = {
										var idToFriend = FirebaseAuth.getInstance().currentUser?.uid
										val messageID = UUID.randomUUID().toString()
//										chatViewModel.addFriend(registerModel(),  selectedFriend?.UUID.toString())

										chatViewModel.addMessage(ChatModel(
											message = comment
										),selectedFriend?.UUID.toString(), messageID, idToFriend.toString())
										chatViewModel.addMessage2(ChatModel(
											message = comment
										), idToFriend.toString(), selectedFriend?.UUID.toString(), messageID)
//											comment = "" // Vacía el texto del campo
										isButtonVisible = true // Oculta el botón
//										enviar(context, token = selectedFriend?.token.toString())
//										println(selectedFriend?.token.toString())
//										println(selectedFriend?.token)
										FirebaseMessaging.getInstance().token
											.addOnCompleteListener { task ->
												if (task.isSuccessful) {
													val token = task.result

													Log.d("TOKEN del usuario", token)
												} else {
													Log.e("TOKEN del usuario", "Error al obtener el token: ${task.exception}")
												}
											}



									}
								) {
									Icon(
										painter = painterResource(id = R.drawable.send),
										contentDescription = "SEND"
									)
								}
							}
						}
					},
					colors = TextFieldDefaults.outlinedTextFieldColors(
						focusedBorderColor = Color.Black, // Cambia el color del borde cuando el campo está enfocado
						unfocusedBorderColor = Color.Gray,
						textColor = Color.White,
						cursorColor = Color.Black,
						placeholderColor = Color.Black// Cambia el color del borde cuando el campo no está enfocado
					)
				)
			}
		}
	}

}


@Composable
fun ChatList(id: String, chatViewModel: ChatViewModel, location: LatLngWrapper) {
	val message by chatViewModel.addMessage.collectAsState(null)


	// Almacena el estado de desplazamiento
	val scrollState = rememberScrollState()

	// Observa los cambios en la lista de mensajes
	LaunchedEffect(message) {
		// Si hay un nuevo mensaje, desplázate automáticamente al último mensaje
		if (message?.isNotEmpty() == true) {
			scrollState.animateScrollTo(scrollState.maxValue)
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.White)
			.verticalScroll(scrollState)
	) {
		val currentUser = FirebaseAuth.getInstance().currentUser





		message?.forEach { message ->

			val cardShape = if (message?.UUIDSender == currentUser?.uid) {
				RoundedCornerShape(topStart = 8.dp, topEnd = 18.dp, bottomStart = 18.dp)
			} else {
				RoundedCornerShape(topStart = 18.dp, topEnd = 8.dp, bottomEnd = 18.dp)
			}
			val pad = if (message?.UUIDSender == currentUser?.uid) {
				PaddingValues(top = 7.dp, bottom = 7.dp, start = 150.dp, end = 14.dp)
			} else {
				PaddingValues(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 150.dp)
			}
			Card(
				modifier = Modifier
					.padding(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 14.dp)
					.align(if (message?.UUIDSender == currentUser?.uid) Alignment.End else Alignment.Start),
				elevation = 8.dp,
				shape = cardShape,
				backgroundColor = if (message?.UUIDSender == currentUser?.uid) colorResource(id = R.color.BLUE) else colorResource(
					id = R.color.GREY
				)
			) {
				Column(
					modifier = Modifier
						.padding(5.dp)
				) {
					Text(
						text = message?.message.toString(),
						fontWeight = FontWeight.ExtraBold,
						color = Color.Black,
						textAlign = TextAlign.Start,
						modifier = Modifier
							.background(
								if (message?.UUIDSender == currentUser?.uid) colorResource(id = R.color.BLUE) else colorResource(
									id = R.color.GREY
								)
							)
					)
					val time = message?.timestamp ?: 0L
					val date = Date(time)
					val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
					val formattedTime = dateFormat.format(date)

					Text(
						text = formattedTime,
						fontWeight = FontWeight.Thin,
						color = Color.Black,
						textAlign = TextAlign.Start,
						style = TextStyle(color = Color.Black, fontSize = 10.sp),
						modifier = Modifier
							.background(
								if (message?.UUIDSender == currentUser?.uid) colorResource(id = R.color.BLUE) else colorResource(
									id = R.color.GREY
								)
							)
					)

				}
			}
		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard(chatViewModel: ChatViewModel, context: Context) {
	val message by chatViewModel.addLocation.collectAsState(null)
	var visible by remember { mutableStateOf(false) }
	var visiblea by remember { mutableStateOf(true) }
	var visibleb by remember { mutableStateOf(false) }

	Column() {

		AnimatedVisibility(
			visiblea,
			enter = expandVertically(expandFrom = Alignment.Top) { 20 },
			exit = shrinkVertically(animationSpec = tween()),
		) {
			Button(
				modifier = Modifier.fillMaxWidth(),
				colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
				onClick = {
					visible = !visible
					visiblea = !visiblea
					visibleb = !visibleb
				}
			) {
				Text(
					color = Color.Black,
					text = "MAPS"
				)
			}
		}
		AnimatedVisibility(
			visible,
			// Sets the initial height of the content to 20, revealing only the top of the content at
			// the beginning of the expanding animation.
			enter = expandVertically(expandFrom = Alignment.Top) { 20 },
			// Shrinks the content to half of its full height via an animation.
			exit = shrinkVertically(animationSpec = tween()),
		) {
			// Content that needs to appear/disappear
			Card(
				modifier = Modifier
					.padding(bottom = 90.dp, start = 14.dp, end = 14.dp),
				elevation = 8.dp,
				shape = RoundedCornerShape(8.dp),
				backgroundColor = Color.White
			) {
				val cameraPosition = rememberCameraPositionState()
				LaunchedEffect(message) {
					// Observe changes in 'message
					// Update 'cameraPosition' when the location changes
					message?.forEach { message ->
						message?.location.let { location ->
							if (location?.latitude != 0.0 && location?.longitude != 0.0) {
								val markerPosition =
									LatLng(location?.latitude!!, location.longitude!!)
								cameraPosition.position =
									CameraPosition.fromLatLngZoom(markerPosition, 15f)
							}
						}
					}
				}

				Column(
					modifier = Modifier.padding(10.dp)
				) {
					message?.forEach { message ->
						message?.location.let { location ->
							if (location?.latitude != 0.0 && location?.longitude != 0.0) {
								val markerPosition =
									LatLng(location?.latitude!!, location.longitude!!)
									GoogleMap(
										modifier = Modifier.fillMaxWidth(),
										cameraPositionState = cameraPosition
									) {
										val bitmap: Bitmap = BitmapFactory.decodeResource(
											context.resources, R.drawable.user)
										val desiredWidth = 110
										val desiredHeight = 110
										val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true)
										val icon: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
										Marker(
											state = MarkerState(position = markerPosition),
											icon = icon
										)
									}
							}
						}
					}
				}
				AnimatedVisibility(
					visible = visibleb,
					enter = expandVertically(expandFrom = Alignment.Top) { 20 },
					exit = shrinkVertically(animationSpec = tween()),
				) {
					Button(
						modifier = Modifier
							.fillMaxWidth()
							.padding(bottom = 130.dp),
						colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
						onClick = {
							visible = !visible
							visibleb = !visibleb
							visiblea = !visiblea
						}
					) {
						Text(
							color = Color.Red,
							text = "CLOSE MAP"
						)
					}
				}
			}
		}
	}
}






