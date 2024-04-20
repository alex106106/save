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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.blue
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
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.md_amber_50))
        ) {
            Column() {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 60.dp, bottom = 20.dp, end = 30.dp, start = 30.dp),
                    elevation = 18.dp,
                    shape = RoundedCornerShape(120.dp),
                    backgroundColor = colorResource(id = R.color.md_amber_50)
                ) {
                    Image(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentHeight()
//                            .padding(top = 60.dp, start = 20.dp),
                        painter = painterResource(
                            id = R.drawable.norma),
                        contentDescription = "")
                }


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()

                ) {

                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                    ) {

//                        Card(
//                            modifier = Modifier
//                                .padding(15.dp),
//                            shape = RoundedCornerShape(25.dp),
//                            elevation = 10.dp
//
//                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .background(
                                        colorResource(id = R.color.md_grey_200),
                                        shape = RoundedCornerShape(25.dp)
                                    ),
                                value = emailUser,
                                shape = RoundedCornerShape(25.dp),
                                onValueChange = { emailUser = it },
                                label = { Text(text = stringResource(id = R.string.email)) },
                            )
//                        }


//                        Card(
//                            modifier = Modifier
//                                .padding(15.dp),
//                            shape = RoundedCornerShape(25.dp),
//                            elevation = 10.dp
//
//                        ) {
                            OutlinedTextField(
//                                modifier = Modifier
//                                    .background(
//                                        colorResource(id = R.color.md_grey_200),
//                                        shape = RoundedCornerShape(25.dp)),
                                modifier = Modifier
                                    .padding(15.dp),
                                value = passUser,
                                shape = RoundedCornerShape(25.dp),
                                onValueChange = { passUser = it },
                                label = { Text(
                                    text = stringResource(id = R.string.password)) },
                                visualTransformation = PasswordVisualTransformation()
                            )
//                        }


//                        Card(
//                            modifier = Modifier
//                                .padding(15.dp),
//                            shape = RoundedCornerShape(25.dp),
//                            elevation = 10.dp
//
//                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .background(
                                        colorResource(id = R.color.md_grey_200),
                                        shape = RoundedCornerShape(25.dp)
                                    ),
                                value = confirmPass,
                                shape = RoundedCornerShape(25.dp),
                                onValueChange = { confirmPass = it },
                                label = { Text("Confirm Password") },
                                visualTransformation = PasswordVisualTransformation()
                            )
//                        }


                        Button(
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .align(CenterHorizontally),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.md_grey_200),
                                    contentColor = Color.Black,
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
//                            .padding(15.dp)
                                .align(CenterHorizontally),
                            style = TextStyle(color = Color.Black, fontSize = 10.sp),
                            text = AnnotatedString("Don't have an account? Register"),
                            onClick = {
                                loginViewModel.navigateToRegister(navController)
                            }
                        )
                    }

                }
            }
        }





}