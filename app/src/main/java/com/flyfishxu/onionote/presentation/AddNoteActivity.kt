package com.flyfishxu.onionote.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
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
    
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    MainApp()
}