package org.aisee.app.navigation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import org.aisee.app.core.common.Resource
import org.aisee.app.presentation.auth.AuthViewModel
import org.aisee.app.presentation.beforestart.BeforeStartScreen
import org.aisee.app.presentation.main.MainScreen
import org.aisee.app.presentation.permission.PermissionScreen
import org.aisee.app.presentation.permission.hasRequiredPermissions
import org.aisee.app.presentation.settings.SettingsScreen
import org.aisee.app.presentation.settings.WebViewScreen
import org.aisee.app.presentation.signin.ForgotPasswordScreen
import org.aisee.app.presentation.signin.SignInScreen
import org.aisee.app.presentation.signup.SignUpScreen
import org.aisee.app.presentation.signup.RegistrationViewModel
import org.aisee.app.presentation.signup.SignUpWithEmailScreen
import org.aisee.app.presentation.splash.SplashScreen
import org.aisee.app.presentation.terms.PrivacyAndTermsScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AiSeeNavHost() {
    val authViewModel: AuthViewModel = koinViewModel()
    val googleSignInState by authViewModel.googleSignInState.collectAsState()
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

    LaunchedEffect(googleSignInState) {
        when (val state = googleSignInState) {
            is Resource.Success -> {
                authViewModel.resetState()
                navigateToMain()
            }
            is Resource.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.resetState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                            onContinueWithGoogle = {
                                authViewModel.signInWithGoogle(context)
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
                        val registrationViewModel: RegistrationViewModel = koinViewModel()
                        val registrationState by registrationViewModel.registrationState.collectAsState()

                        LaunchedEffect(registrationState) {
                            when (val state = registrationState) {
                                is Resource.Success -> {
                                    Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                                    registrationViewModel.resetState()
                                    navigateToMain()
                                }
                                is Resource.Error -> {
                                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                                    registrationViewModel.resetState()
                                }
                                else -> {}
                            }
                        }

                        SignUpWithEmailScreen(
                            onCreateAccount = { username, email, password ->
                                registrationViewModel.registerUser(username, email, password)
                            },
                            onSignUpWithGoogle = {
                                authViewModel.signInWithGoogle(context)
                            },
                            isLoading = registrationState is Resource.Loading
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
                        val user = authViewModel.currentUser
                        SettingsScreen(
                            userName = user?.displayName ?: "User",
                            userEmail = user?.email ?: "",
                            onTermsOfUse = {
                                backStack.add(WebViewRoute(
                                    url = "https://aisee.ai/terms",
                                    title = "Terms of Use"
                                ))
                            },
                            onCheckForUpdates = {
                                backStack.add(WebViewRoute(
                                    url = "https://play.google.com/store/apps/details?id=org.ahlab.aisee",
                                    title = "Google Play"
                                ))
                            },
                            onSignOut = {
                                authViewModel.signOut()
                                backStack.clear()
                                backStack.add(SignUpRoute)
                            },
                            onClose = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                    is WebViewRoute -> NavEntry(key) {
                        WebViewScreen(
                            url = key.url,
                            title = key.title,
                            onClose = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                    else -> NavEntry(Unit) { Text("Unknown") }
                }
            }
        )

        if (googleSignInState is Resource.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF9B87E8))
            }
        }
    }
}
