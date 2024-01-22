package com.example.savethem.ViewModel

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.savethem.Model.registerModel
import com.example.savethem.navigation.Screens
import com.example.savethem.util.constants.const.AUTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewModel() : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun login(registerModel: registerModel, navController: NavController, context: Context) {
        // ...

        viewModelScope.launch {
            _isLoading.value = true

            try {
                // Realiza la autenticación de Firebase Realtime
                    AUTH.signInWithEmailAndPassword(registerModel.email!!, registerModel.pass!!).await()

 2
                // Redirige al usuario a la pantalla principal
                navController.navigate("main_screen")
            } catch (e: Exception) {
                // Muestra un mensaje de error en caso de fallo
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }

            _isLoading.value = false
        }
    }


    fun navigateToRegister(navController: NavController){
        navController.navigate(Screens.Register.route)
    }
}