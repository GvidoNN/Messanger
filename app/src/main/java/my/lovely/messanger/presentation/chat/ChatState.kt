package my.lovely.messanger.presentation.chat


data class ChatState(
    val message: List<my.lovely.messanger.domain.model.Message> = emptyList(),
    val isLoading: Boolean = false
)
