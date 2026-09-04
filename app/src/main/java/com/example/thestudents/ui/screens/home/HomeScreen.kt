package com.example.thestudents.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.ui.components.ReviewCard
import com.example.thestudents.ui.screens.home.components.HomeTopBar
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun HomeScreen(
    onReviewClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
) {

    val state by homeViewModel.uiState.collectAsState()

    BodyHomeScreen(
        followedReviews = state.followedReviews,
        onLikeClick = { index -> homeViewModel.updateLikedReviews(index) },
        onDislikeClick = { index -> homeViewModel.updateDislikedReviews(index) },
        onCommentClick = onReviewClick,
        onCardClick = onReviewClick,
        onReviewerClick = onStudentClick,
        isLiked = { index -> homeViewModel.reviewIsLiked(index) },
        isDisliked = { index -> homeViewModel.reviewIsDisliked(index) },
        modifier = modifier
    )
}

@Composable
fun BodyHomeScreen(
    followedReviews: List<Review>,
    onLikeClick: (Int) -> Unit,
    onDislikeClick: (Int) -> Unit,
    onCommentClick: (String) -> Unit,
    onCardClick: (String) -> Unit,
    onReviewerClick: (String) -> Unit,
    isLiked: (Int) -> Boolean,
    isDisliked: (Int) -> Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeTopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (followedReviews.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.aun_no_hay_resenas_de_las_personas_que_sigues_comienza_a_explorar_para_seguir_a_tus_companeros),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                itemsIndexed(followedReviews) { index, review ->

                    ReviewCard(
                        review = review,
                        isLiked = isLiked(index),
                        isDisliked = isDisliked(index),
                        onLikeClick = { onLikeClick(index) },
                        onDislikeClick = { onDislikeClick(index) },
                        onCommentClick = { onCommentClick(review.id) },
                        onCardClick = { onCardClick(review.id) },
                        onReviewerClick = { onReviewerClick(review.reviewer.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    TheStudentsTheme(darkTheme = false) {
        Surface {
            HomeScreen(
                homeViewModel = viewModel(),
                onReviewClick = {},
                onStudentClick = {}
            )
        }
    }
}

@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        Surface {
            HomeScreen(
                homeViewModel = viewModel(),
                onReviewClick = {},
                onStudentClick = {}
            )
        }
    }
}
