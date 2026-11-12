package com.example.thestudents.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended

@Composable
fun ReviewQuote(
    modifier: Modifier = Modifier,
    content: String,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "\u201C",
            fontSize = 44.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.extended.rating
        )
        Text(
            text = content,
            fontSize = 15.sp,
            lineHeight = 24.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = maxLines,
            overflow = overflow,
            modifier = Modifier.testTag("reviewContent")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewQuotePreview() {
    TheStudentsTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ReviewQuote(content = "Excelente compañero, explica muy bien los temas complejos.")
        }
    }
}
