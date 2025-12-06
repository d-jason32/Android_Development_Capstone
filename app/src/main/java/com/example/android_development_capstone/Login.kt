package com.example.android_development_capstone

import android.content.ContentValues.TAG
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lint.kotlin.metadata.Visibility
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Login(nav: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // needed for the text boxes
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var visible by remember { mutableStateOf(false) }
    val auth = Firebase.auth

    val activity = context as Activity
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }



    Surface(
        color = MaterialTheme.colorScheme.secondary,  // Pink background from theme
        modifier = Modifier.fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
        ) {


            // Column for the login page
            Column(
                modifier = Modifier.fillMaxSize()
                    .widthIn(max = 600.dp)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {



                PlantPicture()

                Text("Sign in",
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        fontSize = 48.sp
                    ),
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(top = 26.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                        .height(56.dp),
                    value = email,
                    shape = RoundedCornerShape(25.dp),
                    onValueChange = { email = it},
                    label = { Text("Email")},

                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Email Icon",
                            tint = MaterialTheme.colorScheme.outline,
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth()
                        .height(56.dp),
                    value = password,
                    shape = RoundedCornerShape(25.dp),
                    onValueChange = { password = it},
                    label = { Text("Password")},
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black),

                    visualTransformation =
                        if (visible) VisualTransformation.None

                        else PasswordVisualTransformation(),

                    trailingIcon = {
                        IconButton(onClick = { visible = !visible }) {
                            Icon(
                                imageVector = if (visible) Icons.Outlined.Visibility
                                else Icons.Outlined.VisibilityOff,
                                contentDescription = if (visible) "Hide password" else "Show password",
                                tint = MaterialTheme.colorScheme.outline,
                            )
                        }
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button for login
                Button(
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.fillMaxWidth()
                        .heightIn(min = 56.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        // All fields must be filled in
                        if (email.isBlank() || password.isBlank()){
                            Toast.makeText(context, "Fill in all the fields", Toast.LENGTH_SHORT).show()
                        }
                        // Password must be greater than 6 and less than 30 characters
                        else if (password.length < 6 || password.length > 30){
                            Toast.makeText(context, "Password must be greater than 6 and less than 30 characters.", Toast.LENGTH_SHORT).show()
                        }
                        // Email must be valid using regex.
                        else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())){
                            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {

                                        nav.navigate("home") {
                                            popUpTo("intro") { inclusive = true }
                                        }
                                    } else {
                                        val msg = task.exception?.localizedMessage ?: "Authentication failed."
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
                ) {
                    Text("Sign in",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))


                // Button to go to the register account page
                Text("Don't have an account? Register",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        nav.navigate("register") })

            }

        }

    }



}


