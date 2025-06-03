package com.chess.candidate.battlequeens.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString

class TextUtils {
    companion object {
        const val TAG = "TextUtils"

        fun addRedAsterisk(text: String): SpannableString {
            val asterisk = "*"
            val resultText = "$text$asterisk"
            val spannableString = SpannableString(resultText)
            spannableString.setSpan(
                ForegroundColorSpan(Color.Red.toArgb()),
                text.length,
                resultText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannableString
        }

        fun getRedAsteriskAnnotatedString(text: String) : AnnotatedString {
            val spannable = addRedAsterisk(text)
            val start = spannable.indexOf("*")
            val end = start + 1
            return AnnotatedString.Builder(spannable.toString())
                .apply {
                    addStyle(
                        style = androidx.compose.ui.text.SpanStyle(color = Color.Red),
                        start = start,
                        end = end
                    )
                }
                .toAnnotatedString()
        }
    }
}