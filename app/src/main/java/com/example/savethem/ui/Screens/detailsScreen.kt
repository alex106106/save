package com.example.savethem.ui.Screens

import android.media.Rating
import android.util.Log
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.savethem.Model.CommentsModel
import com.example.savethem.R
import com.example.savethem.ViewModel.detailsViewModel
import com.example.savethem.ViewModel.mainViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun detailsScreen(id: String, mainViewModel: detailsViewModel) {
    val comm by mainViewModel.addComment.observeAsState()
    val commentList by mainViewModel.commentsList.collectAsState()
    val name by mainViewModel.name.collectAsState()
    val selectedCountry by mainViewModel.selectedMarker.collectAsState(null)
    var comment by remember { mutableStateOf("") }


    mainViewModel.getAllComments(id = id)
    mainViewModel.getUserData()

    LaunchedEffect(id) {
        mainViewModel.getCountryById(id)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.md_amber_100))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ){
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(3.dp),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(5.dp),
                    backgroundColor = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(id = R.color.md_amber_50)),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedCountry?.title ?: "",
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 30.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.josefinsanslight)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(end = 16.dp),
                                contentAlignment = Alignment.Center,
                                content = {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(8.dp),
                                        backgroundColor = Color.White
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color.LightGray,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .fillMaxSize()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val levelIcon = when (selectedCountry?.gLevel) {
                                                "BAJO" -> R.drawable.like
                                                "ALTO" -> R.drawable.send
                                                "MEDIO" -> R.drawable.user
                                                else -> R.drawable.turismo
                                            }
                                            Image(
                                                painter = painterResource(id = levelIcon),
                                                contentDescription = null,
                                                modifier = Modifier.size(64.dp)
                                            )
                                        }
                                    }
                                }
                            )
                        }



                        /* ************************************** */
                        /* ************************************** */
                        /* ************************************** */
                    }
                }
            }
            item {
                /* ************************************** */
                /* ******BODY****** */
                /* ************************************** */
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(3.dp),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(5.dp),
                    backgroundColor = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(id = R.color.md_amber_50)),
                        ) {
                    Column() {
//                        Rate the state
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.rate),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp) // Ajusta el tamaño del icono según sea necesario
                            )
                            Text(
                                text = "Rate the state",
                                fontFamily = FontFamily(Font(R.font.josefinsansbold)),
                                modifier = Modifier.padding(end = 8.dp) // Agrega espacio entre el texto y el icono
                            )
                        }
                            // Botones para la calificación
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    modifier = Modifier
                                        .weight(1f),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = colorResource(id = R.color.md_green_500),
                                        contentColor = Color.Black,
                                    ),
                                    onClick = {}
                                ) {
                                    Text(text = "Low")
                                }

                                Button(
                                    modifier = Modifier
                                        .weight(1f),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = colorResource(id = R.color.md_yellow_500),
                                        contentColor = Color.Black,
                                    ),
                                    onClick = {}
                                ) {
                                    Text(text = "Medium")
                                }

                                Button(
                                    modifier = Modifier
                                        .weight(1f),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = colorResource(id = R.color.md_red_500),
                                        contentColor = Color.Black,
                                    ),
                                    onClick = {}
                                ) {
                                    Text(text = "High")
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.crime),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp) // Ajusta el tamaño del icono según sea necesario
                                )
                                Text(
                                    text = "Crime Statistics",
                                    fontFamily = FontFamily(Font(R.font.josefinsansbold)),
                                    modifier = Modifier.padding(end = 8.dp) // Agrega espacio entre el texto y el icono
                                )
                            }
                            barras(mainViewModel)
                        }


                    }
                }
            }
            item {
                Box() {
                    Column(
                    ) {

                        Button(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.md_amber_500)
                            ),
                            onClick = { /* Acción al hacer clic en el botón */ },
//                            modifier = Modifier.padding(vertical = 8.dp) // Agrega espacio vertical alrededor del botón
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Become a journalist",
                                    fontFamily = FontFamily(Font(R.font.josefinsansbold)),
                                    modifier = Modifier.padding(end = 8.dp) // Agrega espacio entre el texto y el icono
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.journailst),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp) // Ajusta el tamaño del icono según sea necesario
                                )
                            }
                        }

                        Card(
                                modifier = Modifier
                                    .padding(6.dp),
                                elevation = 8.dp,
                                shape = RoundedCornerShape(5.dp),
                                backgroundColor = Color.White
                            ) {
                                Column(
                                    Modifier
                                        .padding(3.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.turismo),
                                        contentDescription = "maps"
                                    )
                                    Text(
                                        text = "News",
                                        textAlign = TextAlign.Center,
                                        fontSize = 35.sp,
                                        color = Color.Black,
                                        fontFamily = FontFamily(Font(R.font.josefinsansbold))
                                    )
                                }
                            }
                    }
                }
            }
        }

    }

    }

@Composable
fun RatingBar(
    maxStars: Int = 5,
    initialRating: Int = 0,
    onRatingChanged: (Int) -> Unit
) {
    var rating by remember { mutableStateOf(initialRating) }
    Row {
       repeat(maxStars){index ->
           val isSelected = index < rating
           val starIcon = if (isSelected) Icons.Filled.Star else Icons.Filled.Star
           val startColor = if (isSelected) Color.Yellow else Color.Gray
           Icon(
               imageVector = starIcon,
               contentDescription = null,
               tint = startColor,
                modifier = Modifier
                    .clickable {
                        rating = index + 1
                        onRatingChanged(rating)
                    }
                    .padding(4.dp)
           )
       }
    }
}
@Composable
fun start(){
    var currentRating by remember { mutableStateOf(0) }
    RatingBar(
        maxStars = 5,
        initialRating = currentRating,
        onRatingChanged = {newRating ->
            currentRating = newRating
        })
}

