package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R

@Composable
fun ProfileTabs(
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            ProfileTabItem(
                text = "Recibidas",
                isSelected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            )
            Spacer(modifier = Modifier.width(16.dp))
            ProfileTabItem(
                text = "Escritas",
                isSelected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            color = colorResource(R.color.sage).copy(alpha = 0.2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileTabsPreview() {
    ProfileTabs()
}
