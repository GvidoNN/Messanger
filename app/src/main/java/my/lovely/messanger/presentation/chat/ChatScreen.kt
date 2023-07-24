package my.lovely.messanger.presentation.chat

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    username: String?,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    val lifecyclerOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecyclerOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.send(event = Event.ConnectToChat)
            } else if (event == Lifecycle.Event.ON_STOP) {
                viewModel.send(event = Event.DisconnectFromChat)
            }
        }
        lifecyclerOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecyclerOwner.lifecycle.removeObserver(observer)
        }

    }

    val state = viewModel.state.value

    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Item 1", "Item 2", "Item 3")
    var showMenu by remember { mutableStateOf(false) }
    var menuState by remember { mutableStateOf(false) }


    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(state.message) { message ->
                val isOwnMessage = message.userName == username
                MessageItems(
                    messageData = message,
                    isOwnMessage = isOwnMessage,
                    mainViewModel = viewModel
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = viewModel.messageText.value,
                onValueChange = viewModel::onMessageChange,
                placeholder = {
                    Text("Enter a message")
                },
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { viewModel.send(Event.SendMessageEvent) }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")

            }

            IconButton(onClick = {
                if(showMenu && menuState){
                    showMenu = false
                    menuState = false
                    Log.d("MyLog","Закрываем")
                } else {
                    showMenu = true
                    menuState = true
                    Log.d("MyLog","Открываем")
                }

            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Attach")
            }

            if (showMenu) {
                Log.d("MyLog", "Вылазий")
                DropDownMenu()
            }
        }

    }


}

