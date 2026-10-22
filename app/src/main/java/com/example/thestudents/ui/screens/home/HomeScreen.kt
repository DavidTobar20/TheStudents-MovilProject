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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.components.ReviewCard
import com.example.thestudents.ui.screens.home.components.HomeTab
import com.example.thestudents.ui.screens.home.components.HomeTabs
import com.example.thestudents.ui.screens.home.components.HomeTopBar
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun HomeScreen(
    onReviewClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    val state by homeViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadReviews()
    }

    Column(modifier = modifier.fillMaxSize()) {
        HomeTopBar()

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                val reviews = if (state.selectedTab == HomeTab.ALL) state.allReviews else state.followedReviews

                BodyHomeScreen(
                    selectedTab = state.selectedTab,
                    onTabSelected = { homeViewModel.onTabSelected(it) },
                    reviews = reviews,
                    onLikeClick = { index -> homeViewModel.updateLikedReviews(index) },
                    onDislikeClick = { index -> homeViewModel.updateDislikedReviews(index) },
                    onCommentClick = onReviewClick,
                    onCardClick = onReviewClick,
                    onReviewerClick = onStudentClick
                )
            }
        }
    }
}

@Composable
fun BodyHomeScreen(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    reviews: List<Review>,
    onLikeClick: (Int) -> Unit,
    onDislikeClick: (Int) -> Unit,
    onCommentClick: (String) -> Unit,
    onCardClick: (String) -> Unit,
    onReviewerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeTabs(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (reviews.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTab == HomeTab.FOLLOWED) {
                            stringResource(R.string.aun_no_hay_resenas_de_las_personas_que_sigues_comienza_a_explorar_para_seguir_a_tus_companeros)
                        } else {
                            stringResource(R.string.no_hay_resenas_disponibles)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    itemsIndexed(reviews, key = { _, review -> review.id }) { index, review ->
                        ReviewCard(
                            review = review,
                            isLiked = review.isLiked,
                            isDisliked = review.isDisliked,
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
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun BodyHomeScreenPreview() {
    TheStudentsTheme {
        BodyHomeScreen(
            selectedTab = HomeTab.ALL,
            onTabSelected = {},
            reviews = localReviewsProvider.allReviews,
            onLikeClick = {},
            onDislikeClick = {},
            onCommentClick = {},
            onCardClick = {},
            onReviewerClick = {}
        )
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    TheStudentsTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopBar()
            BodyHomeScreen(
                selectedTab = HomeTab.ALL,
                onTabSelected = {},
                reviews = localReviewsProvider.allReviews,
                onLikeClick = {},
                onDislikeClick = {},
                onCommentClick = {},
                onCardClick = {},
                onReviewerClick = {}
            )
        }
    }
}
