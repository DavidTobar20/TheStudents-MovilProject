package com.example.thestudents.ui.screens.register.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp

import androidx.compose.ui.tooling.preview.Preview
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun TermsAndConditions(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        append("Al registrarte, aceptas nuestros ")
        
        withLink(
            LinkAnnotation.Clickable(
                tag = "terms",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = { onTermsClick() }
            )
        ) {
            append("Términos de Servicio")
        }
        
        append(" y ")
        
        withLink(
            LinkAnnotation.Clickable(
                tag = "privacy",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = { onPrivacyClick() }
            )
        ) {
            append("Política de Privacidad")
        }
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun TermsAndConditionsPreview() {
    TheStudentsTheme(darkTheme = false) {
        TermsAndConditions(onTermsClick = {}, onPrivacyClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TermsAndConditionsDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        TermsAndConditions(onTermsClick = {}, onPrivacyClick = {})
    }
}
