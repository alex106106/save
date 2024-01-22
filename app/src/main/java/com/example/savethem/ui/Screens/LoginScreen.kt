package com.example.savethem.ui.Screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savethem.Model.registerModel
import com.example.savethem.R
import com.example.savethem.ViewModel.LoginViewModel


@Composable
fun loginScreen(loginViewModel: LoginViewModel, navController: NavController, context: Context) {
    var emailUser by remember { mutableStateOf("alexisgalindo106@gmail.com") }
    var passUser by remember { mutableStateOf("juniorniko106") }
    var confirmPass by remember { mutableStateOf("juniorniko106") }



    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, top = 30.dp)
                        .align(CenterHorizontally),
                    value = emailUser,
                    onValueChange = { emailUser = it },
                    label = { Text("Email") },

                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp)
                        .align(CenterHorizontally),
                    value = passUser,
                    onValueChange = { passUser = it },
                    label = { Text("Password") }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp)
                        .align(CenterHorizontally),
                    value = confirmPass,
                    onValueChange = { confirmPass = it },
                    label = { Text("Confirm Password") }
                )

                Button(
                    modifier = Modifier
                        .padding(15.dp)
                        .align(CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    onClick = {
                        if (emailUser.isNotEmpty() && passUser.isNotEmpty()) {
                            loginViewModel.login(
                                registerModel = registerModel(
                                    email = emailUser,
                                    pass = passUser,
                                    name = "",
                                    UUID = ""
                                ),
                                navController = navController,
                                context = context.applicationContext
                            )
                        } else {
                            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                        }
                        println("********************************************************************" +
                                "*************************ERROR EN BUTTON LOGIN**************************" +
                                "********************************************************************")
                    }
                ) {
                    if (loginViewModel.isLoading.value) {
                        CircularProgressIndicator(color = Color.Black)
                    } else {
                        Text("Login")
                    }
                }

                ClickableText(
                    modifier = Modifier
                        .padding(15.dp)
                        .align(CenterHorizontally),
                    style = TextStyle(color = Color.White, fontSize = 10.sp),
                    text = AnnotatedString("Don't have an account? Register"),
                    onClick = {
                        loginViewModel.navigateToRegister(navController)
                    }
                )
            }
        }
    }


}