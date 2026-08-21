package com.example.thestudents.ui.screens.commentsReview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.commentsReview.components.CommentInputFieldBar
import com.example.thestudents.ui.screens.commentsReview.components.CommentItem
import com.example.thestudents.ui.screens.commentsReview.components.CommentsTopAppBar
import com.example.thestudents.ui.screens.commentsReview.components.ReviewCard
import com.example.thestudents.ui.theme.TheStudentsTheme

/**
 * Contenido de los comentarios de una resena.
 *
 * La cabecera y la caja de comentario van dentro del contenido, no como topBar y bottomBar de un
 * Scaffold propio: el unico Scaffold de la app esta en AppNavigation. La lista de comentarios
 * toma el espacio sobrante con weight, asi que la caja de escribir queda anclada abajo igual que
 * antes.
 */
@Composable
fun BodyCommentsReviewScreen(
    initialCommentator: String,
    review: Review,
    onBackClick: () -> Unit,
    isLiked: Boolean,
    isDisliked: Boolean,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    likedComments: Set<Int>,
    dislikedComments: Set<Int>,
    onCommentLikeClick: (Int) -> Unit,
    onCommentDislikeClick: (Int) -> Unit,
    commentInputText: String,
    onCommentTextChange: (String) -> Unit,
    onSendCommentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CommentsTopAppBar(onBackClick = onBackClick)

            ReviewCard(
                review = review,
                isLiked = isLiked,
                isDisliked = isDisliked,
                onLikeClick = onLikeClick,
                onDislikeClick = onDislikeClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Text(
                text = stringResource(R.string.comentarios, review.comments.size),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(review.comments) { index, comment ->
                    CommentItem(
                        comment = comment,
                        isLiked = index in likedComments,
                        isDisliked = index in dislikedComments,
                        onLikeClick = { onCommentLikeClick(index) },
                        onDislikeClick = { onCommentDislikeClick(index) },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            CommentInputFieldBar(
                commentText = commentInputText,
                onCommentChange = onCommentTextChange,
                onSendClick = onSendCommentClick,
                initialCommentator = initialCommentator,
                profileImageCommentator = null,
                backgroundColorProfileIconCommentator = MaterialTheme.colorScheme.primary,
                contentColorProfileIconCommentator = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun BodyCommentsReviewScreenPreview() {
    TheStudentsTheme {
        Surface {
            var commentInputText by rememberSaveable { mutableStateOf("") }
            var isLiked by rememberSaveable { mutableStateOf(false) }
            var isDisliked by rememberSaveable { mutableStateOf(false) }
            BodyCommentsReviewScreen(
                initialCommentator = "LS",
                review = localReviewsProvider.allReviews[0],
                onBackClick = {},
                isLiked = isLiked,
                isDisliked = isDisliked,
                onLikeClick = { isLiked = !isLiked },
                onDislikeClick = { isDisliked = !isDisliked },
                likedComments = emptySet(),
                dislikedComments = emptySet(),
                onCommentLikeClick = {},
                onCommentDislikeClick = {},
                commentInputText = commentInputText,
                onCommentTextChange = { commentInputText = it },
                onSendCommentClick = {}
            )
        }
    }
}

/**
 * Pantalla de comentarios de una resena.
 *
 * Es la duena de todo el estado de interaccion: el texto en curso, el voto sobre la resena y el
 * voto sobre cada comentario. Antes los votos de los comentarios estaban fijos en false dentro
 * del contenido, asi que los botones no reaccionaban.
 */
@Composable
fun CommentsReviewScreen(
    review: Review,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialCommentator: String = localStudentProvider.currentUser.initials
) {
    var commentInputText by rememberSaveable { mutableStateOf("") }
    var isLiked by rememberSaveable { mutableStateOf(false) }
    var isDisliked by rememberSaveable { mutableStateOf(false) }
    var likedComments by rememberSaveable { mutableStateOf(emptySet<Int>()) }
    var dislikedComments by rememberSaveable { mutableStateOf(emptySet<Int>()) }

    BodyCommentsReviewScreen(
        initialCommentator = initialCommentator,
        review = review,
        onBackClick = onBackClick,
        isLiked = isLiked,
        isDisliked = isDisliked,
        onLikeClick = {
            isLiked = !isLiked
            if (isLiked) isDisliked = false
        },
        onDislikeClick = {
            isDisliked = !isDisliked
            if (isDisliked) isLiked = false
        },
        likedComments = likedComments,
        dislikedComments = dislikedComments,
        onCommentLikeClick = { index ->
            likedComments = likedComments.toggle(index)
            if (index in likedComments) dislikedComments = dislikedComments - index
        },
        onCommentDislikeClick = { index ->
            dislikedComments = dislikedComments.toggle(index)
            if (index in dislikedComments) likedComments = likedComments - index
        },
        commentInputText = commentInputText,
        onCommentTextChange = { commentInputText = it },
        onSendCommentClick = { commentInputText = "" },
        modifier = modifier
    )
}

/** Agrega el elemento si falta y lo quita si ya estaba. */
private fun Set<Int>.toggle(value: Int): Set<Int> =
    if (value in this) this - value else this + value

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun CommentsReviewScreenPreview() {
    TheStudentsTheme {
        Surface {
            CommentsReviewScreen(
                review = localReviewsProvider.allReviews[0],
                onBackClick = {}
            )
        }
    }
}
