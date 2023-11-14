package com.example.lin_li_tranimg.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * 遮蔽使用者的電子郵件
 * 這個轉換保留了電子郵件的第一個和最後一個字符，並將 "@" 符號之前的其他字符替換為星號 (*)。
 */
class EmailVisualTransformation : VisualTransformation {
    /**
     * 覆寫 filter 方法來定義文本的轉換規則。
     *
     * @param text AnnotatedString 輸入的原始文本。
     * @return TransformedText 轉換後的文本與偏移映射。
     */
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val atIndex = originalText.indexOf('@')
        // 如果文本中不包含 '@'，則不進行轉換。
        if (atIndex == -1) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        // 如果 '@' 之前的文本長度小於或等於 2，則不進行轉換。
        val firstPart = originalText.substring(0, atIndex)
        if (firstPart.length <= 2) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        // 建立轉換後的字符串，只保留第一個和最後一個字符，其他用 '*' 替換。
        val transformedString = buildString {
            append(firstPart[0])
            repeat(firstPart.length - 2) { append('*') }
            append(firstPart.last())
            append(originalText.substring(atIndex))
        }
        // 定義偏移映射，用於處理用戶互動時的光標位置轉換。
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
