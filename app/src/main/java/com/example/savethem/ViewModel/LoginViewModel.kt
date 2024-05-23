package com.example.savethem.ViewModel

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.savethem.Model.registerModel
import com.example.savethem.navigation.Screens
import com.example.savethem.util.constants.const.AUTH
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val isUserLoggedIn = mutableStateOf<Boolean?>(null)

    init {
        checkIfLoggedIn()
    }

    private fun checkIfLoggedIn() {
        // This could be run on a background thread if needed
        viewModelScope.launch {
            isUserLoggedIn.value = firebaseAuth.currentUser != null
        }
    }

    fun login(registerModel: registerModel, navController: NavController) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                firebaseAuth.signInWithEmailAndPassword(registerModel.email!!, registerModel.pass!!).await()
                isUserLoggedIn.value = true
                navController.navigate("main_screen") {
                    popUpTo("login_screen") { inclusive = true }
                }

            } catch (e: Exception) {
                // Handle error (e.g., show a toast)
                isUserLoggedIn.value = false
            }

            _isLoading.value = false
        }
    }

    fun navigateToRegister(navController: NavController){
        navController.navigate(Screens.Register.route)
    }
}
