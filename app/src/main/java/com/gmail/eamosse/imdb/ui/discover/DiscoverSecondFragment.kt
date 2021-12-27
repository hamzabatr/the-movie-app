package com.gmail.eamosse.imdb.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.gmail.eamosse.imdb.databinding.FragmentHomeSecondBinding
import com.gmail.eamosse.imdb.ui.home.MoviesAdapter
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
            token.observe(viewLifecycleOwner, {
                if (page == 1) {
                    binding.pageNumber.text = page.toString()
                    discoverMovies(args.myArg, page)
                }

                binding.previous.setOnClickListener {
                    if (page > 1) {
                        page--
                        binding.pageNumber.text = page.toString()
                        discoverMovies(args.myArg, page)
                    }
                }

                binding.next.setOnClickListener {
                    page++
                    binding.pageNumber.text = page.toString()
                    discoverMovies(args.myArg, page)
                }
            })

            movies.observe(viewLifecycleOwner, {
                binding.movieList.adapter = MoviesAdapter(it)
            })
        }
    }
}
