package com.example.savethem.ui.Screens

import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savethem.Model.*
import com.example.savethem.R
import com.example.savethem.ViewModel.FriendsViewModel
import com.example.savethem.ViewModel.mainViewModel
import com.example.savethem.call.boton
import com.example.savethem.navigation.Screens
import com.example.savethem.notification.createNotificationChannel
import com.example.savethem.notification.showSimpleNotificationWithTapAction
import com.example.savethem.util.constants.Notification.CHANNEL
import com.example.savethem.util.constants.Notification.ID
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun add(viewModel: mainViewModel, navController: NavController, friendsViewModel: FriendsViewModel) {

    Scaffold(
        topBar = { TopAppBar(friendsViewModel)},
        content = { MapView(viewModel = viewModel, navController = navController) },
        floatingActionButton = {

        }
    )





}
@Composable
fun list(viewModel: mainViewModel) {
    val markerInfoList by viewModel.markerInfoList.collectAsState(emptyList())
    var stateName by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current


    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            // Obtener el nombre del estado a partir de la ubicación actual
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addressList = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addressList?.isNotEmpty()!!) {
                stateName = addressList?.get(0).adminArea ?: ""
            }
        }
    }


    TextField(
        value = stateName,
        onValueChange = { stateName = it },
        label = { Text("search for location") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
    LazyColumn {
        items(markerInfoList) { feed ->
            Column {
                Card(
                    elevation = 7.dp,
                    shape = RoundedCornerShape(7.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 14.dp)
                ) {
                    Row {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = feed.title.toString(),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = feed.gLevel.toString(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = feed.position?.longitude.toString(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = feed.position?.latitude.toString(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                val gLevel = feed.gLevel.toString()
                if (stateName.isNotBlank() && feed.title!!.contains(stateName, ignoreCase = true)) {
                    if (gLevel == "ALTO") {
                        println("ALTO")
                    }
                }
            }
        }
    }

}



@Composable
fun MapView(viewModel: mainViewModel, navController: NavController) {
    val markerInfoList by viewModel.markerInfoList.collectAsState(emptyList())
    var isMapReady by remember { mutableStateOf(false) }
    var selectedMarkerId by remember { mutableStateOf("") }

    var stateName by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val channelId = CHANNEL
    val notificationId = ID
    var stopLoop by remember { mutableStateOf(false) }

    var selectedRiskLevel by remember { mutableStateOf("") }
    val riskLevels = listOf("ALTO", "MEDIO", "BAJO")
    var expanded by remember { mutableStateOf(false) }




    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            // Obtener el nombre del estado a partir de la ubicación actual
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addressList = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addressList?.isNotEmpty()!!) {
                stateName = addressList?.get(0).adminArea ?: ""
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.md_amber_100))
    ) {

        Column() {
            Box(modifier = Modifier.height(56.dp)) {
                // Crea el botón
                Button(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray, // Cambia el color de fondo del botón
                        contentColor = Color.White // Cambia el color del contenido del botón (texto/icono)
                    ),
                    content = {
                        Text(if (selectedRiskLevel.isEmpty()) "Select risk" else selectedRiskLevel)
                    }
                )

                // Crea el menú desplegable
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DropdownMenuItem(onClick = {
                        selectedRiskLevel = "ALTO"
                        expanded = false
                    }) {
                        Text("ALTO")
                    }
                    DropdownMenuItem(onClick = {
                        selectedRiskLevel = "MEDIO"
                        expanded = false
                    }) {
                        Text("MEDIO")
                    }
                    DropdownMenuItem(onClick = {
                        selectedRiskLevel = "BAJO"
                        expanded = false
                    }) {
                        Text("BAJO")
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(6.dp)
                        .weight(1f),
                    elevation = 5.dp,
                    shape = RoundedCornerShape(5.dp),
                    backgroundColor = Color.White
                ) {
                    Column(modifier = Modifier.
                    padding(start = 10.dp, bottom =  10.dp)) {
                        Text(text = "You are in:",
                            fontFamily = FontFamily(Font(R.font.josefinsansbold)),
                            color = Color.Black,
                            textAlign = TextAlign.Center)
                        Text(text = stateName,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.josefinsansitalicvariablefontwght)),
                            textAlign = TextAlign.Center)
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(6.dp)
                        .weight(1f),
                    elevation = 5.dp,
                    shape = RoundedCornerShape(5.dp),
                    backgroundColor = Color.White
                ) {
                    Column(modifier = Modifier.
                    padding( bottom =  10.dp)) {
                        Text(text = "You selected risk:",
                            fontFamily = FontFamily(Font(R.font.josefinsansbold)),
                            color = Color.Black,
                            textAlign = TextAlign.Center)

                        Text(text = selectedRiskLevel,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.josefinsansitalicvariablefontwght)),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 10.dp))
                    }
                }
            }


            // Filtra los marcadores en base al nivel de riesgo seleccionado
            val filteredMarkers = if (selectedRiskLevel.isNotEmpty()) {
                markerInfoList.filter { it.gLevel == selectedRiskLevel }
            } else {
                markerInfoList
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(50.dp)
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp),
                    elevation = 18.dp,
                    shape = RoundedCornerShape(25.dp),
                    backgroundColor = Color.Red
                ) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp),
                        onClick = { stopLoop = true },
                        enabled = !stopLoop
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(50.dp),
                            painter = painterResource(id = R.drawable.stop), contentDescription = "stop")
                    }
                }
            }

