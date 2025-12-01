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
import androidx.navigation.toRoute
import com.example.android_development_capstone.game.Congrats1_2
import com.example.android_development_capstone.game.Game1
import com.example.android_development_capstone.game.Game2
import com.example.android_development_capstone.game.Game3
import com.example.android_development_capstone.subject.SubjectScreen
import com.example.android_development_capstone.question.QuestionScreen
import com.example.android_development_capstone.question.AddQuestionScreen
import com.example.android_development_capstone.question.EditQuestionScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

// Main activity for the application
// The main activity for the app will start off at the splash screen
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val startDestination = "home"
        SoundManager.init(this)

        setContent {
            Android_Development_CapstoneTheme {
                MyApp(modifier = Modifier.fillMaxSize(), startDestination = startDestination)

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
        MyApp(Modifier.fillMaxSize(), startDestination = "onboarding")
    }
}


// Main app function to allow for navigation
@Composable
fun MyApp(modifier: Modifier = Modifier, startDestination: String) {
    // Needed to create a navigation controller
    val nav = rememberNavController()

    Surface(modifier) {
        NavHost(
            navController = nav,
            // makes the app start at the login screen
            startDestination = startDestination
            //startDestination = "home"
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
            composable("home") { Home(nav) }

            composable("game") { Game(nav) }

            composable("Game1") { 
                Game1(
                    onWin = { nav.navigate("Congrats1_2") }
                )
            }

            composable("Game2") {
                Game2(
                    onWin = { nav.navigate("Congrats1_2") }
                )
            }
            
            composable("Congrats1_2") { Congrats1_2(nav) }
            
            composable("Game3") {
                Game3(
                    onWin = { nav.navigate("home") }  // Final level - go home on win
                )
            }


            composable("subjectscreen") { 
                SubjectScreen(
                    navController = nav,
                    onSubjectClick = { subject ->
                        nav.navigate(Routes.Question(subjectId = subject.id, showLastQuestion = false))
                    }
                )
            }
            
            composable<Routes.Question> { backStackEntry ->
                val route = backStackEntry.toRoute<Routes.Question>()
                QuestionScreen(
                    onUpClick = { nav.popBackStack() },
                    onAddClick = { 
                        nav.navigate(Routes.AddQuestion(subjectId = route.subjectId))
                    },
                    onEditClick = { questionId ->
                        nav.navigate(Routes.EditQuestion(questionId = questionId))
                    }
                )
            }
            
            composable<Routes.AddQuestion> { backStackEntry ->
                val route = backStackEntry.toRoute<Routes.AddQuestion>()
                AddQuestionScreen(
                    onUpClick = { nav.popBackStack() },
                    onSaveClick = { nav.popBackStack() }
                )
            }
            
            composable<Routes.EditQuestion> { backStackEntry ->
                val route = backStackEntry.toRoute<Routes.EditQuestion>()
                EditQuestionScreen(
                    onUpClick = { nav.popBackStack() },
                    onSaveClick = { nav.popBackStack() }
                )
            }







        }
    }
}