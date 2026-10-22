package com.example.thestudents.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.TabItem

enum class HomeTab { ALL, FOLLOWED }

@Composable
fun HomeTabs(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TabItem(
                text = stringResource(R.string.todos),
                isSelected = selectedTab == HomeTab.ALL,
                onClick = { onTabSelected(HomeTab.ALL) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            TabItem(
                text = stringResource(R.string.seguidos),
                isSelected = selectedTab == HomeTab.FOLLOWED,
                onClick = { onTabSelected(HomeTab.FOLLOWED) }
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
fun HomeTabsPreview() {
    TheStudentsTheme {
        Surface {
            HomeTabs(
                selectedTab = HomeTab.ALL,
                onTabSelected = {}
            )
        }
    }
}
