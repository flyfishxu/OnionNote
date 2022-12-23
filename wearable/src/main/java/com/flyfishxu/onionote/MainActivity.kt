package com.flyfishxu.onionote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.flyfishxu.onionote.theme.OnionNoteTheme
import com.google.android.gms.wearable.MessageEvent
import me.chenhe.lib.wearmsger.MessageHub
import me.chenhe.lib.wearmsger.listener.MessageListener

class MainActivity : ComponentActivity() {
    private val msgListener = object : MessageListener {
        override fun onMessageReceived(messageEvent: MessageEvent) {
            Toast.makeText(this@MainActivity, messageEvent.data.decodeToString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MessageHub.addMessageListener(this, msgListener)
        setContent {
            MainApp()
        }
    }

    @Composable
    fun MainApp() {
        val context = LocalContext.current
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
                MainAppList(scalingLazyListState, context)
            }
        }
    }

    @Composable
    fun MainAppList(state: ScalingLazyListState, context: Context) {
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
                    strId = R.string.new_note,
                    imageId = R.drawable.ic_outline_add
                ) {
                    context.startActivity(Intent(context, AddNoteActivity::class.java))
                }
            }
            item {
                ListChip(
                    modifier = Modifier
                        .fillMaxWidth(),
                    strId = R.string.my_notes,
                    imageId = R.drawable.ic_outline_notes
                ) {
                    context.startActivity(Intent(context, NoteListActivity::class.java))
                }
            }
            item {
                ListChip(
                    modifier = Modifier
                        .fillMaxWidth(),
                    strId = R.string.app_settings,
                    imageId = R.drawable.ic_outline_settings
                ) {
                    context.startActivity(Intent(context, SettingActivity::class.java))
                }
            }
            item {
                ListChip(
                    modifier = Modifier
                        .fillMaxWidth(),
                    strId = R.string.about,
                    imageId = R.drawable.ic_outline_info
                ) {
                    context.startActivity(Intent(context, AboutActivity::class.java))
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
    fun MainPreview() {
        MainApp()
    }
}