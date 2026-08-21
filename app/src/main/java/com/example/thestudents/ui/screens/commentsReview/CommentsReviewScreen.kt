package com.example.thestudents.ui.screens.commentsReview

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.screens.commentsReview.components.CommentInputFieldBar
import com.example.thestudents.ui.screens.commentsReview.components.CommentItem
import com.example.thestudents.ui.screens.commentsReview.components.CommentsTopAppBar
import com.example.thestudents.ui.screens.commentsReview.components.ReviewCard
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.FixedBottomBar

@Composable
fun BodyCommentsReviewScreen(
    initialCommentator: String,
    review: Review,
    onBackClick: () -> Unit,
    isLiked: Boolean,
    isDisliked: Boolean,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    commentInputText: String,
    onCommentTextChange: (String) -> Unit,
    onSendCommentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CommentsTopAppBar(
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            CommentInputFieldBar(
                commentText = commentInputText,
                onCommentChange = onCommentTextChange,
                onSendClick = onSendCommentClick,
                initialCommentator = initialCommentator,
                profileImageCommentator = null,
                backgroundColorProfileIconCommentator = MaterialTheme.colorScheme.primary,
                contentColorProfileIconCommentator = MaterialTheme.colorScheme.onPrimary,
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 1. Tarjeta fija
            ReviewCard(
                review = review,
                isLiked = isLiked,
                isDisliked = isDisliked,
                onLikeClick = onLikeClick,
                onDislikeClick = onDislikeClick,
                modifier = Modifier.padding(top = 8.dp)
            )

            // 2. Título fijo
            Text(
                text = stringResource(R.string.comentarios, review.comments.size),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 10.dp, bottom = 12.dp)
            )

            // 3. LazyColumn exclusivo para los comentarios (toma el espacio restante)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(review.comments) { comment ->
                    CommentItem(
                        comment = comment,
                        //REVISAR
                        isLiked = false,
                        isDisliked = false,
                        onLikeClick = {},
                        onDislikeClick = {},
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}


@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyCommentsReviewScreenPreview() {
    TheStudentsTheme {
        Surface {
            var commentInputText by remember { mutableStateOf("") }
            var isLiked by remember { mutableStateOf(false) }
            var isDisliked by remember { mutableStateOf(false) }
            BodyCommentsReviewScreen(
                initialCommentator = "LS",
                review = localReviewsProvider.allReviews[0],
                onBackClick = {},
                isDisliked = isDisliked,
                isLiked = isLiked,
                onLikeClick = {
                    isLiked = !isLiked
                },
                onDislikeClick = {
                    isDisliked = !isDisliked
                },
                commentInputText = commentInputText,
                onCommentTextChange = {commentInputText = it},
                onSendCommentClick = {}
            )
        }
    }
}


@Composable
fun CommentsReviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    review: Review
) {

    var commentInputText by remember { mutableStateOf("") }
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            FixedBottomBar(
                navController,
                "profile"
            )
        }
    ) { padding ->
        BodyCommentsReviewScreen(
            initialCommentator = "LS",
            review = review,
            onBackClick = { navController.popBackStack() },
            isLiked = isLiked,
            isDisliked = isDisliked,
            onLikeClick = {
                isLiked = !isLiked
            },
            onDislikeClick = {
                isDisliked = !isDisliked
            },
            commentInputText = commentInputText,
            onCommentTextChange = {commentInputText=it},
            onSendCommentClick = {},
            modifier = Modifier.padding(padding)
        )
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CommentsReviewScreenPreview() {
    TheStudentsTheme {
        Surface {
            CommentsReviewScreen(
                review = localReviewsProvider.allReviews[0]
            )
        }
    }
}
