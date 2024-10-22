package com.matin.composeplayground

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Preview(widthDp = 200, heightDp = 300)
@Preview(widthDp = 300, heightDp = 300)
@Composable
fun AdaptiveButtonWidth() {
    Layout(
        content = {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .defaultMinSize(110.dp)
                    .layoutId("first_btn")
            ) {
                Text(text = "First button")
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .defaultMinSize(110.dp)
                    .layoutId("second_btn")
            ) {
                Text(text = "Second button")
            }
        },
        measurePolicy = { measurables, constraints ->
            val firstBtnMeasurable = measurables.find { it.layoutId == "first_btn" }!!
            val secondBtnMeasurable = measurables.find { it.layoutId == "second_btn" }!!

            val firstBtnMinIntrinsicWidth =
                firstBtnMeasurable.minIntrinsicWidth(constraints.maxHeight)
            val secondBtnMinIntrinsicWidth =
                secondBtnMeasurable.minIntrinsicWidth(constraints.maxHeight)

            val showHorizontally =
                firstBtnMinIntrinsicWidth <= constraints.maxWidth / 2 && secondBtnMinIntrinsicWidth <= constraints.maxHeight / 2

            val width = constraints.minWidth
            val height: Int
            val firstBtnPlaceable: Placeable
            val secondBtnPlaceable: Placeable

            if (showHorizontally) {
                val halfWidthConstraints = constraints.copy(
                    minWidth = constraints.maxWidth / 2,
                    maxWidth = constraints.maxWidth / 2
                )
                firstBtnPlaceable = firstBtnMeasurable.measure(halfWidthConstraints)
                secondBtnPlaceable = secondBtnMeasurable.measure(halfWidthConstraints)

                height = max(firstBtnPlaceable.height, secondBtnPlaceable.height)
            } else {
                val fullWidthConstraints = constraints.copy(minWidth = constraints.maxWidth)

                firstBtnPlaceable = firstBtnMeasurable.measure(fullWidthConstraints)
                secondBtnPlaceable = secondBtnMeasurable.measure(fullWidthConstraints)

                height = firstBtnPlaceable.height + secondBtnPlaceable.height
            }

            layout(width, height) {
                if (showHorizontally) {
                    firstBtnPlaceable.placeRelative(
                        0,
                        (height - firstBtnPlaceable.height) / 2
                    )
                    secondBtnPlaceable.placeRelative(
                        width / 2,
                        (height - secondBtnPlaceable.height) / 2
                    )
                } else {
                    firstBtnPlaceable.placeRelative(
                        0,
                        0
                    )
                    secondBtnPlaceable.placeRelative(
                        0,
                        height - secondBtnPlaceable.height
                    )
                }
            }
        },
        modifier = Modifier.wrapContentHeight()
    )
}