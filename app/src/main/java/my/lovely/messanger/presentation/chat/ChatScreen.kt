package my.lovely.messanger.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
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
                viewModel.connectToChat()
            } else if (event == Lifecycle.Event.ON_STOP) {
                viewModel.disconnect()
            }
        }
        lifecyclerOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecyclerOwner.lifecycle.removeObserver(observer)
        }

    }

    val state = viewModel.state.value


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
            items(state.message) {
                val isOwnMessage = it.userName == username
                Box(
                    contentAlignment = if (isOwnMessage) {
                        Alignment.CenterEnd
                    } else {
                        Alignment.CenterStart
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier
                        .width(200.dp)
                        .drawBehind {
                            val cornerRadius = 10.dp.toPx()
                            val triangleHeight = 20.dp.toPx()
                            val triangleWidth = 25.dp.toPx()
                            val trianglePath = Path().apply {
                                if (isOwnMessage) {
                                    moveTo(size.width, size.height - cornerRadius)
                                    lineTo(size.width, size.height + triangleHeight)
                                    lineTo(
                                        size.width - triangleWidth,
                                        size.height - cornerRadius
                                    )
                                    close()
                                } else {
                                    moveTo(0f, size.height - cornerRadius)
                                    lineTo(0f, size.height + triangleHeight)
                                    lineTo(triangleWidth, size.height - cornerRadius)
                                    close()
                                }
                            }
                            drawPath(
                                path = trianglePath,
                                color = if (isOwnMessage) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.DarkGray
                            )

                        }
                        .background(
                            color = if (isOwnMessage) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.DarkGray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp))

                    {
                        Text(
                            text = it.userName,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                        Text(
                            text = it.text,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                        Text(
                            text = it.formattedTime,
                            color = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.align(Alignment.End)
                        )

                    }
                }
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

            IconButton(onClick = viewModel::sendMessage) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")

            }

        }

    }


}