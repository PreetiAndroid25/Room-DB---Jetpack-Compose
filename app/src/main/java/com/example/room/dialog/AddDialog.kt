package com.example.room.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.room.R

@Composable
fun AddDialog(
    onDismiss: () -> Unit = {},
    onConfirm: (noteTitle: String, noteDescription: String) -> Unit = { noteTitle, noteDescription -> },
    title: String = "",
    description: String = ""
)
{
    var noteTitle by remember { mutableStateOf(title) }
    var noteDescription by remember { mutableStateOf(description) }

    val dialogTitle = if (title.isNotEmpty() || description.isNotEmpty())
    {
        stringResource(R.string.text_update_your_note)
    } else
    {
        stringResource(R.string.text_add_your_note)
    }

    val buttonText = if (title.isNotEmpty() || description.isNotEmpty())
    {
        stringResource(R.string.text_update)
    } else
    {
        stringResource(R.string.text_add)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = dialogTitle) },
        text = {
            Column {
                TextField(
                    value = noteTitle,
                    onValueChange = { newText -> noteTitle = newText },
                    label = { Text(stringResource(R.string.text_title)) },
                    modifier = Modifier.fillMaxWidth(), maxLines = 2
                )
                TextField(
                    value = noteDescription,
                    onValueChange = { newText -> noteDescription = newText },
                    label = { Text(stringResource(R.string.text_description)) },
                    modifier = Modifier.fillMaxWidth(), maxLines = 7
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    noteTitle,
                    noteDescription
                )
            }) { // Pass the text value to the onConfirm callback
                Text(text = buttonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.text_dismiss))
            }
        }
    )
}
