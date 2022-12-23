package com.flyfishxu.onionote

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Dialog

@Composable
fun InfoDialog(
    title: String,
    message: String,
    iconId: Int,
    showDialog: MutableState<Boolean>,
    positive: () -> Unit,
    negative: () -> Unit
) {
    Dialog(
        showDialog = showDialog.value,
        onDismissRequest = {showDialog.value = false}
    ) {
        Alert(
            title = { Text(text = title) },
            message = { Text(text = message) },
            icon = {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "AlertIcon"
                )
            },
        ) {
            item {
                Row {
                    Button(onClick = negative, Modifier.padding(end = 8.dp)) {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_clear), contentDescription = "clear")
                    }
                    Button(onClick = positive, Modifier.padding(start = 8.dp)) {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_done), contentDescription = "done")
                    }
                }

            }
        }
    }
}