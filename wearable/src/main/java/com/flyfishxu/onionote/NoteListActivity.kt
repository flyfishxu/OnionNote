package com.flyfishxu.onionote

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.wear.compose.material.*
import androidx.wear.compose.material.dialog.Dialog
import com.flyfishxu.onionote.database.Note
import com.flyfishxu.onionote.database.NoteDao
import com.flyfishxu.onionote.database.NoteDatabase
import com.flyfishxu.onionote.theme.OnionNoteTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NoteListActivity : ComponentActivity() {
    private lateinit var showDialog: MutableState<Boolean>
    private lateinit var showMenu: MutableState<Boolean>
    private lateinit var items: MutableState<List<Note>>
    private lateinit var noteDao: NoteDao
    private lateinit var selectedNote: Note
    private lateinit var sp: SharedPreferences
    private var firstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var innerItems: List<Note>
        sp = getSharedPreferences("data", MODE_PRIVATE)
        lifecycleScope.launch(Dispatchers.Default) {
            val db = Room.databaseBuilder(
                this@NoteListActivity,
                NoteDatabase::class.java, "notes"
            ).build()
            noteDao = db.noteDao()
            innerItems = noteDao.getAll()
            withContext(Dispatchers.Main) {
                setContent {
                    items = remember {
                        mutableStateOf(innerItems)
                    }
                    showDialog = remember {
                        mutableStateOf(false)
                    }
                    showMenu = remember {
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
                                    val newItems = mutableListOf<Note>()
                                    for (i in items.value) {
                                        if (i.id != selectedNote.id) newItems.add(i)
                                    }
                                    items.value = newItems
                                    if (sp.getInt("tileNoteId", -1) == selectedNote.id) {
                                        val spEditor = sp.edit()
                                        spEditor.putInt("tileNoteId", -1)
                                        spEditor.apply()
                                    }
                                    noteDao.deleteNote(selectedNote)
                                }
                                showDialog.value = false
                            },
                            negative = {
                                showDialog.value = false
                            })
                    else {
                        if (showMenu.value) MenuApp()
                        else NoteListApp()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        firstLaunch = false
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.Default) {
            delay(500)
            if (!firstLaunch) items.value = noteDao.getAll()
        }
    }

    @Composable
    fun ThereIsNothing() {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            anchorType = ScalingLazyListAnchorType.ItemCenter
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.there_is_nothing),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun NoteListApp() {
        val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
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
                    if (scalingLazyListState.isScrollInProgress) {
                        PositionIndicator(
                            scalingLazyListState =
                            scalingLazyListState
                        )
                    }
                }
            ) {
                if (items.value.isEmpty()) ThereIsNothing()
                else NoteList(scalingLazyListState)
            }
        }
    }

    @Composable
    fun NoteList(state: ScalingLazyListState) {
        val listAnchorType =
            if (items.value.size == 1) ScalingLazyListAnchorType.ItemCenter
            else ScalingLazyListAnchorType.ItemStart
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            anchorType = listAnchorType,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            state = state
        ) {
            items(items.value) {
                ListChip(it) {
                    val intent = Intent(this@NoteListActivity, EditNoteActivity::class.java)
                    intent.putExtra("noteId", it.id)
                    this@NoteListActivity.startActivity(intent)
                }
            }
        }
    }

    @Composable
    fun ListChip(note: Note, click: () -> Unit) {
        Chip(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDrag = { _, _ -> },
                        onDragStart = {
                            selectedNote = note
                            showMenu.value = true
                        }
                    )
                },
            label = {
                Column {
                    Text(text = note.title)
                    Text(text = note.content, fontWeight = FontWeight.Light)
                }
            },
            onClick = click,
            colors = ChipDefaults.chipColors(Color(0xFF202124))
        )
    }

    @Composable
    fun MenuApp() {
        val scalingLazyListState = rememberScalingLazyListState()
        Dialog(
            showDialog = showMenu.value,
            onDismissRequest = { showMenu.value = false }
        ) {
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
                        if (scalingLazyListState.isScrollInProgress) {
                            PositionIndicator(
                                scalingLazyListState =
                                scalingLazyListState
                            )
                        }
                    }
                ) {
                    MenuList(scalingLazyListState)
                }
            }
        }
    }

    @Composable
    fun MenuList(state: ScalingLazyListState) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            anchorType = ScalingLazyListAnchorType.ItemStart,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            state = state,
        ) {
            item {
                ListChip(
                    modifier = Modifier
                        .fillMaxWidth(),
                    strId = R.string.set_to_tile,
                    imageId = R.drawable.ic_outline_watch
                ) {
                    val spEditor = sp.edit()
                    spEditor.putInt("tileNoteId", selectedNote.id!!)
                    spEditor.apply()
                    Toast.makeText(this@NoteListActivity, getString(R.string.set_to_tile_successfully), Toast.LENGTH_LONG).show()
                    showMenu.value = false
                }
            }
            item {
                ListChip(
                    modifier = Modifier
                        .fillMaxWidth(),
                    strId = R.string.delete_note,
                    imageId = R.drawable.ic_baseline_delete
                ) {
                    showMenu.value = false
                    showDialog.value = true
                }
            }
        }
    }

    @Composable
    fun ListChip(modifier: Modifier = Modifier, strId: Int, imageId: Int, click: () -> Unit) {
        Chip(
            modifier = modifier,
            label = {
                Text(text = stringResource(id = strId))
            },
            icon = {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = stringResource(id = strId)
                )
            },
            onClick = click,
            colors = ChipDefaults.chipColors(Color(0xFF202124))
        )
    }

    @Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
    @Composable
    fun NoteListPreview() {
        val note1 = Note(0, "This is example title", "This is example content",0)
        val note2 = Note(1, "This is example title", "This is example content",1)
        showDialog = remember { mutableStateOf(false) }
        items = remember { mutableStateOf(listOf(note1, note2)) }
        selectedNote = note1
        NoteListApp()
    }

    @Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
    @Composable
    fun MenuPreview() {
        MenuApp()
    }
}