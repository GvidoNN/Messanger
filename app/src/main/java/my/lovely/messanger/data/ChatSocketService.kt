package my.lovely.messanger.data

import my.lovely.messanger.domain.Resource
import my.lovely.messanger.domain.model.Message

interface ChatSocketService {

    suspend fun initSession(
        userName: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    suspend fun deleteMessage(message: String)

    suspend fun observeMessages(): kotlinx.coroutines.flow.Flow<Message>

    suspend fun closeSession()

    companion object{
        const val BASE_URL = "ws://192.168.31.191:8080"
    }

    sealed class Endpoints(val url: String){
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}