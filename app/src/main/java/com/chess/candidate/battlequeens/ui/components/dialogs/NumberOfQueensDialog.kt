package com.chess.candidate.battlequeens.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.utils.Constants.AppConstants.MINIMUM_NUMBER_QUEENS
import com.chess.candidate.battlequeens.ui.components.misc.CounterButton
import com.chess.candidate.battlequeens.ui.theme.PastelOrange
import com.chess.candidate.battlequeens.utils.Constants.AppConstants.MAXIMUM_NUMBER_QUEENS

@Composable
fun GetNumberOfQueensDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int?) -> Unit
) {
    AnimatedTransitionDialog(onDismissRequest = onDismiss) { animatedTransitionDialogHelper ->
        EnterNumberOfQueensSection(
            onConfirm = onConfirm,
            onDismiss = {
                animatedTransitionDialogHelper.triggerAnimatedDismiss()
                onDismiss()
            })
    }
}

@Composable
fun EnterNumberOfQueensSection(
    onDismiss: () -> Unit,
    onConfirm: (Int?) -> Unit
) {
    var inputValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val isError = remember(errorMessage) { errorMessage.isNotEmpty() }

    var valueCounter by remember {
        mutableStateOf(MINIMUM_NUMBER_QUEENS)
    }

    val title = stringResource(R.string.enter_number_of_queens_title)
    val errorMessageString =
        stringResource(R.string.error_number_entered, MINIMUM_NUMBER_QUEENS, MAXIMUM_NUMBER_QUEENS)
    Surface(shape = MaterialTheme.shapes.medium) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PastelOrange, //Color(0xFF00B0FF),
                    fontFamily = FontFamily.Serif
                ),
                modifier = androidx.compose.ui.Modifier
                    .padding(16.dp)
                    .semantics { contentDescription = title }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {

                CounterButton(
                    value = valueCounter.toString(),
                    onValueIncreaseClick = {
                        val amount = if (valueCounter < MAXIMUM_NUMBER_QUEENS) 1 else 0
                        valueCounter += amount
                    },
                    onValueDecreaseClick = {
                        val amount = if (valueCounter > MINIMUM_NUMBER_QUEENS) 1 else 0
                        valueCounter -= amount
                    },
                    onValueClearClick = {
                        valueCounter = 0
                    }
                )
            }

            if (isError) {
                Text(
                    text = if (isError) errorMessage else errorMessageString,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = if (isError) Color.Red else Color.Gray,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = androidx.compose.ui.Modifier.padding(8.dp)
                )
            }

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
                    if (valueCounter < MINIMUM_NUMBER_QUEENS) {
                        errorMessage = errorMessageString
                        return@TextButton
                    } else {
                        onConfirm(valueCounter)
                    }
                }) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
fun GetNumberOfQueensDialogPreview() {
    GetNumberOfQueensDialog(
        onDismiss = {},
        onConfirm = {}
    )
}