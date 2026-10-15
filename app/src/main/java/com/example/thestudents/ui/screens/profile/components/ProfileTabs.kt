package com.example.thestudents.ui.screens.profile.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

/** Pestanas del perfil: recibidas y escritas. */
enum class ProfileTab { RECEIVED, WRITTEN }

/**
 * Selector de pestanas del perfil.
 *
 * La pestana activa esta elevada a quien lo usa, porque es la pantalla la que decide que lista
 * mostrar debajo. Antes se guardaba aqui dentro y nadie mas podia leerla.
 */
@Composable
fun ProfileTabs(
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            ProfileTabItem(
                text = stringResource(R.string.recibidas),
                isSelected = selectedTab == ProfileTab.RECEIVED,
                onClick = { onTabSelected(ProfileTab.RECEIVED) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            ProfileTabItem(
                text = stringResource(R.string.escritas),
                isSelected = selectedTab == ProfileTab.WRITTEN,
                onClick = { onTabSelected(ProfileTab.WRITTEN) }
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
            var selectedTab by rememberSaveable { mutableStateOf(ProfileTab.RECEIVED) }
            ProfileTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}
