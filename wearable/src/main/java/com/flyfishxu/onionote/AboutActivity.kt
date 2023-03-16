package com.flyfishxu.onionote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.flyfishxu.onionote.theme.OnionNoteTheme

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutApp()
        }
    }

    @Composable
    fun AboutApp() {
        val scalingLazyListState = rememberLazyListState()
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
                        PositionIndicator(lazyListState = scalingLazyListState)
                    }
                }
            ) {
                AboutList(scalingLazyListState)
            }
        }
    }

    @Composable
    fun AboutList(state: LazyListState) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    modifier = Modifier.padding(top = 32.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    text = stringResource(id = R.string.app_name)
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 12.dp),
                    text = stringResource(id = R.string.app_introduction)
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    color = Color.White,
                    text = stringResource(id = R.string.github_link)
                )
            }
            item {
                Image(
                    painter = painterResource(id = R.mipmap.ic_github_qr),
                    modifier = Modifier.padding(start = 48.dp, end = 48.dp, bottom = 32.dp, top = 12.dp),
                    contentDescription = "Github QR"
                )
            }
        }
    }

    @Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
    @Composable
    fun AboutPreview() {
        AboutApp()
    }
}