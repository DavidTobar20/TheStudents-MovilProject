package com.example.thestudents.ui.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.screens.map.components.MapReviewInfoCard
import com.example.thestudents.ui.theme.TheStudentsTheme
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
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
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

    BodyReviewsMapScreen(
        recentReviews = state.recentReviews,
        selectedReview = state.selectedReview,
        searchQuery = state.searchQuery,
        searchedLocation = state.searchedLocation,
        searchedAddressTitle = state.searchedAddressTitle,
        userLocation = state.userLocation,
        isDarkMap = state.isDarkMap || isSystemInDarkTheme(),
        isLoading = state.isLoading,
        hasLocationPermission = locationPermissionState.status.isGranted,
        onRequestPermissionClick = { locationPermissionState.launchPermissionRequest() },
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
        onBackClick = onBackClick,
        onReviewClick = onReviewClick,
        onStudentClick = onStudentClick,
        modifier = modifier
    )
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
    isLoading: Boolean,
    hasLocationPermission: Boolean,
    onRequestPermissionClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onSelectReview: (Review?) -> Unit,
    onBackClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
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
        if (searchedLocation != null && !isPreview) {
            try {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(searchedLocation, 16f)
                )
            } catch (_: Exception) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(searchedLocation, 16f)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("reviewsMapScreen")
    ) {
        HeaderBack(
            title = stringResource(R.string.mapa_resenas_mayuscula),
            onBackClick = onBackClick
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (isPreview) {
                // Vista previa en Android Studio (sin Google Play Services)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Google Maps Preview (${recentReviews.size} reseñas)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
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
                    // Marcadores de las reseñas publicadas en las ultimas 24 horas
                    recentReviews.forEach { review ->
                        val lat = review.latitude
                        val lon = review.longitude
                        if (lat != null && lon != null) {
                            val ratingLabel = if (review.rating != null) "★ ${review.rating}" else ""
                            Marker(
                                state = rememberUpdatedMarkerState(position = LatLng(lat, lon)),
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
            }

            // Barra superior flotante: Buscador Geocoder + Pildora de "Ultimas 24 horas"
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.buscar_direccion_mapa),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            onSearchSubmit()
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.95f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.92f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(18.dp))
                        .testTag("mapSearchField")
                )

                // Etiqueta informativa: filtro de las ultimas 24 horas y cantidad de marcadores
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .shadow(6.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${stringResource(R.string.ultimas_24_horas)} • ${
                                pluralStringResource(
                                    R.plurals.resenas_count,
                                    recentReviews.size,
                                    recentReviews.size
                                )
                            }",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.testTag("map24hBadge")
                        )
                    }

                    if (!hasLocationPermission) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shadowElevation = 4.dp
                        ) {
                            TextButton(
                                onClick = onRequestPermissionClick,
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.solicitar_permiso),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }

                // Aviso discreto cuando no hay reseñas en las ultimas 24h
                if (!isLoading && recentReviews.isEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.94f),
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(14.dp)
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.sin_resenas_mapa_24h),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .testTag("emptyMapReviewsMessage")
                        )
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Boton flotante para centrar la camara en la ubicacion actual o en Bogota
            if (selectedReview == null) {
                FloatingActionButton(
                    onClick = {
                        val target = userLocation ?: initialTarget
                        if (!isPreview) {
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(target, 16f)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 24.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = stringResource(R.string.mi_ubicacion)
                    )
                }
            }

            // Tarjeta con la informacion del review al hacer click en un marcador
            androidx.compose.animation.AnimatedVisibility(
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
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun BodyReviewsMapScreenPreview() {
    TheStudentsTheme {
        Surface {
            val sampleReviews = localReviewsProvider.allReviews.filter { it.latitude != null }
            BodyReviewsMapScreen(
                recentReviews = sampleReviews,
                selectedReview = sampleReviews.firstOrNull(),
                searchQuery = "",
                searchedLocation = null,
                searchedAddressTitle = "",
                userLocation = DEFAULT_BOGOTA_LOCATION,
                isDarkMap = false,
                isLoading = false,
                hasLocationPermission = true,
                onRequestPermissionClick = {},
                onSearchQueryChange = {},
                onSearchSubmit = {},
                onSelectReview = {},
                onBackClick = {},
                onReviewClick = {},
                onStudentClick = {}
            )
        }
    }
}
