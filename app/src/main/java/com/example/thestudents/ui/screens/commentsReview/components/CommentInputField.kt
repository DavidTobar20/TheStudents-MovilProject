package com.example.thestudents.ui.screens.commentsReview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

@Composable
fun CommentInputField(
    modifier: Modifier = Modifier,
    commentText: String,
    onCommentChange: (String) -> Unit
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = colorResource(R.color.sage),
                shape = RoundedCornerShape(20.5.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (commentText.isEmpty()) {
            Text(
                text = "Escribe un comentario...",
                color = colorResource(R.color.sage),
                fontSize = 14.sp
            )
        }

        BasicTextField(
            value = commentText,
            onValueChange = onCommentChange,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommentInputFieldPreview(){
    var commentText by remember { mutableStateOf("") }
    CommentInputField(
        commentText = commentText,
        onCommentChange = {commentText = it}
    )

}