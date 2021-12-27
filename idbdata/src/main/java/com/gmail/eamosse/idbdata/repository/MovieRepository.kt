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

    suspend fun getMoviesByCategory(genreId: String, page: Int): Result<List<Movies>> {
        return when (val result = online.getMoviesByCategory(genreId, page)) {
            is Result.Succes -> {
                // On utilise la fonction map pour convertir les catégories de la réponse serveur
                // en liste de categories d'objets de l'application
                val movies = result.data.map {
                    it.toMovies()
                }
                Result.Succes(movies)
            }
            is Result.Error -> result
        }
    }

    suspend fun getMovieById(movieId: String): Result<Movie> {
        return when (val result = online.getMovieById(movieId)) {
            is Result.Succes -> {
                Result.Succes(result.data.toMovie())
            }
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

    suspend fun searchForActor(query: String): Result<List<Actor>> {
        return when (val result = online.searchForActor(query)) {
            is Result.Succes -> {
                val movies = result.data.map {
                    it.toActor()
                }
                Result.Succes(movies)
            }
            is Result.Error -> result
        }
    }

    suspend fun discoverMovies(genreId: String, actorId: String, year: String, page: Int): Result<List<Movies>> {
        return when (val result = online.discoverMovies(genreId, actorId, year, page)) {
            is Result.Succes -> {
                val movies = result.data.map {
                    it.toMovies()
                }
                Result.Succes(movies)
            }
            is Result.Error -> result
        }
    }

    suspend fun discoverMoviesByActor(actorId: String, page: Int): Result<List<Movies>> {
        return when (val result = online.discoverMoviesByActor(actorId, page)) {
            is Result.Succes -> {
                val movies = result.data.map {
                    it.toMovies()
                }
                Result.Succes(movies)
            }
            is Result.Error -> result
        }
    }

    suspend fun discoverMoviesByYear(year: String, page: Int): Result<List<Movies>> {
        return when (val result = online.discoverMoviesByYear(year, page)) {
            is Result.Succes -> {
                val movies = result.data.map {
                    it.toMovies()
                }
                Result.Succes(movies)
            }
            is Result.Error -> result
        }
    }

    suspend fun discoverMoviesByGenreAndActor(genreId: String, actorId: String, page: Int): Result<List<Movies>> {
        return when (val result = online.discoverMoviesByGenreAndActor(genreId, actorId, page)) {
            is Result.Succes -> {
                val movies = result.data.map {
                    it.toMovies()
                }
                Result.Succes(movies)
            }
            is Result.Error -> result
        }
    }

    suspend fun discoverMoviesByGenreAndYear(genreId: String, year: String, page: Int): Result<List<Movies>> {
        return when (val result = online.discoverMoviesByGenreAndYear(genreId, year, page)) {
            is Result.Succes -> {
                val movies = result.data.map {
                    it.toMovies()
                }
                Result.Succes(movies)
            }
            is Result.Error -> result
        }
    }

    suspend fun discoverMoviesByActorAndYear(actorId: String, year: String, page: Int): Result<List<Movies>> {
        return when (val result = online.discoverMoviesByActorAndYear(actorId, year, page)) {
            is Result.Succes -> {
                val movies = result.data.map {
                    it.toMovies()
                }
                Result.Succes(movies)
            }
            is Result.Error -> result
        }
    }
}
