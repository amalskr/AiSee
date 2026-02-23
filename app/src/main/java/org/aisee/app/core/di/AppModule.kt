package org.aisee.app.core.di

import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import org.aisee.app.R
import org.aisee.app.core.data.AuthRepository
import org.aisee.app.core.data.FirebaseAuthRepository
import org.aisee.app.presentation.auth.AuthViewModel
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

    viewModel { AuthViewModel(get()) }
}
