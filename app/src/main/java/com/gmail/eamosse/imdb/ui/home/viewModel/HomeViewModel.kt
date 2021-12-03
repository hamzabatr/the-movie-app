package com.gmail.eamosse.imdb.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.eamosse.idbdata.data.*
import com.gmail.eamosse.idbdata.repository.MovieRepository
import com.gmail.eamosse.idbdata.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * VM permettant de gérer les données de la vue
 */
class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _token: MutableLiveData<Token> = MutableLiveData()
    val token: LiveData<Token>
        get() = _token

    private val _categories: MutableLiveData<List<Category>> = MutableLiveData()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _movies: MutableLiveData<List<Movies>> = MutableLiveData()
    val movies: LiveData<List<Movies>>
        get() = _movies

    private val _movie: MutableLiveData<Movie> = MutableLiveData()
    val movie: LiveData<Movie>
        get() = _movie

    private val _videos: MutableLiveData<List<Video>> = MutableLiveData()
    val videos: LiveData<List<Video>>
        get() = _videos

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error

    /**
     * Block d'initialisation du viewmodel
     * On en profite (pour l'instant) pour récupérer le token utilisateur
     */
    init {
        /**
         * getToken est une méthode suspendue, par conséquent elle doit être appelée dans une coroutine
         * De plus, le repository accède à une source de données (API ou BDD); il faut appeler la méthode
         * dans un thread secondaire
         *
         * On utilise l'attribut viewModelScope qui est une coroutine lié au cycle de vie du VM
         * Puis on exécute la méthode getToken dans un [Dispatchers.IO], soit dans un thread secondaire
         */
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getToken()) {
                is Result.Succes -> {
                    _token.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getCategories()) {
                is Result.Succes -> {
                    _categories.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }

    fun getMoviesByCategory(genreId: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getMoviesByCategory(genreId, page)) {
                is Result.Succes -> {
                    _movies.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }

    fun getMovieById(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getMovieById(movieId)) {
                is Result.Succes -> {
                    _movie.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }

    fun getVideoMovieById(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getVideoMovieById(movieId)) {
                is Result.Succes -> {
                    _videos.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }
}