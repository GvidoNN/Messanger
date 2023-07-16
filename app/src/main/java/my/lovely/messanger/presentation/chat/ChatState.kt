package my.lovely.messanger.presentation.chat

import android.os.Message

data class ChatState(
    val message: List<my.lovely.messanger.domain.model.Message> = emptyList(),
    val isLoading: Boolean = false
)
