package com.example.thestudents.ui.screens.teamMatch.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.example.thestudents.R
import com.example.thestudents.data.MatchReason
import com.example.thestudents.data.MatchTier
import com.example.thestudents.data.repository.TeamMatchScorer
import com.example.thestudents.ui.theme.extended
import java.util.Locale

// Textos y colores de "Arma tu equipo" en un solo lugar para reutilizarlos en las tarjetas

/** 4.5 -> "4,5", 4.0 -> "4" */
fun formatStars(value: Double): String {
    val rounded = Math.round(value * 10) / 10.0
    return if (rounded % 1.0 == 0.0) rounded.toInt().toString()
    else String.format(Locale.forLanguageTag("es"), "%.1f", rounded)
}

/** Nivel del equipo completo a partir de su puntaje promedio. */
fun tierForScore(score: Int): MatchTier = when {
    score >= TeamMatchScorer.IDEAL_THRESHOLD -> MatchTier.IDEAL
    score >= TeamMatchScorer.GOOD_THRESHOLD -> MatchTier.GOOD
    else -> MatchTier.TO_DISCOVER
}

@Composable
fun tierLabel(tier: MatchTier): String = when (tier) {
    MatchTier.IDEAL -> stringResource(R.string.tier_ideal)
    MatchTier.GOOD -> stringResource(R.string.tier_bueno)
    MatchTier.TO_DISCOVER -> stringResource(R.string.tier_por_conocer)
    MatchTier.RISK -> stringResource(R.string.tier_riesgo)
}

@Composable
fun tierColor(tier: MatchTier): Color = when (tier) {
    MatchTier.IDEAL -> MaterialTheme.extended.rating
    MatchTier.GOOD -> MaterialTheme.colorScheme.secondary
    MatchTier.TO_DISCOVER -> MaterialTheme.colorScheme.onSurfaceVariant
    MatchTier.RISK -> MaterialTheme.colorScheme.error
}

@Composable
fun reasonText(reason: MatchReason): String = when (reason) {
    is MatchReason.Reputation -> pluralStringResource(
        R.plurals.match_razon_reputacion, reason.count, formatStars(reason.average), reason.count
    )
    is MatchReason.StrongInCourse -> pluralStringResource(
        R.plurals.match_razon_materia, reason.count, formatStars(reason.average), reason.count
    )
    is MatchReason.WorkedTogetherWell ->
        stringResource(R.string.match_razon_trabajaron_bien, formatStars(reason.myRating))
    is MatchReason.WorkedTogetherBadly ->
        stringResource(R.string.match_razon_trabajaron_mal, formatStars(reason.myRating))
    is MatchReason.RatedMeWell ->
        stringResource(R.string.match_razon_te_califico, formatStars(reason.rating))
    is MatchReason.SharedCourses -> pluralStringResource(
        R.plurals.match_razon_materias_compartidas, reason.count, reason.count
    )
    MatchReason.YouFollow -> stringResource(R.string.match_razon_lo_sigues)
    MatchReason.FewReviews -> stringResource(R.string.match_razon_pocas_resenas)
    MatchReason.LowReviews -> stringResource(R.string.match_razon_resenas_bajas)
}

/** Las razones negativas se pintan en rojo para que resalten. */
fun isNegativeReason(reason: MatchReason): Boolean =
    reason is MatchReason.WorkedTogetherBadly || reason is MatchReason.LowReviews
