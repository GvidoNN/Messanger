package my.lovely.messanger.presentation.chat

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import my.lovely.messanger.data.ChatSocketService
import my.lovely.messanger.data.MessageService
import my.lovely.messanger.domain.Resource
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat() {
        Log.d("MyLog","ChatViewModel connectToChat")
        getAllMessages()
        savedStateHandle.get<String>("username")?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val result = chatSocketService.initSession(userName = it)
                when(result){
                    is Resource.Success -> {
                        chatSocketService.observeMessages().onEach { message ->
                            val newList = state.value.message.toMutableList().apply {
                                add(0, message)
                            }
                            _state.value = state.value.copy(
                                message = newList
                            )
                        }.launchIn(viewModelScope)
                    }
                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Unknown error")
                    }
                }

            }
        }
    }

    fun onMessageChange(message: String){
        Log.d("MyLog","ChatViewModel onMessageChange")
        _messageText.value = message
    }

    fun disconnect(){
        Log.d("MyLog","ChatViewModel disconnect")
        viewModelScope.launch(Dispatchers.IO) {
            chatSocketService.closeSession()
        }
    }

    fun sendMessage() {
        Log.d("MyLog","ChatViewModel sendMessage")
        viewModelScope.launch(Dispatchers.IO) {
            if(messageText.value.isNotBlank()){
                chatSocketService.sendMessage(messageText.value)
                _messageText.value = ""
            }
        }

    }

    fun getAllMessages(){
        Log.d("MyLog","ChatViewModel geAllMEssages")
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = state.value.copy(isLoading = true)
            val result = messageService.getAllMessages()
            _state.value = state.value.copy(message = result, isLoading = false)
        }
    }

    override fun onCleared() {
        Log.d("MyLog","ChatViewModel onCleared")
        super.onCleared()
        disconnect()
    }


}