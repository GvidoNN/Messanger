package my.lovely.messanger.data

import android.util.Log
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.lovely.messanger.data.dto.MessageDTO
import my.lovely.messanger.domain.Resource
import my.lovely.messanger.domain.model.Message

class ChatSocketServiceImpl(
    private val client: HttpClient
) : ChatSocketService {

    private var socket: WebSocketSession? = null


    override suspend fun initSession(userName: String): Resource<Unit> {
        Log.d("MyLog","ChatSocketServiceImpl initSession")
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketService.Endpoints.ChatSocket.url}?username=$userName")
            }
            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Couldnt establish connection")
            }
        } catch (e: java.lang.Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun sendMessage(message: String) {
        Log.d("MyLog","ChatSocketServiceImpl sendMessage")
        try {
            socket?.send(Frame.Text(message))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun observeMessages(): Flow<Message> {
        Log.d("MyLog","ChatSocketServiceImpl observeMessages")
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto = Json.decodeFromString<MessageDTO>(json)
                    messageDto.toMessage()
                } ?: flow {  }
        } catch(e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun closeSession() {
        Log.d("MyLog","ChatSocketServiceImpl closeSession")
        socket?.close()
    }


}