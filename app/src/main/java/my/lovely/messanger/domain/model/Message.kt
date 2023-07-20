package my.lovely.messanger.domain.model

data class Message(
    val text: String,
    val formattedTime: String,
    val userName: String,
    val id: String
)
