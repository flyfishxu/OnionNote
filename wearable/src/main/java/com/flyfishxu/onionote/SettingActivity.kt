package com.flyfishxu.onionote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.flyfishxu.onionote.theme.OnionNoteTheme
import com.flyfishxu.onionote.theme.Purple200

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingApp()
        }
    }

    @Composable
    fun SettingApp() {
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
                SettingList(state = scalingLazyListState)
            }
        }
    }

    @Composable
    fun SettingList(state: ScalingLazyListState) {
        val checked  = remember { mutableStateOf(false) }
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            anchorType = ScalingLazyListAnchorType.ItemStart,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            state = state
        ) {
            item {
                SplitToggleChip(
                    modifier = Modifier.fillMaxWidth(),
                    checked = checked.value,
                    onCheckedChange = {checked.value = !checked.value},
                    label = {
                        Text(text = stringResource(id = R.string.sync_auto))
                    },
                    toggleControl = {
                        Switch(
                            checked = checked.value,
                            enabled = true,
                            colors = SwitchDefaults.colors(Purple200)
                        )
                    },
                    colors = ToggleChipDefaults.splitToggleChipColors(Color(0xFF202124)),
                    onClick = { checked.value = !checked.value }
                )
            }
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Column {
                            Text(text = stringResource(id = R.string.version))
                            Text(text = "0.0.1 Test", fontWeight = FontWeight.Light)
                        }
                    },
                    onClick = {  },
                    colors = ChipDefaults.chipColors(Color(0xFF202124))
                )
            }
        }
    }

    @Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
    @Composable
    fun SettingPreview() {
        SettingApp()
    }
}