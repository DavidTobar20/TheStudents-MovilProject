package com.example.thestudents.ui.screens.reviews.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.local.localInscriptionProvider
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun CourseSectionCard(
    section: CourseSection,
    onStudentClick: (String) -> Unit,
    onWriteReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${section.title} (${section.period})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                section.inscriptions.forEachIndexed { index, inscription ->
                    val student = inscription.student
                    ReviewStudentItem(
                        inscription = inscription,
                        onStudentClick = { onStudentClick(student.id) },
                        onWriteReviewClick = { onWriteReviewClick(inscription.id) }
                    )
                    if (index < (section.inscriptions.size - 1)) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseSectionCardPreview() {
    TheStudentsTheme {
        Surface {
            CourseSectionCard(
                section = CourseSection(
                    title = "Estructuras de Datos (ISIS1206)",
                    period = "2025-1",
                    inscriptions = listOf(
                        localInscriptionProvider.inscriptions[2],
                        localInscriptionProvider.inscriptions[4]
                    )
                ),
                onStudentClick = {},
                onWriteReviewClick = {}
            )
        }
    }
}
