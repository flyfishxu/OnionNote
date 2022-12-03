package com.flyfishxu.onionote.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.flyfishxu.onionote.presentation.theme.OnionNoteTheme
import me.chenhe.wearvision.dialog.AlertDialog

class AddNoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddNoteApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "back", Toast.LENGTH_LONG).show()
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
    val context = LocalContext.current
}

@Composable
fun EditNote() {
    var text by rememberSaveable { mutableStateOf("") }
    BasicTextField(
        value = text,
        textStyle = TextStyle(color = Color.White),
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp, 32.dp, 40.dp, 0.dp)
    )
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    AddNoteApp()
}
