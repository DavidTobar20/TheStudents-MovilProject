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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.components.ReviewCard
import com.example.thestudents.ui.screens.home.components.HomeTopBar
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun HomeScreen(
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Requisito: Ver reseñas realizadas por las personas que sigue el usuario.
    val followingIds = localStudentProvider.followingIds
    val followedReviews = localReviewsProvider.getReviewsByFollowed(followingIds)

    // Estado para los likes/dislikes de cada reseña en la Home.
    var likedReviews by rememberSaveable { mutableStateOf(emptySet<Int>()) }
    var dislikedReviews by rememberSaveable { mutableStateOf(emptySet<Int>()) }

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
                            text = stringResource(R.string.a_n_no_hay_rese_as_de_las_personas_que_sigues_comienza_a_explorar_para_seguir_a_tus_compa_eros),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                itemsIndexed(followedReviews) { index, review ->
                    // Obtenemos el índice real de la lista completa para la navegación
                    val realIndex = localReviewsProvider.allReviews.indexOf(review)
                    
                    ReviewCard(
                        review = review,
                        isLiked = index in likedReviews,
                        isDisliked = index in dislikedReviews,
                        onLikeClick = {
                            likedReviews = if (index in likedReviews) likedReviews - index else likedReviews + index
                            if (index in likedReviews) dislikedReviews = dislikedReviews - index
                        },
                        onDislikeClick = {
                            dislikedReviews = if (index in dislikedReviews) dislikedReviews - index else dislikedReviews + index
                            if (index in dislikedReviews) likedReviews = likedReviews - index
                        },
                        onCommentClick = { onReviewClick(realIndex) },
                        onCardClick = { onReviewClick(realIndex) },
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
            HomeScreen(onReviewClick = {})
        }
    }
}

@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        Surface {
            HomeScreen(onReviewClick = {})
        }
    }
}
