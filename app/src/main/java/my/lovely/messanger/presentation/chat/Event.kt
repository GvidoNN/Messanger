package my.lovely.messanger.presentation.chat

sealed class Event{

    class DeleteMessageEvent(val messageId: String) : Event()

    object SendMessageEvent : Event()

    object GetAllMessages : Event()

    object ConnectToChat: Event()

    object DisconnectFromChat: Event()

}