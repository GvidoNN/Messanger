package my.lovely.messanger.data.dto

import my.lovely.messanger.domain.model.Message
import java.text.DateFormat
import java.util.*

@kotlinx.serialization.Serializable
data class MessageDTO(
    val text: String,
    val timestamp: Long,
    val userName: String,
    val id: String
) {
    fun toMessage(): Message {
        val date = Date(timestamp)
        val formattedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(date)
        return Message(
            text = text,
            formattedTime = formattedDate,
            userName = userName,
            id = id
        )
    }
}
