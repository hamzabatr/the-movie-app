package com.gmail.eamosse.idbdata.api.service

import android.icu.text.StringSearch
import android.net.Uri
import com.gmail.eamosse.idbdata.api.response.CategoryResponse
import com.gmail.eamosse.idbdata.api.response.MovieResponse
import com.gmail.eamosse.idbdata.api.response.MoviesResponse
import com.gmail.eamosse.idbdata.api.response.TokenResponse
import com.gmail.eamosse.idbdata.api.response.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MovieService {

    @GET("authentication/token/new")
    suspend fun getToken(): Response<TokenResponse>

    @GET("genre/movie/list")
    suspend fun getCategories(): Response<CategoryResponse>

    @GET("discover/movie?")
    suspend fun getMoviesByCategory(
        @Query("with_genres") genreId: String,
        @Query("page") page: Int
    ): Response<MoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") movieId: String): Response<MovieResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideoMovieById(@Path("movie_id") movieId: String): Response<VideoResponse>

    @GET("search/movie?")
    suspend fun getMoviesBySearch(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<MoviesResponse>

    @GET("movie/popular?")
    suspend fun getPopularMovies(@Query("page") page: Int): Response<MoviesResponse>
}
