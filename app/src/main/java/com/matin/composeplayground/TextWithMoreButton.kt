package com.matin.composeplayground

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.round
import kotlin.math.max
import kotlin.text.Typography.ellipsis

@Preview(widthDp = 200, heightDp = 300)
@Preview(widthDp = 300, heightDp = 300)
@Preview(widthDp = 400, heightDp = 300)
@Preview(widthDp = 500, heightDp = 300)
@Preview(widthDp = 600, heightDp = 300)
@Composable
fun TextWithMoreButton(modifier: Modifier = Modifier) {
    val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    val maxLines = 5
    val textMeasurer = rememberTextMeasurer()
    var fullTextLayoutResult: TextLayoutResult? by remember { mutableStateOf(null) }
    var ellipsizedTextResult: TextLayoutResult? by remember { mutableStateOf(null) }
    var showButton by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.wrapContentSize()) {
        val contentColor = LocalContentColor.current
        val style = LocalTextStyle.current
        Layout(
            content = {
                Text(
                    text = text,
                    style = style,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = maxLines,
                    modifier = Modifier
                        .layoutId("text")
                        .drawBehind {
                            drawText(
                                if (showButton) {
                                    ellipsizedTextResult!!
                                } else {
                                    fullTextLayoutResult!!
                                },
                                 contentColor
                            )
                        }
                    ,
                    onTextLayout = {
                        fullTextLayoutResult = it
                    },
                    color = Color.Transparent
                )

                Button(
                    modifier = Modifier.layoutId("more"),
                    onClick = {}
                ) {
                    Text("more")
                }
            },
            measurePolicy = { measurables, constraints ->
                val textMeasurable =
                    measurables.find { it.layoutId == "text" }
                val buttonMeasurable =
                    measurables.find { it.layoutId == "more" }

                val textPlaceable = textMeasurable!!.measure(constraints)
                val buttonPlaceable = buttonMeasurable!!.measure(constraints)

                val newFullTextLayoutResult = fullTextLayoutResult!!

                showButton =
                    newFullTextLayoutResult.isLineEllipsized(newFullTextLayoutResult.lineCount - 1)

                if (showButton) {
                    val ellipsisTextLayoutResult = textMeasurer.measure(
                        ellipsis.toString(),
                        style = style
                    )

                    val lastLineTextWidth =
                        constraints.maxWidth - buttonPlaceable.width - ellipsisTextLayoutResult.size.width

                    val lastCharOffset = newFullTextLayoutResult.getOffsetForPosition(
                        Offset(
                            lastLineTextWidth.toFloat(),
                            newFullTextLayoutResult.getLineBottom(maxLines - 1)
                        )
                    ) - 1

                    val newEllipsisTextLayoutResult = textMeasurer.measure(
                        text.substring(0, lastCharOffset) + ellipsis,
                        style = style,
                        constraints = constraints
                    )

                    ellipsizedTextResult = newEllipsisTextLayoutResult

                    val lastLineEndBoundingBox = newEllipsisTextLayoutResult.getBoundingBox(
                        newEllipsisTextLayoutResult.getLineEnd(
                            newEllipsisTextLayoutResult.lineCount - 1,
                            visibleEnd = true
                        ) - 1
                    )
                    val lastLineRelativeOffset = Offset(
                        when (layoutDirection) {
                            LayoutDirection.Ltr ->
                                lastLineEndBoundingBox.right
                            LayoutDirection.Rtl ->
                                newEllipsisTextLayoutResult.size.width - lastLineEndBoundingBox.left
                        },
                        lastLineEndBoundingBox.top,
                    ).round()

                    val width = constraints.maxWidth
                    val height = max(
                        newFullTextLayoutResult.size.height,
                        lastLineRelativeOffset.y + buttonPlaceable.height
                    )
                    val buttonX = constraints.maxWidth - buttonPlaceable.width
                    val buttonY = lastLineRelativeOffset.y

                    layout(width, height) {
                        textPlaceable.placeRelative(0, 0)
                        buttonPlaceable.placeRelative(buttonX, buttonY)
                    }
                } else {
                    layout(newFullTextLayoutResult.size.width, newFullTextLayoutResult.size.height) {
                        textPlaceable.placeRelative(0, 0)
                    }
                }
            },
        )
    }
}