package com.example.savethem.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savethem.Model.CommentsModel
import com.example.savethem.Model.MarkerModel
import com.example.savethem.Repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class detailsViewModel @Inject constructor(
    private val DAO: Repository
) : ViewModel() {

    private val _selectedMarker = MutableStateFlow<MarkerModel?>(null)
    val selectedMarker: StateFlow<MarkerModel?> = _selectedMarker.asStateFlow()



    private val _addComment = MutableLiveData<CommentsModel>()
    val addComment: LiveData<CommentsModel> = _addComment

    private val _comments = MutableStateFlow(emptyList<CommentsModel>())
    val commentsList: StateFlow<List<CommentsModel>> = _comments.asStateFlow()


    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name


    fun getAllComments(id: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val comments = DAO.getAllComments(id)
                _comments.value = comments
            }

        }
    }
    fun addComments(id: String, commentsModel: CommentsModel){
        viewModelScope.launch {
            val comment = DAO.
            addComment(commentsModel = commentsModel, id = id)
            withContext(Dispatchers.IO){
                comment.value?.let {
                    // Update the list of comments by appending the new comment
                    _comments.value = _comments.value + it
                    getAllComments(id) // Llamamos a la función que actualiza en tiempo real la lista de comentarios
                    comment.value!!.content = "" // Agregar esta línea para establecer la variable comment en una cadena vacía
                }
            }
            getAllComments(id)
        }
    }



    fun getCountryById(id: String){
        viewModelScope.launch {
            val marker = DAO.getCountryById(id)
            _selectedMarker.value = marker
            getAllComments(id)
        }
    }
    fun getUserData(){
        viewModelScope.launch {
            val userData = DAO.getUserData()
            userData?.let {
                _name.value = it.name.toString()
            }
        }
    }


    fun addLike(commentId: String, userId: String, liked: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DAO.addLike(commentId, userId, liked)
            }
        }
    }




    fun getLikes2(commentId: String): LiveData<Pair<List<String>, Boolean>> {
        val likesLiveData = MutableLiveData<Pair<List<String>, Boolean>>()
        val likesRef = FirebaseDatabase.getInstance().getReference("place/comments/$commentId/likes/")
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                likesRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val likes = mutableListOf<String>()
                        var currentUserLiked = false
                        for (likeSnap in snapshot.children) {
                            val value = likeSnap.getValue(Boolean::class.java)
                            if (value == true) {
                                likes.add(likeSnap.key!!)
                            }
                            if (likeSnap.key == FirebaseAuth.getInstance().currentUser?.uid) {
                                currentUserLiked = likeSnap.getValue(Boolean::class.java) ?: false
                            }
                            likesLiveData.value = Pair(likes, currentUserLiked)
                            Log.d("getLikes", "Likes: $likes - currentUserLiked: $currentUserLiked")

                        }
                        likesLiveData.value = Pair(likes, currentUserLiked)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejar el error
                    }
                })
            }
        }

        return likesLiveData
    }


}