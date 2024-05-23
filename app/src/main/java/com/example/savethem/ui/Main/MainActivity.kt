package com.example.savethem.ui.Main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.savethem.Model.MarkerModel
import com.example.savethem.R
import com.example.savethem.ViewModel.*
import com.example.savethem.navigation.SetupNavHost
import com.example.savethem.ui.Screens.*
import com.example.savethem.ui.theme.SaveThemTheme
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<mainViewModel>()
        val RegisterVM by viewModels<RegisterViewModel> ()
        val LoginVM by viewModels<LoginViewModel> ()
        val DetailsVM by viewModels<detailsViewModel> ()
        val FriendsVM by viewModels<FriendsViewModel> ()
        val ChatVM  by viewModels<ChatViewModel>()
        var idToFriend = ""


        setContent {
            SaveThemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    AppContent()
//                    test()
                    val navController = rememberNavController()
                    SetupNavHost(
                        navHostController = navController,
                        viewModel = viewModel,
                    context = this,
                    loginViewModel = LoginVM,
                    registerViewModel = RegisterVM,
                    detailsViewModel = DetailsVM,
                    friendsViewModel = FriendsVM,
                    idToFriend = idToFriend,
                    chatViewModel = ChatVM)
//
//                    val serviceIntent = Intent(applicationContext, BackgroundService::class.java)
//                    applicationContext.startService(serviceIntent)
                }
            }
        }
    }

}




