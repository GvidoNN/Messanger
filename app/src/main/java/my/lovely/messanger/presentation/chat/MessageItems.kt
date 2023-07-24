package my.lovely.messanger.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import my.lovely.messanger.domain.model.Message

@Composable
fun MessageItems(
    messageData: Message,
    isOwnMessage: Boolean,
    mainViewModel: ChatViewModel = viewModel()
) {
    Box(

        contentAlignment = if (isOwnMessage) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { Log.d("MyLog", "Clicl") },
                    onDoubleTap = { Log.d("MyLog", "Double Tap") },
                    onLongPress = {
                        mainViewModel.send(
                            event = Event.DeleteMessageEvent(
                                messageId = messageData.id
                            )
                        )
                    },
                    onTap = { Log.d("MyLog", "Tap") }
                )
            }
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
                    color = if (isOwnMessage) Color.Green else Color.DarkGray
                )

            }
            .background(
                color = if (isOwnMessage) Color.Green else Color.DarkGray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(8.dp))

        {
            Text(
                text = messageData.userName,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = messageData.text,
                color = Color.White
            )
            Text(
                text = messageData.formattedTime,
                color = Color.White,
                modifier = Modifier.align(Alignment.End)
            )


        }


    }

}