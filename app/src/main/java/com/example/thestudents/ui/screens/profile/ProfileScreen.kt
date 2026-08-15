package com.example.thestudents.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.ui.utils.FixedBottomBar
import com.example.thestudents.ui.utils.Student
import com.example.thestudents.ui.theme.*

@Composable
fun ProfileHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Atrás", tint = DarkGreen)
        }
        Text(
            text = "Perfil",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            fontFamily = FontFamily.Serif
        )
        IconButton(onClick = { /* Menú */ }) {
            Icon(Icons.Default.MoreHoriz, contentDescription = "Opciones", tint = DarkGreen)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    ProfileHeader(onBackClick = {})
}

@Composable
fun UserInfoSection(
    student: Student,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(LightTan),
                contentAlignment = Alignment.Center
            ) {
                if (student.profileImageRes != null) {
                    Image(
                        painter = painterResource(id = student.profileImageRes),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = student.initials,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = student.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = student.email.split("@")[0].let { "@$it" },
                    fontSize = 14.sp,
                    color = MediumGreen.copy(alpha = 0.6f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Tan.copy(alpha = 0.2f),
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.School, null, Modifier.size(16.dp), tint = DarkGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${student.program} · Sem. ${student.semester}",
                    fontSize = 13.sp,
                    color = DarkGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = student.bio,
            fontSize = 14.sp,
            color = MediumGreen.copy(alpha = 0.8f),
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoSectionPreview() {
    UserInfoSection(
        student = Student(
            id = "1",
            name = "Juan Pablo Mejía",
            email = "juan.pablo.m@u.edu.co",
            program = "Ingeniería de Sistemas",
            semester = 7,
            bio = "Me gusta trabajar en equipo y aprender de proyectos reales. Abierto a grupos de estudio.",
            rating = 4.8f,
            reviewsCount = 21,
            initials = "JP",
            profileColor = LightTan
        )
    )
}

@Composable
fun StatItem(
    count: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MediumGreen.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatsSection(
    student: Student,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem("128", "SEGUIDORES")
        VerticalDivider(modifier = Modifier.height(40.dp), color = Sage.copy(alpha = 0.3f))
        StatItem("96", "SIGUIENDO")
        VerticalDivider(modifier = Modifier.height(40.dp), color = Sage.copy(alpha = 0.3f))
        StatItem(student.reviewsCount.toString(), "RESEÑAS")
    }
}

@Preview(showBackground = true)
@Composable
fun StatsSectionPreview() {
    StatsSection(
        student = Student(
            id = "1",
            name = "Juan Pablo Mejía",
            email = "juan.pablo.m@u.edu.co",
            program = "Ingeniería de Sistemas",
            semester = 7,
            bio = "",
            rating = 4.8f,
            reviewsCount = 21,
            initials = "JP",
            profileColor = LightTan
        )
    )
}

@Composable
fun EditProfileButton(modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { /* Editar */ },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGreen),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen)
    ) {
        Icon(Icons.Outlined.Edit, null, Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Editar perfil", fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileButtonPreview() {
    EditProfileButton()
}

@Composable
fun RatingChartSection(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            val ratings = listOf(0.7f, 0.2f, 0.1f, 0.05f, 0.02f)
            ratings.forEachIndexed { index, progress ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${5 - index}",
                        fontSize = 12.sp,
                        color = Sage,
                        modifier = Modifier.width(12.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape),
                        color = Gold,
                        trackColor = LightTan
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingChartSectionPreview() {
    RatingChartSection()
}

@Composable
fun ProfileTabs(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(onClick = { selectedTab = 0 }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Recibidas",
                        color = if (selectedTab == 0) DarkGreen else Sage,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    if (selectedTab == 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(2.dp)
                                .background(DarkGreen)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            TextButton(onClick = { selectedTab = 1 }) {
                Text(
                    text = "Escritas",
                    color = if (selectedTab == 1) DarkGreen else Sage,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = Sage.copy(alpha = 0.2f))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileTabsPreview() {
    ProfileTabs()
}

@Composable
fun ReviewItem(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "DR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Daniel Ruiz", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "FIS220, 2025-2  ·  Hace 3 días", color = Sage, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "“Muy responsable, aportó ideas clave en todas las etapas del proyecto.”",
                    fontSize = 14.sp,
                    color = MediumGreen.copy(alpha = 0.9f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
            IconButton(onClick = { /* Opciones */ }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.MoreHoriz, null, tint = Sage)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewItemPreview() {
    ReviewItem()
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
        profileColor = LightTan,
        profileImageRes = R.drawable.logosinfondo // Ejemplo de imagen, puedes cambiarla luego
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Cream,
        bottomBar = { FixedBottomBar(navController, "profile") }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item { ProfileHeader(onBackClick = { navController.popBackStack() }) }
            item { UserInfoSection(student = currentUser) }
            item { StatsSection(student = currentUser) }
            item { EditProfileButton() }
            item { RatingChartSection() }
            item { ProfileTabs() }
            item { ReviewItem() }
            item { ReviewItem() } // Duplicamos para llenar
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullProfileScreenPreview() {
    ProfileScreen()
}
