package com.gmail.eamosse.imdb.di

import android.content.Context
import com.gmail.eamosse.imdb.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // On injecte l'api key en utilisant la propriété named
    // ce qui permet de lui donner un nom (ici API_KEY)
    single(named("API_KEY")) {
        "507a86e6d98ae2b2cd600e594ee02637"
    }

    single(named("BASE_URL")) {
        "https://api.themoviedb.org/3/"
    }

    single(named("APP_PREFS")) {
        androidContext().getSharedPreferences("app_private", Context.MODE_PRIVATE)
    }

    viewModel {
        HomeViewModel(repository = get())
    }
}