//            Button(
//                onClick = { stopLoop = true },
//                enabled = !stopLoop,
//                modifier = Modifier.padding(16.dp),
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = Color.DarkGray, // Cambia el color de fondo del botón
//                    contentColor = Color.White // Cambia el color del contenido del botón (texto/icono)
//                ),
//            ) {
//                Text("Detener alarma")
//            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                elevation = 18.dp,
                shape = RoundedCornerShape(25.dp),
                backgroundColor = Color.DarkGray
            ) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned {
                            isMapReady = true
                        },
                ) {
                    if (isMapReady) {

                        filteredMarkers.forEach { markerInfo ->
                            val gLevel = markerInfo.gLevel.toString()

                            // Filtrar markers en base al nivel de riesgo seleccionado
                            if (selectedRiskLevel.isNotEmpty() && gLevel != selectedRiskLevel) {
                                return@forEach // Saltar a la siguiente iteración
                            }

                            // Filtrar markers en base al estado del campo de texto
//                    if (stateName.isNotBlank() && !markerInfo.title!!.contains(stateName, ignoreCase = true)) {
//                        return@forEach // Saltar a la siguiente iteración
//                    }

                            LaunchedEffect(Unit) {
                                createNotificationChannel(channelId, context)
                                // Comprobar la coincidencia de valores en un bucle infinito
                                while (!stopLoop) {
                                    val textField1Value = stateName

                                    if (textField1Value.isNotEmpty() && textField1Value == markerInfo.title) {
                                        if (gLevel == "ALTO") {
                                            // Mostrar la notificación si hay una coincidencia
                                            showSimpleNotificationWithTapAction(
                                                context,
                                                channelId,
                                                notificationId,
                                                "You are in",
                                                "${markerInfo.title}"
                                            )
                                        }
                                    }
                                    delay(1000) // Esperar un segundo antes de comprobar la coincidencia de nuevo
                                }
                            }


                            Marker(
                                state = MarkerState(position = markerInfo.position!!),
                                title = markerInfo.title,
                                snippet = markerInfo.gLevel,
                                onClick = {
                                    selectedMarkerId = markerInfo.ID!!
                                    true
                                },
                                visible = selectedRiskLevel.isEmpty() || gLevel == selectedRiskLevel,
                            )

                        }
                    }
                }
            }


        }
    }
    LaunchedEffect(selectedMarkerId) {
        if (selectedMarkerId.isNotEmpty()) {
            navController.navigate(Screens.Details.route + "/$selectedMarkerId")
        }
    }

}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopAppBar(friendsViewModel: FriendsViewModel) {
    var searchText by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }
//    var idToFriend = ""

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSearchExpanded) {
                    AnimatedVisibility(
                        visible = isSearchExpanded,
                        enter = fadeIn() + slideInHorizontally(),
                        exit = fadeOut() + slideOutHorizontally()
                    ) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text(text = "Add") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                cursorColor = Color.Black,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    friendsViewModel.addFriend(registerModel(), searchText)
                                }
                            )
                        )
                    }
                } else {
                    Text(text = "NORM", color = Color.Black, fontSize = 16.sp)
                }
            }
        },
        actions = {
            IconButton(onClick = { isSearchExpanded = !isSearchExpanded }) {
                Icon(
                    imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }
        },
        modifier = Modifier.height(52.dp),
        backgroundColor = colorResource(id = R.color.md_amber_50),
        elevation = 0.dp
    )
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun friendList(viewModel: mainViewModel, navController: NavController, friendsViewModel: FriendsViewModel) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                // Contenido de la pantalla emergente
                Card(
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 30.dp, start = 14.dp, end = 14.dp),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        var id = ""
                        boton(friendsViewModel = friendsViewModel, navController)
                    }
                }
            },
            sheetBackgroundColor = Color.Transparent,

            content = {
                // Contenido de la pantalla principal
                Scaffold(
                    topBar = { TopAppBar(friendsViewModel) },
                    content = {
                        // Contenido principal
                        MapView(viewModel = viewModel, navController = navController)
                    },

                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { scope.launch { sheetState.show() } },
                            content = {
                                Icon(painterResource(
                                    id = R.drawable.friends),
                                    contentDescription = "Add friend")
                                      },
                            backgroundColor = Color.White,
                        )
                    }
                )
            }
        )
}






