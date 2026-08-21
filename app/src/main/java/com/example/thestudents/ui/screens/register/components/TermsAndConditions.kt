package com.example.thestudents.ui.screens.register.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun TermsAndConditions(
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = termsAccepted,
            onCheckedChange = onTermsAcceptedChange,
            colors = CheckboxDefaults.colors(checkedColor = colorResource(id = R.color.medium_green))
        )
        
        val annotatedString = buildAnnotatedString {
            append("Acepto los ")
            
            val linkStyles = TextLinkStyles(
                style = SpanStyle(
                    color = colorResource(id = R.color.medium_green),
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium
                )
            )

            withLink(
                LinkAnnotation.Clickable(
                    tag = "terms",
                    styles = linkStyles,
                    linkInteractionListener = {
                        // Handle terms click
                    }
                )
            ) {
                append("Términos de Servicio")
            }
            
            append(" y la ")
            
            withLink(
                LinkAnnotation.Clickable(
                    tag = "privacy",
                    styles = linkStyles,
                    linkInteractionListener = {
                        // Handle privacy click
                    }
                )
            ) {
                append("Política de Privacidad")
            }
            
            append(".")
        }

        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall.copy(color = colorResource(id = R.color.dark_green))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAndConditionsPreview() {
    TheStudentsTheme {
        TermsAndConditions(
            termsAccepted = true,
            onTermsAcceptedChange = {}
        )
    }
}
