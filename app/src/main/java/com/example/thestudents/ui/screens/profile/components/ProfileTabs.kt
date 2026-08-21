package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

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
                text = stringResource(R.string.recibidas),
                isSelected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            )
            Spacer(modifier = Modifier.width(16.dp))
            ProfileTabItem(
                text = stringResource(R.string.escritas),
                isSelected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileTabsPreview() {
    TheStudentsTheme {
        Surface {
            ProfileTabs()
        }
    }
}
