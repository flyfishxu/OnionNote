package com.flyfishxu.onionote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.wear.compose.material.*
import com.flyfishxu.onionote.database.Note
import com.flyfishxu.onionote.database.NoteDao
import com.flyfishxu.onionote.database.NoteDatabase
import com.flyfishxu.onionote.theme.OnionNoteTheme
import kotlinx.coroutines.*

class EditNoteActivity : ComponentActivity() {
    private lateinit var title: MutableState<String>
    private lateinit var content: MutableState<String>
    private lateinit var showDialog: MutableState<Boolean>
    private lateinit var note: Note
    private lateinit var noteDao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteId = intent.getIntExtra("noteId", -1)
        val db = Room.databaseBuilder(
            this,
            NoteDatabase::class.java, "notes"
        ).build()
        noteDao = db.noteDao()
        lifecycleScope.launch(Dispatchers.Default) {
            note = noteDao.getNoteById(noteId)
            withContext(Dispatchers.Main) {
                setContent {
                    showDialog = remember {
                        mutableStateOf(false)
                    }
                    if (showDialog.value)
                        InfoDialog(
                            title= stringResource(id = R.string.notice),
                            message = stringResource(id = R.string.sure_to_delete),
                            iconId = R.drawable.ic_outline_info,
                            showDialog = showDialog,
                            positive = {
                                lifecycleScope.launch(Dispatchers.Default) {
                                    noteDao.deleteNote(note)
                                    finish()
                                }
                                showDialog.value = false
                            },
                            negative = {
                                showDialog.value = false
                            })
                    else {
                        EditNoteApp()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch {
            note.title = title.value
            note.content = content.value
            note.updateDate = System.currentTimeMillis()
            noteDao.updateNote(note)
            this.cancel()
        }
    }

    @Composable
    fun EditNoteApp() {
        val lazyListState = rememberLazyListState()
        OnionNoteTheme {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                timeText = { TimeText() },
                vignette = {
                    Vignette(
                        vignettePosition =
                        VignettePosition.TopAndBottom
                    )
                },
                positionIndicator = {
                    if (lazyListState.isScrollInProgress) {
                        PositionIndicator(
                            lazyListState = lazyListState
                        )
                    }
                }
            ) {
                EditNote(lazyListState)
            }
        }
    }

    @Composable
    fun EditNote(state: LazyListState) {
        val titleText = note.title
        title = remember {
            mutableStateOf(titleText)
        }

        val noteText = note.content
        content  = remember {
            mutableStateOf(noteText)
        }

        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                BasicTextField(
                    value = title.value,
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    onValueChange = {
                        title.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp, 32.dp, 40.dp, 0.dp),
                    maxLines = 1,
                )
            }
            item {
                BasicTextField(
                    value = content.value,
                    textStyle = TextStyle(color = Color.White),
                    onValueChange = {
                        content.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp, 4.dp, 40.dp, 12.dp)
                )
            }
            item { 
                Button(
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = {
                        showDialog.value = true
                    }
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_baseline_delete), contentDescription = "Delete Note")
                }
            }
        }
    }

    @Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
    @Composable
    fun AddNotePreview() {
        note = Note(0, "Title", "Content", 0)
        EditNoteApp()
    }
}
