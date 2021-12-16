package com.gmail.eamosse.idbdata.repository

import com.gmail.eamosse.idbdata.api.response.*
import com.gmail.eamosse.idbdata.api.response.toCategory
import com.gmail.eamosse.idbdata.api.response.toEntity
import com.gmail.eamosse.idbdata.api.response.toToken
import com.gmail.eamosse.idbdata.data.*
import com.gmail.eamosse.idbdata.datasources.LocalDataSource
import com.gmail.eamosse.idbdata.datasources.OnlineDataSource
import com.gmail.eamosse.idbdata.utils.Result
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * La classe permettant de gérer les données de l'application
 */
class MovieRepository : KoinComponent {
    // Gestion des sources de données locales
    private val local: LocalDataSource by inject()

    // Gestion des sources de données en lignes
    private val online: OnlineDataSource by inject()

    /**
     * Récupérer le token permettant de consommer les ressources sur le serveur
     * Le résultat du datasource est converti en instance d'objets publiques
     */
    suspend fun getToken(): Result<Token> {
        return when (val result = online.getToken()) {
            is Result.Succes -> {
                // save the response in the local database
                local.saveToken(result.data.toEntity())
                // return the response
                Result.Succes(result.data.toToken())
            }
            is Result.Error -> result
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        return when (val result = online.getCategories()) {
            is Result.Succes -> {
                // On utilise la fonction map pour convertir les catégories de la réponse serveur
                // en liste de categories d'objets de l'application
                val categories = result.data.map {
                    it.toCategory()
                }
                Result.Succes(categories)
            }
            is Result.Error -> result
        }
    }

    suspend fun getMoviesByCategory(genreId: String, page: Int): Result<MoviesResponse> {
        return when (val result = online.getMoviesByCategory(genreId, page)) {
            is Result.Succes -> Result.Succes(result.data.toResponse())

            is Result.Error -> result
        }
    }

    suspend fun getMovieById(movieId: String): Result<MovieResponse> {
        return when (val result = online.getMovieById(movieId)) {
            is Result.Succes -> Result.Succes(result.data.toMovie())

            is Result.Error -> result
        }
    }

    suspend fun getVideoMovieById(movieId: String): Result<List<Video>> {
        return when (val result = online.getVideoMovieById(movieId)) {
            is Result.Succes -> {
                val video = result.data.map {
                    it.toVideo()
                }
                Result.Succes(video)
            }
            is Result.Error -> result
        }
    }

    suspend fun getMoviesBySearch(title: String, page: Int): Result<MoviesResponse> {
        return when (val result = online.getMoviesBySearch(title, page)) {
            is Result.Succes -> Result.Succes(result.data.toResponse())

            is Result.Error -> result
        }
    }

    suspend fun getPopularMovies(page: Int): Result<MoviesResponse> {
        return when (val result = online.getPopularMovies(page)) {
            is Result.Succes -> Result.Succes(result.data.toResponse())

            is Result.Error -> result
        }
    }
}
