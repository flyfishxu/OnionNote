package com.flyfishxu.onionote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import androidx.wear.compose.material.*
import com.flyfishxu.onionote.database.Note
import com.flyfishxu.onionote.database.NoteDatabase
import com.flyfishxu.onionote.theme.OnionNoteTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddNoteActivity : ComponentActivity() {
    private lateinit var title: MutableState<String>
    private lateinit var content: MutableState<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddNoteApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch {
            val db = Room.databaseBuilder(
                this@AddNoteActivity,
                NoteDatabase::class.java, "notes"
            ).build()
            val noteDao = db.noteDao()
            noteDao.insertNote(Note(null, title.value, content.value, System.currentTimeMillis()))
            this.cancel()
        }
    }

    @Composable
    fun AddNoteApp() {
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
                AddNote(lazyListState)
            }
        }
    }

    @Composable
    fun AddNote(state: LazyListState) {
        val titleText = stringResource(id = R.string.new_note_title)
        title = remember {
            mutableStateOf(titleText)
        }

        val noteText = stringResource(id = R.string.new_note_content)
        content  = remember {
            mutableStateOf(noteText)
        }

        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                BasicTextField(
                    value = title.value,
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    onValueChange = {
                        title.value = it
                    },
                    modifier = Modifier
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
                        .padding(40.dp, 4.dp, 40.dp, 40.dp)
                )
            }
        }
    }

    @Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
    @Composable
    fun AddNotePreview() {
        AddNoteApp()
    }
}
