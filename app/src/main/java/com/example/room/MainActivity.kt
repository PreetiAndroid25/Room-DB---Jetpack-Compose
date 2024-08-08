package com.example.room

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.room.dialog.AddDialog
import com.example.room.model.Note
import com.example.room.ui.theme.RoomDBTheme
import com.example.room.utils.Constant
import com.example.room.viewmodel.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDBTheme {
                val navController = rememberNavController()
                // Set up NavHost with NavController

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: NoteViewModel by viewModels()
                    viewModel.getNoteList()
                    val notes: List<Note> by viewModel.noteList.collectAsState()
                    MainScreen(viewModel, notes, LocalContext.current)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: NoteViewModel, notes: List<Note>, current: Context)
{
    val showAddDialog = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            //title(Header)
            Text(
                text = stringResource(R.string.text_my_notes),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )

            //Show note list
            if (notes.isNotEmpty())
            {
                NoteList(notes = notes, viewModel = viewModel, current)
            } else
            {
                EmptyView(viewModel)
            }

        }

        //Add Floating Button
        FloatingActionButton(
            onClick = {
                showAddDialog.value = true
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.text_add_your_note)
            )
        }
    }

    //show add dialog
    if (showAddDialog.value)
    {
        AddDialog(onDismiss = { showAddDialog.value = false },
            onConfirm = { noteTitle, noteDescription ->
                // Handle confirm action here
                showAddDialog.value = false
                viewModel.insertNote(Note(noteTitle = noteTitle, noteDescription = noteDescription))
            })
    }
}

@Composable
fun NoteList(notes: List<Note>, viewModel: NoteViewModel, current: Context)
{
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(count = notes.size, // Specify the count of items
            key = { index -> notes[index].id }, // Use a stable and unique key
            contentType = { null } // Content type, can be optimized if needed
        ) { index ->
            NoteItem(note = notes[index], viewModel, current)
        }
    }
}

@Composable
fun NoteItem(note: Note, viewModel: NoteViewModel, current: Context)
{
    var expanded by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.noteTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = note.noteDescription,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.text_edit)
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        viewModel.deleteNote(note)
                    }, text = {
                        Text(stringResource(R.string.text_delete))
                    })
                    DropdownMenuItem(onClick = {
                        expanded = false
                        showUpdateDialog = true
                    }, text = {
                        Text(stringResource(R.string.text_update))
                    })
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.text_view)) },
                        onClick = {
                            expanded = false
                            current.startActivity(Intent(
                                current, NoteDetailActivity::class.java
                            ).apply {
                                putExtra(Constant.NOTE_OBJECT, Gson().toJson(note))
                            })
                        })
                }
            }
        }
    }

    //show update dialog
    if (showUpdateDialog)
    {
        AddDialog(onDismiss = { showUpdateDialog = false },
            onConfirm = { noteTitle, noteDescription ->
                // Handle confirm action here
                showUpdateDialog = false
                viewModel.updateNote(
                    Note(
                        id = note.id, noteTitle = noteTitle, noteDescription = noteDescription
                    )
                )
            },
            title = note.noteTitle,
            description = note.noteDescription
        )
    }
}

@Composable
fun EmptyView(viewModel: NoteViewModel)
{
    val showAddDialog = remember { mutableStateOf(false) }

    //show add dialog
    if (showAddDialog.value)
    {
        AddDialog(onDismiss = { showAddDialog.value = false },
            onConfirm = { noteTitle, noteDescription ->
                // Handle confirm action here
                showAddDialog.value = false
                viewModel.insertNote(Note(noteTitle = noteTitle, noteDescription = noteDescription))
            })
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .clickable {
                showAddDialog.value = true
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.text_add),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(id = R.string.text_add_your_note))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview()
{/* RoomDBTheme {
         val viewModel = NoteViewModel()
         MainScreen(viewModel)
     }*/
}
