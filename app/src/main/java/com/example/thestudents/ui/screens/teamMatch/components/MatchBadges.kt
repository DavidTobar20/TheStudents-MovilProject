package com.example.thestudents.ui.screens.teamMatch.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.MatchReason
import com.example.thestudents.data.MatchTier
import com.example.thestudents.ui.theme.TheStudentsTheme

/** Anillo de progreso con el porcentaje de match en el centro. */
@Composable
fun MatchScoreRing(
    score: Int,
    tier: MatchTier,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    strokeWidth: Dp = 5.dp,
    fontSize: TextUnit = 15.sp
) {
    val color = tierColor(tier)
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { score.coerceIn(0, 100) / 100f },
            modifier = Modifier.size(size),
            color = color,
            strokeWidth = strokeWidth,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "$score%",
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/** Etiqueta pequeña "Match ideal", "Buen match", etc. */
@Composable
fun MatchTierChip(tier: MatchTier, modifier: Modifier = Modifier) {
    val color = tierColor(tier)
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = color.copy(alpha = 0.14f),
        contentColor = color
    ) {
        Text(
            text = tierLabel(tier),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

/** Razones del match en forma de etiquetas ("Lo sigues", "★ 4,8 · 12 reseñas"...). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatchReasonChips(
    reasons: List<MatchReason>,
    modifier: Modifier = Modifier,
    maxReasons: Int = 3
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        reasons.take(maxReasons).forEach { reason ->
            val negative = isNegativeReason(reason)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (negative) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = if (negative) MaterialTheme.colorScheme.onErrorContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    text = reasonText(reason),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchBadgesPreview() {
    TheStudentsTheme {
        Surface {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MatchScoreRing(score = 82, tier = MatchTier.IDEAL)
                MatchTierChip(tier = MatchTier.IDEAL)
                MatchTierChip(tier = MatchTier.RISK)
            }
        }
    }
}
