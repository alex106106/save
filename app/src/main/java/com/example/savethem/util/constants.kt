package com.example.savethem.util

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class constants {
    object Screens{
        const val LOGIN_SCREEN = "login_screen"
        const val MAIN_SCREEN = "main_screen"
        const val MAP_SCREEN = "map_screen"
        const val DETAILS_SCREEN = "details_screen"
        const val REGISTER_SCREEN = "register_screen"
        const val KEY_GAME_ID = "com.example.savethem"
        const val CALL_SCREEN = "call_screen"
        const val CHAT_SCREEN = "chat_screen"
//        val messageID = UUID.randomUUID().toString()
        val time = System.currentTimeMillis()
    }
    object Notification{
        const val CHANNEL = "channel"
        const val ID = 1
        const val EXTRA_MESSAGE = "extra_message"

        const val INTENT_ACTION_DETAILS = "OPEN_DETAILS"
    }
    object const{
        val AUTH = FirebaseAuth.getInstance()
    }
    object AppTheme {
        val Dark = darkColors(
            primary = Color.White,
            onPrimary = Color.Black,
            secondary = Color.Yellow,
            onSecondary = Color.Black,
            background = Color.Black,
            onBackground = Color.White
        )

        val Light = lightColors(
            primary = Color.Black,
            onPrimary = Color.White,
            secondary = Color.Yellow,
            onSecondary = Color.Black,
            background = Color.White,
            onBackground = Color.Black
        )
    }

}