@Composable
fun list(id: String, mainViewModel: detailsViewModel) {
    val commentList by mainViewModel.commentsList.collectAsState()

    var visible by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var numCommentsToShow by remember { mutableStateOf(10) }

    AnimatedVisibility(
        visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Box {
            Column {
                commentList.take(numCommentsToShow).forEach { comments ->
                    // display each comment
                    Card(
                        modifier = Modifier
                            .padding(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 14.dp),
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color.White
                    ) {
                        Row {
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .align(Alignment.CenterVertically)
                                    .weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(2.dp)
                                ) {
                                    Text(
                                        text = comments.name.toString(),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth(1f)
                                    )
                                    Text(
                                        text = comments.content.toString(),
                                        fontWeight = FontWeight.Light,
                                        modifier = Modifier.fillMaxWidth(1f)
                                    )
                                }
                            }
                            val likesPair1 = mainViewModel.getLikes2(comments?.IDComments ?: "").observeAsState(initial = Pair(emptyList(), false)).value


                            // En el lugar donde se encuentra el botón
                            IconButton(onClick = {
                                val likesPair = likesPair1
                                val isLiked = likesPair.first.contains(FirebaseAuth.getInstance().currentUser?.uid) && likesPair.second
                                mainViewModel.addLike(
                                    comments?.IDComments ?: "",
                                    FirebaseAuth.getInstance().currentUser?.uid ?: "",
                                    !isLiked
                                )
                            }) {
                                val likesPair = mainViewModel.getLikes2(commentId = comments?.IDComments ?: "").observeAsState(initial = Pair(emptyList(), false)).value
                                val isLiked = likesPair.first.contains(FirebaseAuth.getInstance().currentUser?.uid) && likesPair.second
                                val iconImage = if (isLiked) R.drawable.like else R.drawable.unlike
                                val icon = painterResource(id = iconImage)
                                Icon(painter = icon, contentDescription = "like/unlike")
                            }

                            Text("${likesPair1.first.size}",
                                textAlign = TextAlign.Center)
                        }
                    }
                }
                if (numCommentsToShow < commentList.size) {

                    ClickableText(
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(color = Color.White, fontSize = 15.sp),
                        text = AnnotatedString("Show more"),
                        onClick = {
                            loading = true
                            // add a delay to simulate loading time
                            GlobalScope.launch {
                                delay(2000)
                                numCommentsToShow += 5
                                loading = false
                            }
                        }
                    )
                }
            }
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}


@Composable
fun barras(mainViewModel: detailsViewModel) {
    val selectedCountry by mainViewModel.selectedMarker.collectAsState(null)

    val femicide = selectedCountry?.femicide?.percentage?.toFloat()
    val homicide = selectedCountry?.homicide?.percentage?.toFloat()
    val kidnapping = selectedCountry?.kidnapping?.percentage?.toFloat()
    val rape = selectedCountry?.rape?.percentage?.toFloat()
    val vt = selectedCountry?.vehicleTheft?.percentage?.toFloat()


    val num: Double = 100.0
    val num2: Double = 50.0
    val datos= listOf(
        femicide?.let { Datos("Femicide", it) },
        homicide?.let { Datos("Homicide", it) },
        kidnapping?.let { Datos("Kidnapping", it) },
        rape?.let { Datos("Rape", it) },
        vt?.let { Datos("VT", it) },

        )
    var barras = ArrayList<BarChartData.Bar>()
    datos.mapIndexed { index, datos ->
        barras.add(
            BarChartData.Bar(
                label = datos?.label ?: "",
                value = datos?.value ?: 0f,
                color = Color.Blue
            )
        )
    }
    BarChart(barChartData = BarChartData(
        bars = barras
    ),
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 30.dp)
            .height(130.dp),
        labelDrawer = SimpleValueDrawer(
            drawLocation = SimpleValueDrawer.DrawLocation.XAxis
        )
    )
}

data class Datos(
    val label: String,
    val value: Float
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Comments(id: String, mainViewModel: detailsViewModel){
    val comm by mainViewModel.addComment.observeAsState()
    val name by mainViewModel.name.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
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
                    list(id = id, mainViewModel = mainViewModel)
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        var comment by remember { mutableStateOf("") }
                        var isButtonVisible by remember { mutableStateOf(false) }

                        Card() {
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
                                        AnimatedVisibility(
                                            visible = isButtonVisible,
                                            enter = fadeIn(),
                                            exit = fadeOut()
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    // Lógica del botón
                                                    mainViewModel.addComments(
                                                        id = id,
                                                        commentsModel = CommentsModel(
                                                            "",
                                                            "",
                                                            comm?.content ?: comment,
                                                            name = name
                                                        )
                                                    )
                                                    comment = "" // Vacía el texto del campo
                                                    isButtonVisible = false // Oculta el botón
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
        },
    sheetBackgroundColor = Color.Transparent,
    content = {
      Scaffold(
          topBar = { androidx.compose.material.TopAppBar(
              modifier = Modifier.height(25.dp),
              backgroundColor = colorResource(id = R.color.md_amber_50),
              elevation = 50.dp
          ) {

          }},
      content = {
        detailsScreen(id = id, mainViewModel = mainViewModel)
      },
      floatingActionButton = {
          FloatingActionButton(
              onClick = { scope.launch { sheetState.show() } },
                content = {
                    Icon(painterResource(
                        id = R.drawable.friends),
                        contentDescription = "Add friend")
                },
          backgroundColor = colorResource(id = R.color.md_amber_50))
      })
    })
}


@Composable
fun asddd(){
    Box(
        Modifier
            .background(colorResource(id = R.color.md_red_500))
            .fillMaxSize()
    ) {

    }
}