package com.example.thestudents.ui.screens.EditarPerfil.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.local.localStudentProvider

@Composable
fun EditBioSection(
    bio: String,
    onBioChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxLength = 160
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.biografia_mayuscula),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.sage),
                    letterSpacing = 0.5.sp
                )
                
                Text(
                    text = "${bio.length}/$maxLength",
                    fontSize = 12.sp,
                    color = colorResource(R.color.gold),
                    fontWeight = FontWeight.Medium
                )
            }
            
            TextField(
                value = bio,
                onValueChange = { if (it.length <= maxLength) onBioChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = colorResource(R.color.medium_green).copy(alpha = 0.9f),
                    lineHeight = 20.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorResource(R.color.dark_green)
                ),
                minLines = 3
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditBioSectionPreview() {
    EditBioSection(
        bio = localStudentProvider.currentUser.bio,
        onBioChange = {}
    )
}
