package my.lovely.messanger.presentation.userLogin

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor() : ViewModel() {

    private val _userNameText = mutableStateOf("")
    val userNameText: State<String> = _userNameText

    private val _onJoinChat = MutableSharedFlow<String>()
    val onJoinChat = _onJoinChat.asSharedFlow()

    fun onUserNameChange(userName: String){
        Log.d("MyLog","UserLoginViewModel onUserNameChange")
        _userNameText.value = userName
    }

    fun onJoinClick(){
        Log.d("MyLog","UserLoginViewModel onJoinClick")
        viewModelScope.launch(Dispatchers.IO) {
            if(userNameText.value.isNotBlank()) {
                _onJoinChat.emit(userNameText.value)
            }
        }
    }
}