package com.example.thestudents.ui.screens.reviews.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            profileImageRes = student.profileImageRes,
            backgroundColor = student.profileColor,
            contentColor = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = student.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = student.period,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        ButtonWithoutIcon(
            textoBoton = stringResource(R.string.resenar),
            onClick = {},
            fontSize = 12.sp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewStudentItemPreview() {
    TheStudentsTheme {
        ReviewStudentItem(
            student = localStudentProvider.students[3]
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReviewStudentItemDarkPreview() {
    TheStudentsTheme {
        ReviewStudentItem(
            student = localStudentProvider.students[3]
        )
    }
}
