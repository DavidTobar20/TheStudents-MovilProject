package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ProfileIcon

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
            ProfileIcon(
                initials = student.initials,
                profileImageRes = student.profileImageRes,
                modifier = Modifier.size(80.dp),
                backgroundColor = colorResource(R.color.light_tan),
                contentColor = colorResource(R.color.dark_green),
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = student.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.dark_green),
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = student.username,
                    fontSize = 14.sp,
                    color = colorResource(R.color.medium_green).copy(alpha = 0.6f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = colorResource(R.color.tan).copy(alpha = 0.2f),
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.School, null, Modifier.size(16.dp), tint = colorResource(R.color.dark_green))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${student.program} · Sem. ${student.semester}",
                    fontSize = 13.sp,
                    color = colorResource(R.color.dark_green),
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = student.bio,
            fontSize = 14.sp,
            color = colorResource(R.color.medium_green).copy(alpha = 0.8f),
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoSectionPreview() {
    TheStudentsTheme {
        UserInfoSection(
            student = localStudentProvider.currentUser
        )
    }
}
