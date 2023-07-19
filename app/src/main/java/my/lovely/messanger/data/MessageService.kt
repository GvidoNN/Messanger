package my.lovely.messanger.data

import my.lovely.messanger.domain.model.Message

interface MessageService {

    suspend fun getAllMessages(): List<Message>

    companion object{
        const val BASE_URL = "http://192.168.31.191:8080"
    }

    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$BASE_URL/messages")
    }

}