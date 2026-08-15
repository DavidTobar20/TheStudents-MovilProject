package com.example.thestudents.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.ui.utils.FixedBottomBar
import com.example.thestudents.ui.Student
import com.example.thestudents.ui.screens.profile.components.ProfileHeader
import com.example.thestudents.ui.screens.profile.components.ProfileTabs
import com.example.thestudents.ui.screens.profile.components.RatingChartSection
import com.example.thestudents.ui.screens.profile.components.ReviewItem
import com.example.thestudents.ui.screens.profile.components.StatsSection
import com.example.thestudents.ui.screens.profile.components.UserInfoSection
import com.example.thestudents.ui.utils.ButtonWithLogo


@Composable
fun BodyProfile(
    student: Student,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        item { ProfileHeader(onBackClick = onBackClick) }
        item { UserInfoSection(student = student) }
        item { StatsSection(student = student) }
        item { ButtonWithLogo(
            text = "Editar perfil",
            iconRes = R.drawable.logo_google,
            onClick = {},
            borderColor = colorResource(R.color.dark_green),
            contentColor = colorResource(R.color.dark_green),
            modifier = Modifier.height(48.dp)
                .padding(horizontal = 24.dp)
        ) }
        item { RatingChartSection() }
        item { ProfileTabs() }
        item { ReviewItem() }
        item { ReviewItem() }
    }
}

@Preview(showBackground = true)
@Composable
fun BodyProfilePreview() {
    val mockStudent = Student(
        id = "1",
        name = "Juan Pablo Mejía",
        email = "juan.pablo.m@u.edu.co",
        program = "Ingeniería de Sistemas",
        semester = 7,
        bio = "Me gusta trabajar en equipo y aprender de proyectos reales.",
        rating = 4.8f,
        reviewsCount = 21,
        initials = "JP",
        profileColor = colorResource(R.color.light_tan)
    )
    BodyProfile(student = mockStudent, onBackClick = {})
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val currentUser = Student(
        id = "1",
        name = "Juan Pablo Mejía",
        email = "juan.pablo.m@u.edu.co",
        program = "Ingeniería de Sistemas",
        semester = 7,
        bio = "Me gusta trabajar en equipo y aprender de proyectos reales. Abierto a grupos de estudio.",
        rating = 4.8f,
        reviewsCount = 21,
        initials = "JP",
        profileColor = colorResource(R.color.light_tan),
        profileImageRes = R.drawable.logosinfondo // Ejemplo de imagen, puedes cambiarla luego
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.cream),
        bottomBar = { FixedBottomBar(navController, "profile") }
    ) { padding ->
        BodyProfile(
            student = currentUser,
            onBackClick = { navController.popBackStack() },
            contentPadding = padding
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullProfileScreenPreview() {
    ProfileScreen()
}
