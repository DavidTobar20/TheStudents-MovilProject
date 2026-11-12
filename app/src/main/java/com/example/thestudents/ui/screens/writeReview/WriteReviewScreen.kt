package com.example.thestudents.ui.screens.writeReview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thestudents.R
import com.example.thestudents.ui.screens.writeReview.components.RatingCard
import com.example.thestudents.ui.screens.writeReview.components.ReviewField
import com.example.thestudents.ui.screens.writeReview.components.ReviewHeader
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.HeaderBack
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
fun BodyWriteReviewScreen(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingSelected: (Int) -> Unit,
    onReviewChange: (String) -> Unit,
    review: String,
    onPublishClick: () -> Unit,
    onBackClick: () -> Unit,
    onStudentClick: () -> Unit,
    nameReviewed: String,
    initialsReviewed: String,
    profileImageReviewed: String? = null,
    courseInfoReviewed: String,
    hasLocationAttached: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        HeaderBack(
            title = stringResource(R.string.nueva_resena),
            onBackClick = onBackClick
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReviewHeader(
                name = nameReviewed,
                initials = initialsReviewed,
                profileImage = profileImageReviewed,
                courseInfo = courseInfoReviewed,
                onAvatarClick = onStudentClick
            )
            RatingCard(
                rating = rating,
                onRatingSelected = onRatingSelected
            )
            ReviewField(
                review = review,
                onContenidoChange = onReviewChange,
                placeholder = "Escribe tu experiencia con $nameReviewed..."
            )
            if (hasLocationAttached) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.ubicacion_adjunta_mapa),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        ButtonWithoutIcon(
            textoBoton = stringResource(R.string.publicar_resena),
            onClick = onPublishClick,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BodyWriteReviewScreenPreview() {
    TheStudentsTheme {
        BodyWriteReviewScreen(
            rating = 3,
            onRatingSelected = {},
            onReviewChange = {},
            review = "Esta es una reseña de prueba",
            onPublishClick = {},
            onBackClick = {},
            onStudentClick = {},
            nameReviewed = "Laura Martínez",
            initialsReviewed = "LM",
            profileImageReviewed = null,
            courseInfoReviewed = "Estructuras de Datos (ISIS1206) • 2025-2",
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun WriteReviewScreen(
    writeReviewViewModel: WriteReviewViewModel,
    inscriptionId: String,
    onBackClick: () -> Unit,
    onStudentClick: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by writeReviewViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        writeReviewViewModel.getInscriptionById(inscriptionId)
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    writeReviewViewModel.updateLocation(location.latitude, location.longitude)
                } else {
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    ).addOnSuccessListener { currentLoc ->
                        if (currentLoc != null) {
                            writeReviewViewModel.updateLocation(currentLoc.latitude, currentLoc.longitude)
                        } else {
                            // Ubicacion por defecto en la Javeriana (Bogota) si el emulador no tiene GPS activo
                            writeReviewViewModel.updateLocation(4.6285, -74.0646)
                        }
                    }.addOnFailureListener {
                        writeReviewViewModel.updateLocation(4.6285, -74.0646)
                    }
                }
            }.addOnFailureListener {
                writeReviewViewModel.updateLocation(4.6285, -74.0646)
            }
        }
    }

    LaunchedEffect(state.navigateToHome) {
        if (state.navigateToHome) {
            onNavigateToHome()
        }
    }

    when {
        state.isLoading && state.student == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.student == null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error ?: stringResource(R.string.estudiante_no_encontrado),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        else -> {
            Column(modifier = modifier.fillMaxSize()) {
                BodyWriteReviewScreen(
                    modifier = Modifier.weight(1f),
                    rating = state.rating,
                    onRatingSelected = { writeReviewViewModel.updateRating(it) },
                    onReviewChange = { writeReviewViewModel.updateReview(it) },
                    review = state.review,
                    onPublishClick = { writeReviewViewModel.createReview() },
                    onBackClick = onBackClick,
                    onStudentClick = { onStudentClick(state.student!!.id) },
                    nameReviewed = state.student!!.name,
                    initialsReviewed = state.student!!.initials,
                    profileImageReviewed = state.student?.profileImage,
                    courseInfoReviewed = "${state.className} • ${state.period}",
                    hasLocationAttached = state.latitude != null && state.longitude != null
                )
                if (state.error != null) {
                    Text(
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WriteReviewScreenPreview() {
    TheStudentsTheme {
        Surface {
            WriteReviewScreen(
                writeReviewViewModel = viewModel(),
                inscriptionId = "i3",
                onBackClick = {},
                onStudentClick = {},
                onNavigateToHome = {}
            )
        }
    }
}
