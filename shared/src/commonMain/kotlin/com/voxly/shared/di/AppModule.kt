package com.voxly.shared.di

import com.voxly.shared.data.remote.*
import com.voxly.shared.data.repository.*
import com.voxly.shared.domain.repository.*
import com.voxly.shared.domain.usecase.*
import com.voxly.shared.ui.screens.home.HomeViewModel
import com.voxly.shared.ui.screens.learn.LearnViewModel
import com.voxly.shared.ui.screens.practice.ScenarioLibraryViewModel
import org.koin.dsl.module

val networkModule = module {
    single<com.voxly.shared.data.repository.TokenStore> {
        object : com.voxly.shared.data.repository.TokenStore {
            private var token: String? = null
            override fun saveToken(t: String) { token = t }
            override fun getToken() = token
            override fun clearToken() { token = null }
        }
    }

    single { createHttpClient(tokenProvider = { get<com.voxly.shared.data.repository.TokenStore>().getToken() }) }
    single { AuthApiService(get()) }
    single { ScenarioApiService(get()) }
    single { SessionApiService(get()) }
    single { LearningApiService(get()) }
}

val repositoryModule = module {
    single<AuthRepository>     { AuthRepositoryImpl(get(), get()) }
    single<ScenarioRepository> { ScenarioRepositoryImpl(get()) }
    single<SessionRepository>  { SessionRepositoryImpl(get()) }
    single<LearningRepository> { LearningRepositoryImpl(get()) }
    single<SpeechRepository>   { SpeechRepositoryImpl() }
}

val useCaseModule = module {
    factory { SignUpWithEmailUseCase(get()) }
    factory { LogInUseCase(get()) }
    factory { LogOutUseCase(get()) }
    factory { SendOtpUseCase(get()) }
    factory { VerifyOtpUseCase(get()) }
    factory { ResetPasswordUseCase(get()) }
    factory { GetCurrentUserFlowUseCase(get()) }
    factory { GetScenariosUseCase(get()) }
    factory { GetRecommendedScenariosUseCase(get()) }
    factory { GetScenarioByIdUseCase(get()) }
    factory { SearchScenariosUseCase(get()) }
    factory { CreateSessionUseCase(get()) }
    factory { CompleteSessionUseCase(get()) }
    factory { GetSessionHistoryUseCase(get()) }
    factory { GetLearningPathUseCase(get()) }
    factory { GetDailyChallengesUseCase(get()) }
    factory { CompleteChallengeUseCase(get()) }
    factory { AnalyseAudioUseCase(get()) }
}

val viewModelModule = module {
    factory { HomeViewModel(get(), get()) }
    factory { LearnViewModel(get()) }
    factory { ScenarioLibraryViewModel(get()) }
}

val appModules = listOf(networkModule, repositoryModule, useCaseModule, viewModelModule)
