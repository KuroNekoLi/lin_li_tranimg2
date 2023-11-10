package com.example.lin_li_tranimg.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class EmailVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val atIndex = originalText.indexOf('@')

        if (atIndex == -1) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val firstPart = originalText.substring(0, atIndex)
        if (firstPart.length <= 2) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val transformedString = buildString {
            append(firstPart[0])
            repeat(firstPart.length - 2) { append('*') }
            append(firstPart.last())
            append(originalText.substring(atIndex))
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1 || offset >= atIndex) return offset
                return 2
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset >= transformedString.length - (originalText.length - atIndex)) {
                    return atIndex + (offset - (transformedString.length - (originalText.length - atIndex)))
                }
                return 2
            }
        }

        return TransformedText(AnnotatedString(transformedString), offsetMapping)
    }
}
