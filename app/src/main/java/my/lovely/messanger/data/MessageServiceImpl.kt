package my.lovely.messanger.data

import io.ktor.client.*
import io.ktor.client.request.*
import my.lovely.messanger.data.dto.MessageDTO
import my.lovely.messanger.domain.model.Message

class MessageServiceImpl(
    private val client: HttpClient
) : MessageService {

    override suspend fun getAllMessages(): List<Message> {
        return try {
            client.get<List<MessageDTO>>(MessageService.Endpoints.GetAllMessages.url)
                .map{ it.toMessage() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}