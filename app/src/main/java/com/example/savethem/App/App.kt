package com.example.savethem.App

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        println("********************************************************************" +
//                "*************************ERROR EN APP**************************" +
//                "********************************************************************")
//        FirebaseApp.initializeApp(this)
//        println("********************************************************************" +
//                "*************************ERROR EN APP2**************************" +
//                "********************************************************************")
//        FirebaseApp.getInstance().persistenceKey
//        println("********************************************************************" +
//                "*************************ERROR EN APP 3**************************" +
//                "********************************************************************")
//        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
//        println("********************************************************************" +
//                "*************************ERROR EN LOGIN VIEW MODEL**************************" +
//                "********************************************************************")
//    }
}
//class activity : Activity(){
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//
//            try {
//                val account = task.getResult(ApiException::class.java)
//                println("SUCCESS ****************************************************")
//                // Autenticación exitosa, hacer algo con la cuenta de Google
//            } catch (e: ApiException) {
//                // Autenticación fallida, manejar el error
//                println("ERROR ****************************************************")
//
//            }
//        }
//
//    }
//}

