package com.gmail.eamosse.imdb.ui.search.fragment


import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.gmail.eamosse.idbdata.repository.MovieRepository
import com.gmail.eamosse.imdb.databinding.FragmentSearchBinding
import com.gmail.eamosse.imdb.ui.search.adapter.SearchAdapter
import com.gmail.eamosse.imdb.ui.search.viewModel.SearchViewModel

class SearchFragment : Fragment(), TextWatcher {

    private val searchViewModel: SearchViewModel = SearchViewModel(MovieRepository())
    private lateinit var binding: FragmentSearchBinding
    private var delay: Long = 1000
    private var lastTextEdit: Long = 0
    private var handler: Handler =  Handler()
    private var page: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPopularMovies()
        binding.searchBar.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        handler.removeCallbacks(inputFinishChecker)
    }

    override fun afterTextChanged(s: Editable?) {
        if (binding.searchBar.text.toString().isNotEmpty()) {
            lastTextEdit = System.currentTimeMillis()
            handler.postDelayed(inputFinishChecker, delay)
        } else {
            setPopularMovies()
        }
    }

    private val inputFinishChecker = Runnable {
        setSearchedMovie()
    }

    private fun setAdapter() {
        with(searchViewModel) {
            with(binding) {
                movies.observe(viewLifecycleOwner, {
                    popularMoviesList.adapter = SearchAdapter(it)
                    pageNumber.text = page.toString() + " - " + it.total_pages.toString()
                })
            }
        }
    }

    private fun setPopularMovies(){
        page = 1
        binding.previous.isInvisible = true
        searchViewModel.getPopularMovies(page)
        setAdapter()
        with(searchViewModel) {
            with(binding) {
                previous.setOnClickListener {
                    if (page > 1) {
                        page--
                        getPopularMovies(page)
                        setAdapter()
                    }
                    if (page == 1) {
                        previous.isInvisible = true
                    }
                    if (page < movies.value!!.total_pages) {
                        next.isInvisible = false
                    }
                }

                next.setOnClickListener {
                    if (page <= movies.value!!.total_pages) {
                        page++
                        getPopularMovies(page)
                        setAdapter()
                    }
                    if (page > 1) {
                        previous.isInvisible = false
                    }
                    if (page == movies.value!!.total_pages) {
                        next.isInvisible = true
                    }
                }
            }
        }
    }

    private fun setSearchedMovie(){
        page = 1
        binding.previous.isInvisible = true
        if (System.currentTimeMillis() > lastTextEdit + delay - 500) {
            searchViewModel.getMoviesBySearch(Uri.encode(binding.searchBar.text.toString()), page)
            setAdapter()
        }
        with(searchViewModel) {
            with(binding) {
                previous.setOnClickListener {
                    if (page > 1) {
                        page--
                        getMoviesBySearch(Uri.encode(binding.searchBar.text.toString()), page)
                        setAdapter()
                    }
                    if (page == 1) {
                        previous.isInvisible = true
                    }
                    if (page < movies.value!!.total_pages) {
                        next.isInvisible = false
                    }
                }

                next.setOnClickListener {
                    if (page <= movies.value!!.total_pages) {
                        page++
                        getMoviesBySearch(Uri.encode(binding.searchBar.text.toString()), page)
                        setAdapter()
                    }
                    if (page > 1) {
                        previous.isInvisible = false
                    }
                    if (page == movies.value!!.total_pages) {
                        next.isInvisible = true
                    }
                }
            }
        }
    }
}

