package com.flyfishxu.onionote

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.flyfishxu.onionote.ui.theme.OnionNoteTheme
import kotlinx.coroutines.launch
import me.chenhe.lib.wearmsger.MessageHub

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            OnionNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Greeting() {
        val message = remember {
            mutableStateOf("")
        }
        Column {
            TextField(
                value = message.value,
                onValueChange = {
                    message.value = it
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp),
                label = { Text("Label") },
                singleLine = true,
                placeholder = { Text("example@gmail.com") }
            )
            Button(
                onClick = {
                    lifecycleScope.launch {
                        val result =
                            MessageHub.sendMessage(this@MainActivity, "/msg/test", message.value)
                        Log.i("SendMsg", result.toString())
                    }
                },
                modifier = Modifier.padding(16.dp, 4.dp)
            ) {
                Text(text = "Send")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        OnionNoteTheme {
            Greeting()
        }
    }
}