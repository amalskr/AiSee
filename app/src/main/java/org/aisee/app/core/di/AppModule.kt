package org.aisee.app.core.di

import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.aisee.app.R
import org.aisee.app.core.data.AuthRepository
import org.aisee.app.core.data.FirebaseAuthRepository
import org.aisee.app.core.data.UserPreferences
import org.aisee.app.core.data.UserRepository
import org.aisee.app.core.data.UserRepositoryImpl
import org.aisee.app.core.data.remote.AiSeeApiClient
import org.aisee.app.presentation.auth.AuthViewModel
import org.aisee.app.presentation.signup.RegistrationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }

    single { CredentialManager.create(androidContext()) }

    single<AuthRepository> {
        FirebaseAuthRepository(
            firebaseAuth = get(),
            credentialManager = get(),
            webClientId = androidContext().getString(R.string.default_web_client_id)
        )
    }

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        android.util.Log.d("KtorHttp", message)
                    }
                }
                level = LogLevel.ALL
            }
            expectSuccess = false
        }
    }

    single { AiSeeApiClient(get()) }

    single { UserPreferences(androidContext()) }

    single<UserRepository> { UserRepositoryImpl(get()) }

    viewModel { AuthViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
}
