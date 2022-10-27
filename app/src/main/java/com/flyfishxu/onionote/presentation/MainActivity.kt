package com.flyfishxu.onionote.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.flyfishxu.onionote.R
import com.flyfishxu.onionote.presentation.theme.OnionNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
    OnionNoteTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
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
            Greeting(scalingLazyListState)
        }
    }
}

@Composable
fun Greeting(state: ScalingLazyListState) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        state = state,
    ) {
        item {
            ListChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                strId = R.string.add_a_note,
                imageId =  R.drawable.ic_outline_add
            ) {
                // Todo
            }
        }
        item {
            ListChip(
                modifier = Modifier
                    .fillMaxWidth(),
                strId = R.string.my_notes,
                imageId =  R.drawable.ic_outline_notes
            ) {
                // Todo
            }
        }
        item {
            ListChip(
                modifier = Modifier
                    .fillMaxWidth(),
                strId = R.string.app_settings,
                imageId =  R.drawable.ic_outline_settings
            ) {
                // Todo
            }
        }
        item {
            ListChip(
                modifier = Modifier
                    .fillMaxWidth(),
                strId = R.string.about,
                imageId =  R.drawable.ic_outline_info
            ) {
                // Todo
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
        colors = ChipDefaults.chipColors(Color.DarkGray)
    )
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}