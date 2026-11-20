package com.example.thestudents.ui.screens.teamMatch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.TeamMatchCandidate
import com.example.thestudents.ui.components.GoldRingAvatar
import com.example.thestudents.ui.components.SectionLabel
import com.example.thestudents.ui.components.luxuryCard
import com.example.thestudents.ui.theme.TheStudentsTheme

/** Resumen del equipo: compatibilidad, tamaño, integrantes y botón para invitar. */
@Composable
fun SuggestedTeamCard(
    members: List<TeamMatchCandidate>,
    teamSize: Int,
    maxTeamSize: Int,
    teamScore: Int,
    isSendingInvites: Boolean,
    invitesSent: Boolean,
    invitedCount: Int,
    inviteError: String?,
    onTeamSizeChange: (Int) -> Unit,
    onResetSuggestion: () -> Unit,
    onSendInvites: () -> Unit,
    modifier: Modifier = Modifier,
    minTeamSize: Int = 2
) {
    val teamTier = tierForScore(teamScore)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .luxuryCard()
            .padding(20.dp)
    ) {
        SectionLabel(text = stringResource(R.string.tu_equipo_sugerido))
        Spacer(modifier = Modifier.height(12.dp))

        // Compatibilidad del equipo
        Row(verticalAlignment = Alignment.CenterVertically) {
            MatchScoreRing(
                score = teamScore,
                tier = teamTier,
                size = 84.dp,
                strokeWidth = 7.dp,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.compatibilidad_equipo),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (members.isNotEmpty()) MatchTierChip(tier = teamTier)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pluralStringResource(
                        R.plurals.integrantes_seleccionados,
                        teamSize - 1,
                        members.size,
                        teamSize - 1
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Integrantes: el usuario + los elegidos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TeamMemberBubble(label = stringResource(R.string.tu)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.tu),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            members.forEach { member ->
                val student = member.student
                TeamMemberBubble(label = student.name.substringBefore(" ").ifBlank { student.username }) {
                    GoldRingAvatar(
                        initials = student.initials,
                        profileImage = student.profileImage,
                        backgroundColor = student.profileColor,
                        size = 48.dp,
                        fontSize = 17.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHigh)
        Spacer(modifier = Modifier.height(12.dp))

        // Selector de tamaño del equipo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.integrantes_contandote),
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            FilledTonalIconButton(
                onClick = { onTeamSizeChange(teamSize - 1) },
                enabled = teamSize > minTeamSize && !isSendingInvites,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Filled.Remove, contentDescription = stringResource(R.string.quitar_integrante))
            }
            Text(
                text = teamSize.toString(),
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            FilledTonalIconButton(
                onClick = { onTeamSizeChange(teamSize + 1) },
                enabled = teamSize < maxTeamSize && !isSendingInvites,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.agregar_integrante))
            }
        }

        TextButton(
            onClick = onResetSuggestion,
            enabled = !isSendingInvites,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.restablecer_sugerencia), fontSize = 12.sp)
        }

        Button(
            onClick = onSendInvites,
            enabled = members.isNotEmpty() && !isSendingInvites,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (isSendingInvites) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.enviar_invitacion_equipo),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        when {
            invitesSent -> {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pluralStringResource(R.plurals.invitaciones_enviadas, invitedCount, invitedCount),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            inviteError != null -> {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = inviteError, fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
            }
            members.isEmpty() -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.selecciona_companeros),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TeamMemberBubble(label: String, avatar: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        avatar()
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SuggestedTeamCardPreview() {
    TheStudentsTheme {
        Surface {
            SuggestedTeamCard(
                members = TeamMatchPreviewData.candidates.take(2),
                teamSize = 3,
                maxTeamSize = 5,
                teamScore = 74,
                isSendingInvites = false,
                invitesSent = true,
                invitedCount = 2,
                inviteError = null,
                onTeamSizeChange = {},
                onResetSuggestion = {},
                onSendInvites = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
