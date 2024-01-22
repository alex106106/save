package com.example.savethem.ViewModel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savethem.Model.ChatLocationModel
import com.example.savethem.Model.ChatModel
import com.example.savethem.Model.LocationModel
import com.example.savethem.Model.registerModel
import com.example.savethem.Repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
	private val DAO: Repository
): ViewModel(){

	private val _selectedFriend = MutableStateFlow<registerModel?>(null)
	val selectedFriend: StateFlow<registerModel?> = _selectedFriend.asStateFlow()

	private val _addMessage = MutableStateFlow(emptyList<ChatModel>())
	val addMessage: StateFlow<List<ChatModel>> = _addMessage.asStateFlow()

	private val _addLocation = MutableStateFlow(emptyList<LocationModel>())
	val addLocation: StateFlow<List<LocationModel>> = _addLocation.asStateFlow()

	private val _locationById = MutableStateFlow<LocationModel?>(null)
	val locationById: StateFlow<LocationModel?> = _locationById.asStateFlow()

	private val _updateUserData = MutableStateFlow<registerModel?>(null)
	val updateUserData: StateFlow<registerModel?> = _updateUserData.asStateFlow()

	private val _updateLocationResult = MediatorLiveData<LocationModel>()
	val updateLocationResult: LiveData<LocationModel> = _updateLocationResult


//	private val _addLocation = MutableStateFlow(emptyList<ChatLocationModel>())
//	val addLocation: StateFlow<List<ChatLocationModel>> = _addLocation.asStateFlow()





	private val _name = MutableStateFlow("")
	val name: StateFlow<String> = _name

	fun friendID(id: String){
		viewModelScope.launch {
			withContext(Dispatchers.IO){
				val friend = DAO.getFriendById(id)
				_selectedFriend.value = friend
			}
		}
	}
	fun getUserData(idFriend: String){
		viewModelScope.launch {
			val userData = DAO.getFriendData(idFriend)
			_name.value = userData.toString()
		}
	}

	fun addMessage(addMessage: ChatModel, id: String, messageID: String, idUser: String){
		viewModelScope.launch {
			val message = DAO.addMessage(addMessage, id, messageID)
			withContext(Dispatchers.IO){
				val comments = DAO.getLocation(idUser,id)
				_name.value = comments.toString()
			}
//			getAllMessage(idUser, id)
		}
	}
	fun addLocation(addMessage: LocationModel, id: String, messageID: String, idUser: String){
		viewModelScope.launch {
			val message = DAO.addLocation(addMessage, id, messageID)
			withContext(Dispatchers.IO){
				message.value?.let {
					_addLocation.value = _addLocation.value + it
					getAllMessage(idUser,id)
					message.value!!
				}
			}
			getAllMessage(idUser, id)
		}
	}
	fun addLocation2(addMessage: LocationModel, id: String, idUser: String, messageID: String){
		viewModelScope.launch {
			val message = DAO.addLocation2(addMessage, id, idUser, messageID)
			withContext(Dispatchers.IO){
				message.value?.let {
					_addLocation.value = _addLocation.value + it
					getAllMessage(idUser, id)
					message.value!!
				}
			}
			getAllMessage(idUser, id)
		}
	}
	fun updateLocation(addMessage: LocationModel, id: String, idUser: String, messageID: String) {
		viewModelScope.launch {
			val result = DAO.updateLocation(addMessage, id, idUser, messageID)
			_updateLocationResult.addSource(result) { updatedLocation ->
				_updateLocationResult.value = updatedLocation
			}
		}
	}
	fun getLocation(idUser: String, id: String) {
		viewModelScope.launch {
			withContext(Dispatchers.IO){
				DAO.getLocation(idUser, id).collect { messages ->
					_addLocation.value = messages
				}
			}

		}
	}
	fun locationID(idUser: String, id: String, latitude: Double, longitude: Double, context: Context,
	               token: String){
		viewModelScope.launch {
			withContext(Dispatchers.IO){
				val location = DAO.getLocationById(idUser, id, latitude, longitude, context, token)
				_locationById.value = location
			}
		}
	}
	fun getUserDataUpdate(idUser: String, id: String, token: String){
		viewModelScope.launch {
			withContext(Dispatchers.IO){
				val location = DAO.getUserDataUpdate(idUser, id, token)
				_updateUserData.value = location
			}
		}
	}
	fun addMessage2(addMessage: ChatModel, id: String, idUser: String, messageID: String){
		viewModelScope.launch {
			val message = DAO.addMessage2(addMessage, id, idUser, messageID)
			withContext(Dispatchers.IO){
				message.value?.let {
					_addMessage.value = _addMessage.value + it
					getAllMessage(idUser, id)
					message.value!!.message = "jhgffghjk"
				}
			}
			getAllMessage(idUser, id)
		}
	}
	fun getAllMessage(idUser: String, id: String) {
		viewModelScope.launch {
			withContext(Dispatchers.IO){
				DAO.getAllMessage(idUser, id).collect { messages ->
					_addMessage.value = messages
				}
			}

		}
	}

	fun getAllMessage2(id: String, idUser: String) {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				val message = DAO.getAllMessage2(id,idUser) // Obtener mensajes del usuario receptor
				_addMessage.value = message
			}
		}
	}
	private val _friends = MutableStateFlow(emptyList<registerModel>())
	val friends: StateFlow<List<registerModel>> = _friends.asStateFlow()

	fun addFriend(addFriend: registerModel, uid: String){
		viewModelScope.launch {
			val add = DAO.addFriendChat(addFriend, uid)
			add
		}
	}

}