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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
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
import org.aisee.app.presentation.signin.ForgotPasswordViewModel
import org.aisee.app.presentation.signin.LoginViewModel
import org.aisee.app.presentation.signin.SignInScreen
import org.aisee.app.presentation.signup.SignUpScreen
import org.aisee.app.core.data.UserPreferences
import org.aisee.app.presentation.common.ErrorDialog
import org.aisee.app.presentation.common.InfoDialog
import org.aisee.app.presentation.signup.RegistrationViewModel
import org.koin.compose.koinInject
import org.aisee.app.presentation.signup.SignUpWithEmailScreen
import org.aisee.app.presentation.splash.SplashScreen
import org.aisee.app.presentation.terms.PrivacyAndTermsScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AiSeeNavHost() {
    val authViewModel: AuthViewModel = koinViewModel()
    val userPreferences: UserPreferences = koinInject()
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
                if (state.data.status != "error") {
                    userPreferences.saveFromResponse(state.data)
                    authViewModel.resetState()
                    navigateToMain()
                }
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
                                if (userPreferences.isLoggedIn) {
                                    navigateToMain()
                                } else {
                                    backStack[0] = PrivacyAndTermsRoute
                                }
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

                        var registrationFirstName by remember { mutableStateOf("") }
                        var registrationLastName by remember { mutableStateOf("") }
                        var registrationPhoneNumber by remember { mutableStateOf("") }

                        LaunchedEffect(registrationState) {
                            when (val state = registrationState) {
                                is Resource.Success -> {
                                    if (state.data.status != "error") {
                                        userPreferences.saveFromResponse(state.data)
                                        userPreferences.saveLocalFields(
                                            "$registrationFirstName $registrationLastName",
                                            registrationPhoneNumber
                                        )
                                        Toast.makeText(context, state.data.message ?: "Registration successful", Toast.LENGTH_LONG).show()
                                        registrationViewModel.resetState()
                                        navigateToMain()
                                    }
                                }
                                is Resource.Error -> {}
                                else -> {}
                            }
                        }

                        val apiResponse = (registrationState as? Resource.Success)?.data
                        val networkError = (registrationState as? Resource.Error)?.message

                        if (apiResponse?.status == "error") {
                            ErrorDialog(
                                title = "Error ${apiResponse.httpCode ?: ""}",
                                message = apiResponse.message ?: "Something went wrong",
                                errorCode = apiResponse.errors?.code,
                                onDismiss = { registrationViewModel.resetState() }
                            )
                        }

                        if (networkError != null) {
                            ErrorDialog(
                                title = "Network Error",
                                message = networkError,
                                onDismiss = { registrationViewModel.resetState() }
                            )
                        }

                        SignUpWithEmailScreen(
                            onCreateAccount = { firstName, lastName, email, password, phoneNumber ->
                                registrationFirstName = firstName
                                registrationLastName = lastName
                                registrationPhoneNumber = phoneNumber
                                registrationViewModel.registerUser(firstName, lastName, email, password, phoneNumber)
                            },
                            onSignUpWithGoogle = {
                                authViewModel.signInWithGoogle(context)
                            },
                            isLoading = registrationState is Resource.Loading
                        )
                    }
                    SignInRoute -> NavEntry(key) {
                        val loginViewModel: LoginViewModel = koinViewModel()
                        val loginState by loginViewModel.loginState.collectAsState()

                        LaunchedEffect(loginState) {
                            when (val state = loginState) {
                                is Resource.Success -> {
                                    if (state.data.status != "error") {
                                        userPreferences.saveFromResponse(state.data)
                                        loginViewModel.resetState()
                                        navigateToMain()
                                    }
                                }
                                is Resource.Error -> {}
                                else -> {}
                            }
                        }

                        val apiResponse = (loginState as? Resource.Success)?.data
                        val networkError = (loginState as? Resource.Error)?.message

                        if (apiResponse?.status == "error") {
                            ErrorDialog(
                                title = "Error ${apiResponse.httpCode ?: ""}",
                                message = apiResponse.message ?: "Login failed",
                                errorCode = apiResponse.errors?.code,
                                onDismiss = { loginViewModel.resetState() }
                            )
                        }

                        if (networkError != null) {
                            ErrorDialog(
                                title = "Network Error",
                                message = networkError,
                                onDismiss = { loginViewModel.resetState() }
                            )
                        }

                        SignInScreen(
                            onSignIn = { username, password ->
                                loginViewModel.login(username, password)
                            },
                            onForgotPassword = {
                                backStack.add(ForgotPasswordRoute)
                            },
                            isLoading = loginState is Resource.Loading
                        )
                    }
                    ForgotPasswordRoute -> NavEntry(key) {
                        val forgotPasswordViewModel: ForgotPasswordViewModel = koinViewModel()
                        val forgotPasswordState by forgotPasswordViewModel.forgotPasswordState.collectAsState()

                        val apiResponse = (forgotPasswordState as? Resource.Success)?.data
                        val networkError = (forgotPasswordState as? Resource.Error)?.message

                        if (apiResponse != null && apiResponse.status != "error") {
                            InfoDialog(
                                title = "Password Reset",
                                message = apiResponse.data?.message ?: apiResponse.message ?: "Check your email",
                                onDismiss = {
                                    forgotPasswordViewModel.resetState()
                                    backStack.removeLastOrNull()
                                }
                            )
                        }

                        if (apiResponse?.status == "error") {
                            ErrorDialog(
                                title = "Error ${apiResponse.httpCode ?: ""}",
                                message = apiResponse.message ?: "Request failed",
                                errorCode = apiResponse.errors?.code,
                                onDismiss = { forgotPasswordViewModel.resetState() }
                            )
                        }

                        if (networkError != null) {
                            ErrorDialog(
                                title = "Network Error",
                                message = networkError,
                                onDismiss = { forgotPasswordViewModel.resetState() }
                            )
                        }

                        ForgotPasswordScreen(
                            onResetPassword = { email ->
                                forgotPasswordViewModel.forgotPassword(email)
                            },
                            isLoading = forgotPasswordState is Resource.Loading
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
                        val firebaseUser = authViewModel.currentUser
                        val rawUsername = userPreferences.username ?: ""
                        val isGoogleUser = userPreferences.authProvider == "google"
                        val displayUsername = rawUsername.substringBefore("@").let {
                            if (isGoogleUser) "$it (Google)" else it
                        }
                        SettingsScreen(
                            fullName = userPreferences.fullName ?: firebaseUser?.displayName ?: rawUsername ?: "User",
                            username = displayUsername,
                            userEmail = firebaseUser?.email ?: userPreferences.email ?: "",
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
                                userPreferences.clear()
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

        val googleApiResponse = (googleSignInState as? Resource.Success)?.data
        if (googleApiResponse?.status == "error") {
            ErrorDialog(
                title = "Error ${googleApiResponse.httpCode ?: ""}",
                message = googleApiResponse.message ?: "Google Sign-In failed",
                errorCode = googleApiResponse.errors?.code,
                onDismiss = { authViewModel.resetState() }
            )
        }

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
