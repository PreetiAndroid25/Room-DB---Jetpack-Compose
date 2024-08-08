package com.example.room

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.room.model.Note
import com.example.room.ui.theme.RoomDBTheme
import com.example.room.utils.Constant
import com.google.gson.Gson

class NoteDetailActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDBTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var note = Note()
                    if (intent.hasExtra(Constant.NOTE_OBJECT))
                    {
                        val noteJsonString = intent.getStringExtra(Constant.NOTE_OBJECT)
                        note = Gson().fromJson(noteJsonString, Note::class.java)
                    }
                    NoteDescription(note)
                }
            }
        }
    }
}

@Composable
fun NoteDescription(note: Note)
{
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        AppTopBar(title = note.noteTitle, onBackPressed = {
            (context as NoteDetailActivity).finish()
        })
        Text(text = note.noteDescription, modifier = Modifier.padding(horizontal = 25.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onBackPressed: () -> Unit
)
{
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        modifier = Modifier.background(Color.White),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2()
{
    RoomDBTheme {
        NoteDescription(Note())
    }
}