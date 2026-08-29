package com.example.thestudents.ui.screens.reviews.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun ReviewStudentItem(
    student: Student,
    onStudentClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileIcon(
            initials = student.initials,
            profileImage = student.profileImage,
            backgroundColor = student.profileColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            modifier = Modifier
                .size(48.dp)
                .clickable(onClick = onStudentClick)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier
            .weight(1f)
            .clickable(onClick = onStudentClick)
        ) {
            Text(
                text = student.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = student.period,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        ButtonWithoutIcon(
            textoBoton = stringResource(R.string.resenar),
            onClick = onWriteReviewClick,
            fontSize = 12.sp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewStudentItemPreview() {
    TheStudentsTheme {
        Surface {
            ReviewStudentItem(
                student = localStudentProvider.students[3],
                onStudentClick = {},
                onWriteReviewClick = {}
            )
        }
    }
}
