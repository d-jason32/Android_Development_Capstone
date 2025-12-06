@file:OptIn(ExperimentalFoundationApi::class)

package com.example.android_development_capstone.game

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.pm.ActivityInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.android_development_capstone.R
import com.example.android_development_capstone.SoundManager


@Composable
fun Game2(modifier: Modifier = Modifier, onWin: () -> Unit = {}) {
    // Lock to landscape mode
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    val coroutineScope = rememberCoroutineScope()

    // Track the sequence of arrow commands
    val commands = remember { mutableStateListOf<String>() }

    // Plant position on the 5x5 grid (0-4 for both row and col)
    var plantRow by remember { mutableIntStateOf(0) }  // Start at bottom-right
    var plantCol by remember { mutableIntStateOf(0) }

    // Finish line position (bottom-left corner)
    val finishRow = 4
    val finishCol = 0
    
    // Barrier positions - plant cannot cross these
    val barriers = listOf(
        Pair(3, 0)
    )

    // Is the animation running?
    var isRunning by remember { mutableStateOf(false) }

    // Grid cell size
    val cellSize = 50.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Command area - one large rectangle
        Text(
            text = "Game 2",
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp)
                .border(2.dp, MaterialTheme.colorScheme.onBackground)
                .background(MaterialTheme.colorScheme.surface)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { event ->
                        event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    },
                    target = remember {
                        object : DragAndDropTarget {
                            override fun onDrop(event: DragAndDropEvent): Boolean {
                                val dragData = event.toAndroidDragEvent()
                                    .clipData
                                    .getItemAt(0)
                                    .text
                                    .toString()
                                commands.add(dragData)
                                return true
                            }
                        }
                    }
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                commands.forEachIndexed { index, arrowType ->
                    val icon = getArrowIcon(arrowType)
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = arrowType,
                            modifier = Modifier
                                .size(40.dp)
                                .dragAndDropSource { _ ->
                                    DragAndDropTransferData(
                                        clipData = ClipData.newPlainText("arrow", arrowType)
                                    )
                                },
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                // Show placeholder if empty
                if (commands.isEmpty()) {
                    Text(
                        text = "Drop arrows here...",
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid and controls side by side
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 5x5 Game Grid
            Box(
                modifier = Modifier
                    .size(cellSize * 5 + 20.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(10.dp)
            ) {
                // Draw grid cells
                Column {
                    repeat(5) { row ->
                        Row {
                            repeat(5) { col ->
                                Box(
                                    modifier = Modifier
                                        .size(cellSize)
                                        .border(1.dp, MaterialTheme.colorScheme.outline)
                                        .background(
                                            if ((row + col) % 2 == 0)
                                                MaterialTheme.colorScheme.surface
                                            else
                                                MaterialTheme.colorScheme.surfaceVariant
                                        )
                                )
                            }
                        }
                    }
                }

                // Finish line image
                Image(
                    painter = painterResource(id = R.drawable.finish),
                    contentDescription = "Finish",
                    modifier = Modifier
                        .offset(x = cellSize * finishCol, y = cellSize * finishRow)
                        .size(cellSize)
                        .padding(2.dp)
                )
                
                // Barrier images
                barriers.forEach { (row, col) ->
                    Image(
                        painter = painterResource(id = R.drawable.x),
                        contentDescription = "Barrier",
                        modifier = Modifier
                            .offset(x = cellSize * col, y = cellSize * row)
                            .size(cellSize)
                            .padding(4.dp)
                    )
                }

                // Animated plant position
                val animatedOffsetX by animateDpAsState(
                    targetValue = cellSize * plantCol,
                    animationSpec = tween(300),
                    label = "plantX"
                )
                val animatedOffsetY by animateDpAsState(
                    targetValue = cellSize * plantRow,
                    animationSpec = tween(300),
                    label = "plantY"
                )

                // Plant image
                Image(
                    painter = painterResource(id = R.drawable.plant),
                    contentDescription = "Plant",
                    modifier = Modifier
                        .offset(x = animatedOffsetX, y = animatedOffsetY)
                        .size(cellSize)
                        .padding(4.dp)
                )
            }

            // Right side: arrows and buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Arrow buttons to drag
                Text(
                    text = "Drag arrows:",
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Left",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier
                            .size(36.dp)
                            .dragAndDropSource { _ ->
                                DragAndDropTransferData(
                                    clipData = ClipData.newPlainText("arrow", "left")
                                )
                            }
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Up",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .size(36.dp)
                                .dragAndDropSource { _ ->
                                    DragAndDropTransferData(
                                        clipData = ClipData.newPlainText("arrow", "up")
                                    )
                                }
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Down",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .size(36.dp)
                                .dragAndDropSource { _ ->
                                    DragAndDropTransferData(
                                        clipData = ClipData.newPlainText("arrow", "down")
                                    )
                                }
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Right",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier
                            .size(36.dp)
                            .dragAndDropSource { _ ->
                                DragAndDropTransferData(
                                    clipData = ClipData.newPlainText("arrow", "right")
                                )
                            }
                    )
                }

                // Control buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StartButton(
                        enabled = !isRunning && commands.isNotEmpty(),
                        onClick = {
                            coroutineScope.launch {
                                isRunning = true
                                // Execute each command in sequence
                                for (command in commands) {
                                    // Calculate new position
                                    val newRow = when (command) {
                                        "up" -> if (plantRow > 0) plantRow - 1 else plantRow
                                        "down" -> if (plantRow < 4) plantRow + 1 else plantRow
                                        else -> plantRow
                                    }
                                    val newCol = when (command) {
                                        "left" -> if (plantCol > 0) plantCol - 1 else plantCol
                                        "right" -> if (plantCol < 4) plantCol + 1 else plantCol
                                        else -> plantCol
                                    }
                                    
                                    // Only move if not blocked by barrier
                                    if (!barriers.contains(Pair(newRow, newCol))) {
                                        plantRow = newRow
                                        plantCol = newCol
                                    }
                                    
                                    delay(500) // Wait for animation

                                    // Check if player reached the finish
                                    if (plantRow == finishRow && plantCol == finishCol) {
                                        delay(300) // Brief pause before navigating
                                        onWin()
                                        break
                                    }
                                }
                                isRunning = false
                            }
                        }
                    )

                    ResetButton(
                        onReset = {
                            plantRow = 0  // Reset to starting position (top-left)
                            plantCol = 0
                            commands.clear()
                        }
                    )
                }
            }
        }
    }
}

