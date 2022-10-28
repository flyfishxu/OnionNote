package com.flyfishxu.onionote.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    val context = LocalContext.current
    val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
    OnionNoteTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
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
            Greeting(scalingLazyListState, context)
        }
    }
}

@Composable
fun Greeting(state: ScalingLazyListState, context: Context) {
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
                    .fillMaxWidth()  ,
                strId = R.string.add_a_note,
                imageId =  R.drawable.ic_outline_add
            ) {
                context.startActivity(Intent(context, AddNoteActivity::class.java))
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
    MainApp()
}