package com.iolandarosa.retailhub.features.home.di

import com.iolandarosa.retailhub.features.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel {
        HomeViewModel(
            getAuthUserUseCase = get(),
            getLocalUserImageUseCase = get(),
            dispatcherProvider = get(),
        )
    }
}
