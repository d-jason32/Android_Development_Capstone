package com.example.android_development_capstone.game

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.android_development_capstone.BottomNavBar
import com.example.android_development_capstone.NiceJob
import com.example.android_development_capstone.PlantPicture
import com.example.android_development_capstone.R
import com.example.android_development_capstone.SoundManager
import kotlinx.coroutines.delay

@Composable
fun Won(nav: NavHostController, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(1000)
        SoundManager.play("wow")
    }

    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.fillMaxSize(),


        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {



                Image(painter = painterResource(id = R.drawable.won),
                    contentDescription = null,
                    Modifier.size(200.dp)
                )

                Image(painter = painterResource(id = R.drawable.plant),
                    contentDescription = null,
                    Modifier.size(200.dp)
                )

                Spacer(Modifier.height(20.dp))


                OutlinedButton(
                    onClick = {
                        SoundManager.play("button")
                        nav.navigate("home") {
                            popUpTo(nav.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        "Go home!",
                        fontSize = 16.sp
                    )
                }





            }
        }
    }
}