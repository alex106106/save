package com.example.savethem.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savethem.Model.CommentsModel
import com.example.savethem.Model.MarkerModel
import com.example.savethem.Model.likeModel
import com.example.savethem.Repository.Repository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class mainViewModel @Inject constructor(
    private val DAO: Repository
) : ViewModel() {
    private val _markerInfoList = MutableStateFlow(emptyList<MarkerModel>())
    val markerInfoList: StateFlow<List<MarkerModel>> = _markerInfoList.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name


    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val marker = DAO.getAllCountries()
                _markerInfoList.value = marker
            }
        }
        }

    }


