package com.matin.composeplayground

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

@Preview(widthDp = 200, heightDp = 200)
@Preview(widthDp = 300, heightDp = 200)
@Preview(widthDp = 300, heightDp = 400)
@Preview(widthDp = 300, heightDp = 400, locale = "ar")
@Composable
fun TextWithBadge() {
    val text = "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."

    Surface(modifier = Modifier.wrapContentSize()) {
        var textLayoutResult: TextLayoutResult? by remember {
            mutableStateOf(null)
        }

        Layout(
            content = {
                Text(text = text, modifier = Modifier.layoutId("text"), onTextLayout = {
                    textLayoutResult = it
                })
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    modifier = Modifier.layoutId("badge").size(24.dp),
                    contentDescription = null
                )
            },
            measurePolicy = { measureables, constraints ->
                val textPlaceable =
                    measureables.find { it.layoutId == "text" }!!.measure(constraints)
                val badgePlaceable =
                    measureables.find { it.layoutId == "badge" }!!.measure(constraints)

                val badgeWidth = badgePlaceable.width
                val badgeHeight = badgePlaceable.height

                val newTextLayoutResult = textLayoutResult!!

                val lastLineEndBoundingBox = newTextLayoutResult.getBoundingBox(
                    newTextLayoutResult.getLineEnd(
                        newTextLayoutResult.lineCount - 1,
                        visibleEnd = true
                    ) - 1
                )

                val lastLineRelativeOffset = IntOffset(
                    when (layoutDirection) {
                        LayoutDirection.Ltr -> lastLineEndBoundingBox.right
                        LayoutDirection.Rtl -> newTextLayoutResult.size.width - lastLineEndBoundingBox.left
                    }.roundToInt(), lastLineEndBoundingBox.top.roundToInt()
                )

                val displayBadgeInLastLine =
                    constraints.maxWidth - lastLineRelativeOffset.x >= badgeWidth

                val width: Int
                val height: Int
                val badgeX: Int
                val badgeY: Int

                if (displayBadgeInLastLine) {
                    width = max(
                        newTextLayoutResult.size.width,
                        lastLineRelativeOffset.x + badgeWidth
                    )
                    height = max(
                        newTextLayoutResult.size.height,
                        lastLineRelativeOffset.y + badgeHeight
                    )
                    badgeX = lastLineRelativeOffset.x
                    badgeY = lastLineRelativeOffset.y
                } else {
                    width = max(newTextLayoutResult.size.width, badgeWidth)
                    height = newTextLayoutResult.size.height + badgeHeight
                    badgeX = max(0, newTextLayoutResult.size.width - badgeWidth)
                    badgeY = newTextLayoutResult.size.height
                }

                layout(width, height) {
                    textPlaceable.placeRelative(0, 0)
                    badgePlaceable.placeRelative(badgeX, badgeY)
                }
            }
        )
    }
}