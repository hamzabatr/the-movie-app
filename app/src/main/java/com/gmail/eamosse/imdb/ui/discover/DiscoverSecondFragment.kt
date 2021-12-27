package com.gmail.eamosse.imdb.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.FragmentHomeSecondBinding
import com.gmail.eamosse.imdb.ui.home.adapter.MoviesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverSecondFragment : Fragment() {

    private val args: DiscoverSecondFragmentArgs by navArgs()
    private val discoverViewModel: DiscoverViewModel by viewModel()
    private lateinit var binding: FragmentHomeSecondBinding
    private var page: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(discoverViewModel) {
            with(binding) {
                previous.isInvisible = true
                discoverMovies(args.myArg, page)
                setAdapter()

                previous.setOnClickListener {
                    if (page > 1) {
                        page--
                        discoverMovies(args.myArg, page)
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
                        discoverMovies(args.myArg, page)
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

    private fun setAdapter() {
        discoverViewModel.movies.observe(viewLifecycleOwner, {
            with(binding) {
                movieList.adapter = MoviesAdapter(it)
                pageNumber.text = page.toString() + " - " + it.total_pages.toString()
            }
        })
    }
}
