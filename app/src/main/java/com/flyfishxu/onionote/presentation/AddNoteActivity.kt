package com.flyfishxu.onionote.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.flyfishxu.onionote.presentation.theme.OnionNoteTheme

class AddNoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddNoteApp()
        }
    }
}

@Composable
fun AddNoteApp() {
    val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
    OnionNoteTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            timeText = { TimeText() },
            vignette = {
                Vignette (vignettePosition =
                VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                if (scalingLazyListState.isScrollInProgress) {
                    PositionIndicator(scalingLazyListState =
                    scalingLazyListState)
                }
            }
        ) {
            EditNote()
        }
    }
}

@Composable
fun EditNote() {
    var text by rememberSaveable { mutableStateOf("abcdefg") }
    BasicTextField(
        value = text,
        textStyle = TextStyle(color = Color.White),
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxSize()
            .padding(36.dp, 28.dp, 36.dp, 0.dp)
    )
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    AddNoteApp()
}