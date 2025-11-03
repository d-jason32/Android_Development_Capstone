package com.example.android_development_capstone

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_development_capstone.ui.theme.Android_Development_CapstoneTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Main activity for the application
// The main activity for the app will start off at the splash screen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android_Development_CapstoneTheme {
                MyApp(modifier = Modifier.fillMaxSize())

            }
        }
    }
}





// Function to display the splash screen
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    Android_Development_CapstoneTheme {
        OnboardingScreen(
            onContinueClicked = {},
            onRegisterClicked = {},
        )
    }
}


@Preview
@Composable
fun MyAppPreview() {
    Android_Development_CapstoneTheme {
        MyApp(Modifier.fillMaxSize())
    }
}


// Main app function to allow for navigation
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    // Needed to create a navigation controller
    val nav = rememberNavController()

    Surface(modifier) {
        NavHost(
            navController = nav,
            // makes the app start at the login screen
            startDestination = "splashscreen"
        ) {
            composable("onboarding") {

                /*
                If the button on the splash screen is clicked,
                it will go to the login screen.

                If the button on the login screen is clicked, it will go
                to the register screen.

                If the button on the register screen is clicked, it will go onto the
                login screen.
                 */

                OnboardingScreen(
                    onContinueClicked = {
                        nav.navigate("login") {

                        }
                    },

                    onRegisterClicked = {
                        nav.navigate("register") {

                        }
                    }
                )
            }
            // Route for the login screen
            composable("login") { Login(nav) }

            // route for the register screen
            composable("register") { RegisterScreen(nav) }

            // route for the splash screen
            composable("splashscreen") { SplashScreen(nav) }

            // route for the home page
            //composable("home") { Home(nav) }




        }
    }
}