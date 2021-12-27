package com.gmail.eamosse.imdb.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.eamosse.idbdata.data.*
import com.gmail.eamosse.idbdata.repository.MovieRepository
import com.gmail.eamosse.idbdata.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoverViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _token: MutableLiveData<Token> = MutableLiveData()
    val token: LiveData<Token>
        get() = _token

    private val _categories: MutableLiveData<List<Category>> = MutableLiveData()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error

    private val _actor: MutableLiveData<List<Actor>> = MutableLiveData()
    val actor: LiveData<List<Actor>>
        get() = _actor

    private val _movies: MutableLiveData<List<Movies>> = MutableLiveData()
    val movies: LiveData<List<Movies>>
        get() = _movies

    private lateinit var textSplitted: List<String>
    private var genreBool: Boolean = false
    private var actorBool: Boolean = false
    private var yearBool: Boolean = false

    init {
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

    fun searchForActor(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.searchForActor(query)) {
                is Result.Succes -> {
                    _actor.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }

    fun discoverMovies(text: String, page: Int) {
        textSplitted = text.split("|")

        val genre = textSplitted[0]
        genreBool = textSplitted[1].toBoolean()
        val actor = textSplitted[2]
        actorBool = textSplitted[3].toBoolean()
        val year = textSplitted[4]
        yearBool = textSplitted[5].toBoolean()

        when {
            genreBool && actorBool && yearBool -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = repository.discoverMovies(genre, actor, year, page)) {
                        is Result.Succes -> {
                            _movies.postValue(result.data)
                        }
                        is Result.Error -> {
                            _error.postValue(result.message)
                        }
                    }
                }
            }
            genreBool && actorBool -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (
                        val result =
                            repository.discoverMoviesByGenreAndActor(genre, actor, page)
                    ) {
                        is Result.Succes -> {
                            _movies.postValue(result.data)
                        }
                        is Result.Error -> {
                            _error.postValue(result.message)
                        }
                    }
                }
            }
            genreBool && yearBool -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (
                        val result =
                            repository.discoverMoviesByGenreAndYear(genre, year, page)
                    ) {
                        is Result.Succes -> {
                            _movies.postValue(result.data)
                        }
                        is Result.Error -> {
                            _error.postValue(result.message)
                        }
                    }
                }
            }
            actorBool && yearBool -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (
                        val result =
                            repository.discoverMoviesByActorAndYear(actor, year, page)
                    ) {
                        is Result.Succes -> {
                            _movies.postValue(result.data)
                        }
                        is Result.Error -> {
                            _error.postValue(result.message)
                        }
                    }
                }
            }
            genreBool -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = repository.getMoviesByCategory(genre, page)) {
                        is Result.Succes -> {
                            _movies.postValue(result.data)
                        }
                        is Result.Error -> {
                            _error.postValue(result.message)
                        }
                    }
                }
            }
            actorBool -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = repository.discoverMoviesByActor(actor, page)) {
                        is Result.Succes -> {
                            _movies.postValue(result.data)
                        }
                        is Result.Error -> {
                            _error.postValue(result.message)
                        }
                    }
                }
            }
            yearBool -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = repository.discoverMoviesByYear(year, page)) {
                        is Result.Succes -> {
                            _movies.postValue(result.data)
                        }
                        is Result.Error -> {
                            _error.postValue(result.message)
                        }
                    }
                }
            }
        }
    }
}
