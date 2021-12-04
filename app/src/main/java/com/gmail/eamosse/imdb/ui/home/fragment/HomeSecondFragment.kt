package com.gmail.eamosse.imdb.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.gmail.eamosse.imdb.databinding.FragmentHomeSecondBinding
import com.gmail.eamosse.imdb.ui.home.viewModel.HomeViewModel
import com.gmail.eamosse.imdb.ui.home.adapter.MoviesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeSecondFragment : Fragment() {

    private val args: HomeSecondFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeSecondBinding
    private var page: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(homeViewModel) {
                //récupérer les films par catégories
                if(page == 1){
                    binding.pageNumber.text = page.toString()
                    getMoviesByCategory(args.myArg, page)
                }

                binding.previous.setOnClickListener {
                    if (page > 1) {
                        page--
                        binding.pageNumber.text = page.toString()
                        getMoviesByCategory(args.myArg, page)
                    }
                }

                binding.next.setOnClickListener {
//                    if(page > 1){
                    page++
                    binding.pageNumber.text = page.toString()
                    getMoviesByCategory(args.myArg, page)

//                    }
                }

            movies.observe(viewLifecycleOwner, {
                binding.movieList.adapter = MoviesAdapter(it)
            })
        }
    }
}
