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


@Composable
fun Game1(modifier: Modifier = Modifier, onWin: () -> Unit = {}) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    
    val coroutineScope = rememberCoroutineScope()
    val commands = remember { mutableStateListOf<String>() }
    var plantRow by remember { mutableIntStateOf(4) }
    var plantCol by remember { mutableIntStateOf(0) }
    
    // Finish line position
    val finishRow = 0
    val finishCol = 4
    
    // Is the animation running?
    var isRunning by remember { mutableStateOf(false) }
    
    // Grid cell size
    val cellSize = 50.dp
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Game 1",
            color = MaterialTheme.colorScheme.onPrimary,
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
                                tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                // Show placeholder if empty
                if (commands.isEmpty()) {
                    Text(
                        text = "Drop arrows here...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
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
                
                // Finish line image (top-right corner)
                Image(
                    painter = painterResource(id = R.drawable.finish),
                    contentDescription = "Finish",
                    modifier = Modifier
                        .offset(x = cellSize * finishCol, y = cellSize * finishRow)
                        .size(cellSize)
                        .padding(2.dp)
                )
                
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
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Left",
                        tint = MaterialTheme.colorScheme.onPrimary,
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
                            tint = MaterialTheme.colorScheme.onPrimary,
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
                            tint = MaterialTheme.colorScheme.onPrimary,
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
                        tint = MaterialTheme.colorScheme.onPrimary,
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
                                    when (command) {
                                        "left" -> if (plantCol > 0) plantCol--
                                        "right" -> if (plantCol < 4) plantCol++
                                        "up" -> if (plantRow > 0) plantRow--
                                        "down" -> if (plantRow < 4) plantRow++
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
                            plantRow = 4  // Reset to bottom-left
                            plantCol = 0
                            commands.clear()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ResetButton(onReset: () -> Unit) {
    Button(
        onClick = onReset,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text("Reset")
    }
}

@Composable
fun StartButton(enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Text("Start")
    }
}

fun getArrowIcon(direction: String): ImageVector? {
    return when (direction) {
        "left" -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
        "right" -> Icons.AutoMirrored.Filled.KeyboardArrowRight
        "up" -> Icons.Filled.KeyboardArrowUp
        "down" -> Icons.Filled.KeyboardArrowDown
        else -> null
    }
}

@Composable
fun WinDialog(onGoHome: () -> Unit) {
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🎉",
                    fontSize = 48.sp
                )
                
                Text(
                    text = "Congratulations!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Text(
                    text = "You guided the plant to the finish!",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = onGoHome,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Go Home", fontSize = 18.sp)
                }
            }
        }
    }
}