package com.example.thestudents.ui.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.ui.screens.map.components.MapEmptyReviewsNotice
import com.example.thestudents.ui.screens.map.components.MapFilterBadge
import com.example.thestudents.ui.screens.map.components.MapMyLocationButton
import com.example.thestudents.ui.screens.map.components.MapReviewInfoCard
import com.example.thestudents.ui.screens.map.components.MapSearchField
import com.example.thestudents.ui.utils.HeaderBack
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

// Coordenadas iniciales en la Pontificia Universidad Javeriana (Bogota)
private val DEFAULT_BOGOTA_LOCATION = LatLng(4.6285, -74.0646)

// Limites geograficos de Colombia / Bogota para las busquedas con Geocoder (Sesion 9)
private const val LOWER_LEFT_LAT = 1.396967
private const val LOWER_LEFT_LON = -78.903968
private const val UPPER_RIGHT_LAT = 11.983639
private const val UPPER_RIGHT_LON = -71.869905

@Suppress("DEPRECATION")
private fun findLocationByAddress(geocoder: Geocoder, address: String): Pair<LatLng, String>? {
    if (address.isBlank()) return null
    return try {
        val addresses = geocoder.getFromLocationName(
            address,
            2,
            LOWER_LEFT_LAT,
            LOWER_LEFT_LON,
            UPPER_RIGHT_LAT,
            UPPER_RIGHT_LON
        )
        if (!addresses.isNullOrEmpty()) {
            val first = addresses[0]
            val title = first.getAddressLine(0) ?: address
            LatLng(first.latitude, first.longitude) to title
        } else {
            null
        }
    } catch (_: Exception) {
        null
    }
}

private fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadiusKm = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
        cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
        sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return ((earthRadiusKm * c) * 100.0).roundToInt() / 100.0
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun ReviewsMapScreen(
    reviewsMapViewModel: ReviewsMapViewModel,
    onBackClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by reviewsMapViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        reviewsMapViewModel.loadRecentReviews()
    }

    // Sensor de luminosidad para alternar entre mapa claro y oscuro (Sesion 9 - Mapas)
    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_LIGHT && event.values.isNotEmpty()) {
                    reviewsMapViewModel.updateAmbientLight(event.values[0])
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (lightSensor != null) {
            sensorManager.registerListener(
                sensorListener,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        onDispose {
            sensorManager?.unregisterListener(sensorListener)
        }
    }

    // Actualizaciones de ubicacion del dispositivo (LocationServices)
    DisposableEffect(locationPermissionState.status.isGranted) {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    reviewsMapViewModel.updateUserLocation(loc.latitude, loc.longitude)
                }
            }
        }

        if (locationPermissionState.status.isGranted &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10_000L)
                .setMinUpdateIntervalMillis(5_000L)
                .build()
            locationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
        }

        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    val geocoder = remember(context) { Geocoder(context) }

    Column(modifier = modifier.fillMaxSize()) {
        HeaderBack(
            title = stringResource(R.string.mapa_resenas_mayuscula),
            onBackClick = onBackClick
        )

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
                BodyReviewsMapScreen(
                    recentReviews = state.recentReviews,
                    selectedReview = state.selectedReview,
                    searchQuery = state.searchQuery,
                    searchedLocation = state.searchedLocation,
                    searchedAddressTitle = state.searchedAddressTitle,
                    userLocation = state.userLocation,
                    isDarkMap = state.isDarkMap || isSystemInDarkTheme(),
                    onSearchQueryChange = { reviewsMapViewModel.updateSearchQuery(it) },
                    onSearchSubmit = {
                        val found = findLocationByAddress(geocoder, state.searchQuery)
                        if (found != null) {
                            val (loc, title) = found
                            reviewsMapViewModel.updateSearchedLocation(loc, title)
                            state.userLocation?.let { userLoc ->
                                val dist = calculateDistanceKm(userLoc.latitude, userLoc.longitude, loc.latitude, loc.longitude)
                                Toast.makeText(context, "Distancia: $dist km", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onSelectReview = { reviewsMapViewModel.selectReview(it) },
                    onReviewClick = onReviewClick,
                    onStudentClick = onStudentClick
                )
            }
        }
    }
}

@Composable
fun BodyReviewsMapScreen(
    recentReviews: List<Review>,
    selectedReview: Review?,
    searchQuery: String,
    searchedLocation: LatLng?,
    searchedAddressTitle: String,
    userLocation: LatLng?,
    isDarkMap: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onSelectReview: (Review?) -> Unit,
    onReviewClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val initialTarget = remember(recentReviews) {
        recentReviews.firstOrNull { it.latitude != null && it.longitude != null }?.let {
            LatLng(it.latitude!!, it.longitude!!)
        } ?: DEFAULT_BOGOTA_LOCATION
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialTarget, 15f)
    }

    LaunchedEffect(searchedLocation) {
        if (searchedLocation != null) {
            try {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(searchedLocation, 16f)
                )
            } catch (_: Exception) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(searchedLocation, 16f)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("reviewsMapScreen")
    ) {
        val mapStyle = remember(isDarkMap, context) {
            val rawRes = if (isDarkMap) R.raw.darkmap else R.raw.lightmap
            try {
                MapStyleOptions.loadRawResourceStyle(context, rawRes)
            } catch (_: Exception) {
                null
            }
        }

        val uiSettings = remember {
            MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true,
                myLocationButtonEnabled = false
            )
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .testTag("googleMapView"),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapStyleOptions = mapStyle),
            uiSettings = uiSettings,
            onMapClick = { onSelectReview(null) }
        ) {
            // Marcadores de las reseñas publicadas en las ultimas 24 horas (con key única por reseña)
            recentReviews.forEach { review ->
                val lat = review.latitude
                val lon = review.longitude
                if (lat != null && lon != null) {
                    val ratingLabel = if (review.rating != null) "★ ${review.rating}" else ""
                    Marker(
                        state = rememberUpdatedMarkerState(
                            position = LatLng(lat, lon)
                        ),
                        title = "${review.reviewer.name} → ${review.reviewedStudent.name} $ratingLabel".trim(),
                        snippet = "${review.classReviewed} • ${review.content}",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                        onClick = {
                            onSelectReview(review)
                            false
                        }
                    )
                }
            }

            // Marcador de la ubicacion actual del usuario
            if (userLocation != null) {
                Marker(
                    state = rememberUpdatedMarkerState(position = userLocation),
                    title = stringResource(R.string.mi_ubicacion),
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            }

            // Marcador de direccion buscada con Geocoder
            if (searchedLocation != null) {
                Marker(
                    state = rememberUpdatedMarkerState(position = searchedLocation),
                    title = searchedAddressTitle,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }

        // Barra superior flotante: Buscador Geocoder + Pildora de "Ultimas 24 horas"
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            MapSearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                onSearch = {
                    keyboardController?.hide()
                    onSearchSubmit()
                }
            )

            // Etiqueta informativa: filtro de las ultimas 24 horas y cantidad de marcadores
            MapFilterBadge(
                recentReviewsCount = recentReviews.size
            )

            // Aviso discreto cuando no hay reseñas en las ultimas 24h
            if (recentReviews.isEmpty()) {
                MapEmptyReviewsNotice()
            }
        }

        // Boton flotante para centrar la camara en la ubicacion actual o en Bogota
        if (selectedReview == null) {
            MapMyLocationButton(
                onClick = {
                    val target = userLocation ?: initialTarget
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(target, 16f)
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 24.dp)
            )
        }

        // Tarjeta con la informacion del review al hacer click en un marcador
        AnimatedVisibility(
            visible = selectedReview != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            selectedReview?.let { review ->
                MapReviewInfoCard(
                    review = review,
                    onReviewClick = onReviewClick,
                    onStudentClick = onStudentClick,
                    onDismiss = { onSelectReview(null) }
                )
            }
        }
    }
}
