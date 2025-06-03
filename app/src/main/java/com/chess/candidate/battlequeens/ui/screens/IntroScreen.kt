package com.chess.candidate.battlequeens.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun GetNumberOfQueens(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int?) -> Unit
) {
    var inputValue by remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column {
                    Text(
                        text = "Number of Queens: ",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = androidx.compose.ui.Modifier.padding(16.dp)
                    )
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { newValue ->
                            inputValue = newValue.filter { it.isDigit() }
                        },
                        label = { Text("4") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = androidx.compose.ui.Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = androidx.compose.ui.Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            val number = inputValue.toIntOrNull()
                            onConfirm(number)
                            onDismiss()
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun GetNumberOfQueensPreview() {
    GetNumberOfQueens(showDialog = true,
        onDismiss = {},
    onConfirm = {})
}