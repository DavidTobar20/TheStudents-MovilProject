package com.example.thestudents.ui.screens.teamMatch.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.TeamMatchCandidate
import com.example.thestudents.ui.components.GoldRingAvatar
import com.example.thestudents.ui.components.luxuryCard
import com.example.thestudents.ui.theme.TheStudentsTheme

/** Tarjeta de un compañero recomendado, con su % de match y por qué se recomienda. */
@Composable
fun MatchCandidateCard(
    rank: Int,
    candidate: TeamMatchCandidate,
    isSelected: Boolean,
    canAdd: Boolean,
    onToggleClick: () -> Unit,
    onStudentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val student = candidate.student
    val match = candidate.match
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .luxuryCard(shape)
            .then(
                if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape)
                else Modifier
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "#$rank",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.width(28.dp)
            )
            GoldRingAvatar(
                initials = student.initials,
                profileImage = student.profileImage,
                backgroundColor = student.profileColor,
                size = 48.dp,
                fontSize = 17.sp,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onStudentClick)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onStudentClick)
            ) {
                Text(
                    text = student.name.ifBlank { student.username },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (student.username.isNotBlank()) {
                    Text(
                        text = "@${student.username}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                MatchTierChip(tier = match.tier)
            }
            Spacer(modifier = Modifier.width(8.dp))
            MatchScoreRing(score = match.score, tier = match.tier)
        }

        if (match.reasons.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            MatchReasonChips(reasons = match.reasons, modifier = Modifier.padding(start = 28.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (isSelected) {
                Button(
                    onClick = onToggleClick,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = stringResource(R.string.en_equipo), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                OutlinedButton(
                    onClick = onToggleClick,
                    enabled = canAdd,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    if (canAdd) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = stringResource(if (canAdd) R.string.agregar_al_equipo else R.string.equipo_completo),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchCandidateCardPreview() {
    TheStudentsTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MatchCandidateCard(
                    rank = 1,
                    candidate = TeamMatchPreviewData.candidates[0],
                    isSelected = true,
                    canAdd = true,
                    onToggleClick = {},
                    onStudentClick = {}
                )
                MatchCandidateCard(
                    rank = 4,
                    candidate = TeamMatchPreviewData.candidates[3],
                    isSelected = false,
                    canAdd = true,
                    onToggleClick = {},
                    onStudentClick = {}
                )
            }
        }
    }
}
