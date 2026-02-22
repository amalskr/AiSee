package org.aisee.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import org.aisee.app.presentation.beforestart.BeforeStartScreen
import org.aisee.app.presentation.main.MainScreen
import org.aisee.app.presentation.terms.PrivacyAndTermsScreen
import org.aisee.app.presentation.permission.PermissionScreen
import org.aisee.app.presentation.permission.hasRequiredPermissions
import org.aisee.app.presentation.settings.SettingsScreen
import org.aisee.app.presentation.signin.ForgotPasswordScreen
import org.aisee.app.presentation.signin.SignInScreen
import org.aisee.app.presentation.signup.SignUpScreen
import org.aisee.app.presentation.signup.SignUpWithEmailScreen
import org.aisee.app.presentation.splash.SplashScreen

@Composable
fun AiSeeNavHost() {
    val backStack = remember { mutableStateListOf<Any>(SplashRoute) }
    val context = LocalContext.current

    val navigateToMain: () -> Unit = {
        backStack.clear()
        if (hasRequiredPermissions(context)) {
            backStack.add(MainRoute)
        } else {
            backStack.add(PermissionRoute)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                SplashRoute -> NavEntry(key) {
                    SplashScreen(
                        onFinished = {
                            backStack[0] = BeforeStartRoute
                        }
                    )
                }
                BeforeStartRoute -> NavEntry(key) {
                    BeforeStartScreen(
                        onAgreeAndContinue = {
                            backStack[0] = PrivacyAndTermsRoute
                        }
                    )
                }
                PrivacyAndTermsRoute -> NavEntry(key) {
                    PrivacyAndTermsScreen(
                        onNext = {
                            backStack[0] = SignUpRoute
                        }
                    )
                }
                SignUpRoute -> NavEntry(key) {
                    SignUpScreen(
                        onContinueWithGoogle = navigateToMain,
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
                        onCreateAccount = { _, _, _ -> navigateToMain() },
                        onSignUpWithGoogle = navigateToMain
                    )
                }
                SignInRoute -> NavEntry(key) {
                    SignInScreen(
                        onSignIn = { _, _ -> navigateToMain() },
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
                PermissionRoute -> NavEntry(key) {
                    PermissionScreen(
                        onPermissionsGranted = {
                            backStack[0] = MainRoute
                        }
                    )
                }
                MainRoute -> NavEntry(key) {
                    MainScreen(
                        onOpenSettings = {
                            backStack.add(SettingsRoute)
                        }
                    )
                }
                SettingsRoute -> NavEntry(key) {
                    SettingsScreen(
                        userName = "User",
                        userEmail = "user@example.com",
                        onTermsOfUse = { /* TODO: open terms URL */ },
                        onCheckForUpdates = { /* TODO: open store */ },
                        onSignOut = {
                            backStack.clear()
                            backStack.add(SignUpRoute)
                        },
                        onClose = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                else -> NavEntry(Unit) { Text("Unknown") }
            }
        }
    )
}
