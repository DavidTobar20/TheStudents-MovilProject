package com.example.thestudents.ui.screens.reviews.components

import com.example.thestudents.ui.components.luxuryCard
import com.example.thestudents.ui.components.SectionLabel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
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
        // Materia y periodo en letras doradas, como "RESEÑA PARA"
        SectionLabel(
            text = "${section.title} · ${section.period}",
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .luxuryCard()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                if (section.inscriptions.isEmpty()) {
                    Text(
                        text = stringResource(R.string.sin_companeros_en_materia),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    )
                } else {
                    section.inscriptions.forEachIndexed { index, inscription ->
                        val student = inscription.student
                        ReviewStudentItem(
                            inscription = inscription,
                            onStudentClick = { onStudentClick(student.id) },
                            onWriteReviewClick = { onWriteReviewClick(inscription.id) }
                        )
                        if (index < (section.inscriptions.size - 1)) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                thickness = 1.dp
                            )
                        }
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
