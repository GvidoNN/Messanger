package my.lovely.messanger.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun DropDownMenu(expanded: Boolean, onExpandedChange: (Boolean) -> Unit) {
    val items = listOf("Item 1", "Item 2", "Item 3")
    Box(modifier = Modifier.wrapContentSize()) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            items.forEach { label ->
                DropdownMenuItem(onClick = {
                    onExpandedChange(false)
                }) {
                    Text(label)
                }
            }
        }
    }
}