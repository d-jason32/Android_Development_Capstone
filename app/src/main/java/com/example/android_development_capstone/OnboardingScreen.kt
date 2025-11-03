package com.example.android_development_capstone

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.TextStyle

@Composable
fun OnboardingScreen(
    // After you click continue you will go to the login screen
    onContinueClicked: () -> Unit,
    // After you click you will go to the login screen
    onRegisterClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(

        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()


    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(22.dp)
                .fillMaxSize()
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(22.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Text(
                    text = "LeafLens",
                    fontWeight = FontWeight.Bold,
                    fontSize = 56.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,

                    fontFamily = FontFamily(Font(R.font.nunito))

                )

                Spacer(modifier = Modifier.height(10.dp))

                PlantPicture()

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            )
            {
                OutlinedButton(
                    onClick = {
                        onContinueClicked() },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Register")
                }

                OutlinedButton(
                    onClick = {
                        onRegisterClicked() },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )

                ) {
                    Text(
                        "Sign in",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }


            }
        }
    }

}


