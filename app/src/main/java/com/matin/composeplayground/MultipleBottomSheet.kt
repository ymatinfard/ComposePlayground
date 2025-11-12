package com.matin.composeplayground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleBottomSheet(modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var openBottomSheet by remember { mutableStateOf(BottomSheets.No) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { openBottomSheet = BottomSheets.ONE }) {
            Text("Open Bottom Sheet")
        }

        when (openBottomSheet) {
            BottomSheets.ONE -> BottomSheetOne(
                action = { openBottomSheet = it },
                scope = scope,
                sheetState = sheetState
            )

            BottomSheets.TWO -> BottomSheetTwo(
                action = { openBottomSheet = it },
                scope = scope,
                sheetState = sheetState
            )

            BottomSheets.THREE -> BottomSheetThree(
                action = { openBottomSheet = it },
                scope = scope,
                sheetState = sheetState
            )

            BottomSheets.FOUR -> BottomSheetFour(
                action = { openBottomSheet = it },
                scope = scope,
                sheetState = sheetState
            )

            BottomSheets.No -> Unit
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BottomSheetOne(
    action: (BottomSheets) -> Unit,
    scope: CoroutineScope,
    sheetState: SheetState
) {
    ModalBottomSheet(onDismissRequest = { action(BottomSheets.No) }, sheetState = sheetState) {
        BottomSheetLayout(
            title = "Hello from Bottom Sheet One!",
            next = BottomSheets.TWO,
            action = action,
            scope = scope,
            sheetState = sheetState
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BottomSheetTwo(
    action: (BottomSheets) -> Unit,
    scope: CoroutineScope,
    sheetState: SheetState
) {
    ModalBottomSheet(onDismissRequest = { action(BottomSheets.No) }, sheetState = sheetState) {
        BottomSheetLayout(
            title = "Hello from Bottom Sheet Two!",
            next = BottomSheets.THREE,
            action = action,
            scope = scope,
            sheetState = sheetState
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BottomSheetThree(
    action: (BottomSheets) -> Unit,
    scope: CoroutineScope,
    sheetState: SheetState
) {
    ModalBottomSheet(onDismissRequest = { action(BottomSheets.No) }, sheetState = sheetState) {
        BottomSheetLayout(
            title = "Hello from Bottom Sheet Three!",
            next = BottomSheets.FOUR,
            action = action,
            scope = scope,
            sheetState = sheetState
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BottomSheetFour(
    action: (BottomSheets) -> Unit,
    scope: CoroutineScope,
    sheetState: SheetState
) {
    ModalBottomSheet(onDismissRequest = { action(BottomSheets.No) }, sheetState = sheetState) {
        BottomSheetLayout(
            title = "Hello from Bottom Sheet Four!",
            next = BottomSheets.ONE,
            action = action,
            scope = scope,
            sheetState = sheetState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetLayout(
    title: String,
    next: BottomSheets,
    action: (BottomSheets) -> Unit,
    scope: CoroutineScope,
    sheetState: SheetState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { action(next) }) {
                Text("BottomSheet ${next.name}")
            }
            Button(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        action(BottomSheets.No)
                    }
                }
            }) {
                Text("Close")
            }
        }
    }
}

enum class BottomSheets {
    No,
    ONE,
    TWO,
    THREE,
    FOUR
}
