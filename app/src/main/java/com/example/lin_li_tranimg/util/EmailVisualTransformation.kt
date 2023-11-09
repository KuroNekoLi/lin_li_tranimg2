package com.example.lin_li_tranimg.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class EmailVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val atIndex = originalText.indexOf('@')

        // 当不存在 @ 符号时，我们不执行任何隐藏操作
        if (atIndex == -1) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val firstPart = originalText.substring(0, atIndex)
        // 如果 @ 符号前只有两个或更少字符，不隐藏任何字符
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
                // 在 @ 符号前，除了第一个和最后一个字符，其他字符都被替换成一个星号
                if (offset <= 1 || offset >= atIndex) return offset
                return 2 // 所有中间的字符都被映射到第二个位置（第一个星号的位置）
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
