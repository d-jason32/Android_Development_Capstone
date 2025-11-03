package com.example.android_development_capstone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    nav: NavHostController
) {
    LaunchedEffect(Unit) {
        delay(2000)
        nav.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.tertiary,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Make the font larger
            Text(
                "LeafLens",
                // Center the words on the splash screen
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onTertiary,

                // makes the font larger
                fontSize = 56.sp,

                // Make the font bold
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.nunito))

            )

            Spacer(modifier = Modifier.height(10.dp))
            
            // Add a picture of the logo
            PlantPicture()


        }
    }
}