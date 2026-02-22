package org.aisee.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import org.aisee.app.presentation.beforestart.BeforeStartScreen
import org.aisee.app.presentation.main.MainScreen
import org.aisee.app.presentation.signin.ForgotPasswordScreen
import org.aisee.app.presentation.signin.SignInScreen
import org.aisee.app.presentation.signup.SignUpScreen
import org.aisee.app.presentation.signup.SignUpWithEmailScreen
import org.aisee.app.presentation.splash.SplashScreen

@Composable
fun AiSeeNavHost() {
    val backStack = remember { mutableStateListOf<Any>(SplashRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                SplashRoute -> NavEntry(key) {
                    SplashScreen(
                        onNavigateToMain = {
                            backStack[0] = BeforeStartRoute
                        }
                    )
                }
                BeforeStartRoute -> NavEntry(key) {
                    BeforeStartScreen(
                        onAgreeAndContinue = {
                            backStack[0] = SignUpRoute
                        }
                    )
                }
                SignUpRoute -> NavEntry(key) {
                    SignUpScreen(
                        onContinueWithGoogle = {
                            backStack[0] = MainRoute
                        },
                        onContinueWithEmail = {
                            backStack.add(SignUpWithEmailRoute)
                        },
                        onAlreadyHaveAccount = {
                            backStack.add(SignInRoute)
                        }
                    )
                }
                SignUpWithEmailRoute -> NavEntry(key) {
                    SignUpWithEmailScreen(
                        onCreateAccount = { _, _, _ ->
                            backStack.clear()
                            backStack.add(MainRoute)
                        },
                        onSignUpWithGoogle = {
                            backStack.clear()
                            backStack.add(MainRoute)
                        }
                    )
                }
                SignInRoute -> NavEntry(key) {
                    SignInScreen(
                        onSignIn = { _, _ ->
                            backStack.clear()
                            backStack.add(MainRoute)
                        },
                        onForgotPassword = {
                            backStack.add(ForgotPasswordRoute)
                        }
                    )
                }
                ForgotPasswordRoute -> NavEntry(key) {
                    ForgotPasswordScreen(
                        onResetPassword = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                MainRoute -> NavEntry(key) {
                    MainScreen()
                }
                else -> NavEntry(Unit) { Text("Unknown") }
            }
        }
    )
}
