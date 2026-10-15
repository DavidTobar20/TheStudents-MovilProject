package com.example.thestudents.ui.screens.commentsReview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.components.ReviewCard
import com.example.thestudents.ui.screens.commentsReview.components.CommentInputFieldBar
import com.example.thestudents.ui.screens.commentsReview.components.CommentItem
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.HeaderBack

/**
 * Contenido de los comentarios de una resena.
 */
@Composable
fun BodyCommentsReviewScreen(
    commentator: Student,
    review: Review,
    onStudentClick: (String) -> Unit,
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
    Column(modifier = modifier.fillMaxSize()) {
        ReviewCard(
            review = review,
            isLiked = isLiked,
            isDisliked = isDisliked,
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
            onReviewerClick = review.reviewer.id
                .takeIf { it.isNotBlank() }
                ?.let { id -> { onStudentClick(id) } },
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
                    onCommentatorClick = comment.commentator.id
                        .takeIf { it.isNotBlank() }
                        ?.let { id -> { onStudentClick(id) } },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        CommentInputFieldBar(
            commentText = commentInputText,
            onCommentChange = onCommentTextChange,
            onSendClick = onSendCommentClick,
            commentator = commentator
        )
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
                commentator = localStudentProvider.currentUser,
                review = localReviewsProvider.allReviews[1],
                onStudentClick = {},
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
 */
@Composable
fun CommentsReviewScreen(
    commentsReviewViewModel: CommentsReviewViewModel,
    reviewId: String,
    onBackClick: () -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by commentsReviewViewModel.uiState.collectAsState()

    LaunchedEffect(reviewId) {
        commentsReviewViewModel.getReviewById(reviewId)
        commentsReviewViewModel.getCommentator()
    }

    val review = state.review

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderBack(
            title = stringResource(R.string.resena),
            onBackClick = onBackClick
        )

        when {
            state.isLoading && review == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            review == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage ?: stringResource(R.string.resena_no_encontrada),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                BodyCommentsReviewScreen(
                    commentator = state.commentator,
                    review = review,
                    onStudentClick = onStudentClick,
                    isLiked = state.isLiked,
                    isDisliked = state.isDisliked,
                    onLikeClick = { commentsReviewViewModel.updateIsLiked() },
                    onDislikeClick = { commentsReviewViewModel.updateIsDisliked() },
                    likedComments = state.likedComments,
                    dislikedComments = state.dislikedComments,
                    onCommentLikeClick = { commentsReviewViewModel.updateLikedComments(it) },
                    onCommentDislikeClick = { commentsReviewViewModel.updateDislikedComments(it) },
                    commentInputText = state.commentInputText,
                    onCommentTextChange = { commentsReviewViewModel.updateCommentInputText(it) },
                    onSendCommentClick = onBackClick
                )
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun CommentsReviewScreenPreview() {
    TheStudentsTheme {
        CommentsReviewScreen(
            commentsReviewViewModel = viewModel(),
            reviewId = "1",
            onBackClick = {},
            onStudentClick = {}
        )
    }
}
