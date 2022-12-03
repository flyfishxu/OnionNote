package com.flyfishxu.onionote.presentation

import android.app.AlertDialog
import android.content.ContextWrapper
import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.wear.compose.material.dialog.Alert
import com.flyfishxu.onionote.R
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
    BackHandler() {

    }
}

@Composable
fun Dialog() {
    Alert(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 24.dp, bottom = 52.dp),
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_info),
                contentDescription = "airplane",
                modifier = Modifier
                    .size(24.dp)
                    .wrapContentSize(align = Alignment.Center),
            )
        },
        title = { Text(text = "Example Title Text", textAlign = TextAlign.Center) },
        message = {
            Text(
                text = "Message content goes here " +
                        "(swipe right to dismiss)",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
        },
    ) {
        item {
            Chip(
                label = { Text("Primary") },
                onClick = { /* Do something e.g. navController.popBackStack() */ },
                colors = ChipDefaults.primaryChipColors(),
            )
        }
        item {
            Chip(
                label = { Text("Secondary") },
                onClick = { /* Do something e.g. navController.popBackStack() */ },
                colors = ChipDefaults.secondaryChipColors(),
            )
        }
    }
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
