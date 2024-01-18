package com.multiplatform.app.ui.designsystem.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.multiplatform.app.ui.theme.MonoColors
import com.multiplatform.app.ui.theme.MonoColors.lightBlack2
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource


@Composable
fun TitleTextBold(
    modifier: Modifier = Modifier,
    string: String){
    Text(
        modifier = modifier,
        text = string,
        style = TextTypographyTokens.titleBold
    )

}

@Composable
fun TitleTextRegular(
    modifier: Modifier = Modifier,
    string: String){
    Text(
        modifier = modifier,
        text = string,
        style = TextTypographyTokens.titleRegular
    )
}

@Composable
fun TitleTextRegular2(
    modifier: Modifier = Modifier,
    string: String){
    Text(
        modifier = modifier,
        text = string,
        style = TextTypographyTokens.titleRegular2
    )
}

@Composable
fun Body2Text(
    modifier: Modifier = Modifier,
    string: String
){
    Text(
        modifier = modifier,
        text = string,
        style = TextTypographyTokens.body2
    )
}

@Composable
fun Body2BoldText(
    modifier: Modifier = Modifier,
    string: String
){
    Text(
        modifier = modifier,
        text = string,
        style = TextTypographyTokens.body2Bold
    )
}

@Composable
fun TitleBoldWhite(modifier: Modifier = Modifier, string: String){
    Text(
        modifier = modifier,
        text = string,
        style = TextTypographyTokens.titleWhite
    )

}

/**
 * Composable function for displaying text with hyperlinks. It uses the ClickableText composable
 * to render hyperlinks and provides a callback for handling hyperlink clicks.
 *
 * @param stringResource A function to retrieve the text with hyperlinks as a string resource.
 *  @example string
 *      "Visit our website [here](https://www.example.com) for more information. " +
 *      "Contact us via [email](mailto:support@example.com) or call us at" +
 *      "[123-456-7890](tel:+1234567890)."
 * @param modifier The modifier for this composable.
 * @param onClick A callback function invoked when a hyperlink is clicked. It provides the clicked
 * token (URL or email) and the type of the link (Hyperlink, Mail, or Tel).

 *
 * @sample
 * ```
 * HyperlinkText(
 *     stringResource = { stringResource(R.string.sample_text_with_links) },
 *     modifier = Modifier.padding(16.dp),
 *     onClick = { token, linkType ->
 *         when (linkType) {
 *             LinkType.HYPERLINK -> openUrl(token)
 *             LinkType.MAIL -> sendEmail(token)
 *             LinkType.TEL -> callNumber(token)
 *         }
 *     }
 * )
 * ```
 */
@Composable
fun HyperlinkText(
    string: String,
    spanStyle: SpanStyle = SpanTypographyTokens.body1,
    modifier: Modifier = Modifier,
    onClick: (token: String, linkType: LinkType)->Unit = {_, _ -> }
) {
    val textWithLinks: String = string
    val text = buildAnnotatedString() {
        val links = Regex("""\[(.*?)\]\((.*?)\)""").findAll(textWithLinks)
        var currentIndex = 0

        links.forEach { result ->
            val (displayText, url) = result.destructured
            val linkStart = result.range.first
            val linkEnd = result.range.last + 1

            // Append non-link text
            append(textWithLinks.subSequence(currentIndex, linkStart))

            // Append clickable text
            pushStringAnnotation("URL", url)
            withStyle(style = SpanTypographyTokens.body1) {
                append(displayText)
            }
            pop()

            currentIndex = linkEnd
        }

        // Append the remaining non-link text
        if (currentIndex < textWithLinks.length) {
            append(textWithLinks.subSequence(currentIndex, textWithLinks.length))
        }
    }

    ClickableText(
        text = text,
        modifier = modifier,
        style = TextTypographyTokens.body1,
        onClick = { offset ->
            text.getStringAnnotations("URL", offset, offset)
                .firstOrNull()?.let { annotation ->
                    val linkType = when {
                        annotation.item.startsWith("http") || annotation.item.startsWith("www") -> LinkType.HYPERLINK
                        annotation.item.startsWith("mailto") -> LinkType.MAIL
                        annotation.item.startsWith("tel") -> LinkType.TEL
                        else -> null
                    }

                    linkType?.let {
                        onClick(annotation.item, it)
                    }
                }
        }
    )
}

enum class LinkType {
    HYPERLINK, MAIL, TEL
}


object TextTypographyTokens {
    val titleBold = TextStyle(
        fontSize = 20.sp,
        lineHeight = 25.sp,
        fontWeight = FontWeight(700),
        color = MonoColors.darkBlack,
        letterSpacing = 0.2.sp,
    )

    val titleRegular = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight(400),
        color = MonoColors.darkBlack,
        letterSpacing = 0.16.sp,
    )

    val titleRegular2 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight(700),
        color = MonoColors.darkBlack,
        letterSpacing = 0.32.sp,
    )

    val body1 = TextStyle(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight(400),
        color = MonoColors.lightBlack,
        letterSpacing = 0.36.sp,
    )

    val body2 = TextStyle(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight(400),
        color = MonoColors.darkBlack,
        letterSpacing = 0.36.sp,
    )

    val body3 = TextStyle(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight(400),
        color = MonoColors.allBlack,
        letterSpacing = 0.36.sp,
    )

    val body2Bold = TextStyle(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight(700),
        color = MonoColors.darkBlack,
        letterSpacing = 0.36.sp,
    )

    val titleWhite =  TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight(700),
        color = MonoColors.white,
        letterSpacing = 0.32.sp,
    )
}

object SpanTypographyTokens {
    val body1 = SpanStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight(400),
        color = lightBlack2,
        textDecoration = TextDecoration.Underline,
        letterSpacing = 0.36.sp,)
}





