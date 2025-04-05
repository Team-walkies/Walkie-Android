import android.graphics.Typeface
import android.text.Html
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

sealed class StyleRule {
    data class Underline(val start: Int, val end: Int) : StyleRule()
    data class Bold(val start: Int, val end: Int) : StyleRule()
    data class ColorText(val start: Int, val end: Int, val color: Color) : StyleRule()
}

/**
 * 스타일 규칙을 적용하는 기본 확장 함수
 */
fun String.styleText(rules: List<StyleRule>): AnnotatedString {
    return buildAnnotatedString {
        append(this@styleText)

        rules.forEach { rule ->
            when (rule) {
                is StyleRule.Underline -> {
                    addStyle(
                        style = SpanStyle(textDecoration = TextDecoration.Underline),
                        start = rule.start,
                        end = rule.end
                    )
                }

                is StyleRule.Bold -> {
                    addStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold),
                        start = rule.start,
                        end = rule.end
                    )
                }

                is StyleRule.ColorText -> {
                    addStyle(
                        style = SpanStyle(color = rule.color),
                        start = rule.start,
                        end = rule.end
                    )
                }

                else -> {}
            }
        }
    }
}

/**
 * 전체 텍스트에 밑줄 적용
 */
fun String.underlineAll(): AnnotatedString {
    return this.styleText(listOf(StyleRule.Underline(0, this.length)))
}

/**
 * 서식 문자열에서 포맷 인자에 볼드 적용
 */
fun String.boldFormatArg(arg: String): AnnotatedString {
    val start = this.indexOf(arg)
    if (start == -1) return AnnotatedString(this)

    return this.styleText(
        listOf(StyleRule.Bold(start, start + arg.length))
    )
}

/**
 * 문자열의 특정 부분에 색상 적용
 */
fun String.applyColorToSubstring(substring: String, color: Color): AnnotatedString {
    val start = this.indexOf(substring)
    if (start == -1) return AnnotatedString(this)

    return runCatching {
        this.styleText(
            listOf(StyleRule.ColorText(start, start + substring.length, color))
        )
    }.getOrElse { AnnotatedString(this) }
}

/**
 * 특정 접두사와 나머지 부분에 다른 색상 적용
 */
fun String.applyColorPrefix(prefix: String, prefixColor: Color, restColor: Color): AnnotatedString {
    if (!this.startsWith(prefix)) return AnnotatedString(this)

    return this.styleText(
        listOf(
            StyleRule.ColorText(0, prefix.length, prefixColor),
            StyleRule.ColorText(prefix.length, this.length, restColor)
        )
    )
}

/**
 * 복합 스타일 적용 (여러 규칙 동시에)
 */
fun String.applyMultiStyle(vararg rules: StyleRule): AnnotatedString {
    return this.styleText(rules.toList())
}

/**
 * 밑줄 스타일 적용 확장 함수
 */
fun String.withUnderline(): AnnotatedString = this.underlineAll()

/**
 * 특정 부분을 볼드체로 만드는 확장 함수
 */
fun String.withBold(substring: String): AnnotatedString {
    val start = this.indexOf(substring)
    if (start == -1) return AnnotatedString(this)

    return this.styleText(
        listOf(StyleRule.Bold(start, start + substring.length))
    )
}

/**
 * 특정 부분에 색상 적용하는 확장 함수
 */
fun String.withColor(substring: String, color: Color): AnnotatedString {
    return this.applyColorToSubstring(substring, color)
}

/**
 * 접두사와 나머지 부분에 다른 색상 적용하는 확장 함수
 */
fun String.withColoredPrefix(
    prefix: String,
    prefixColor: Color,
    restColor: Color
): AnnotatedString {
    return this.applyColorPrefix(prefix, prefixColor, restColor)
}

fun String.toAnnotatedString(): AnnotatedString {
    val text = replace("\n", "<br>")
    val spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    return buildAnnotatedString {
        append(spanned.toString())
        spanned.getSpans(0, spanned.length, Any::class.java)
            .forEach { span ->
                val start = spanned.getSpanStart(span)
                val end = spanned.getSpanEnd(span)
                when (span) {
                    is StyleSpan -> when (span.style) {
                        Typeface.BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
                        Typeface.ITALIC -> SpanStyle(fontStyle = FontStyle.Italic)
                        Typeface.BOLD_ITALIC -> SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                        )

                        else -> null
                    }

                    is StrikethroughSpan -> SpanStyle(textDecoration = TextDecoration.LineThrough)
                    is UnderlineSpan -> SpanStyle(textDecoration = TextDecoration.Underline)
                    is ForegroundColorSpan -> SpanStyle(color = Color(span.foregroundColor))
                    else -> null
                }?.let { style ->
                    addStyle(style, start, end)
                }
            }
    }
}

fun String.hasSpecialCharacters(): Boolean {
    // 알파벳, 숫자만 허용. 그 외는 특수문자로 간주
    return this.any { !it.isLetterOrDigit() } && isNotEmpty()
}
