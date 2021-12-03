package com.gmail.eamosse.imdb.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gmail.eamosse.idbdata.data.Movie
import com.gmail.eamosse.idbdata.data.Video
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.FragmentMovieBinding
import com.gmail.eamosse.imdb.ui.home.viewModel.HomeViewModel
import com.google.android.youtube.player.YouTubeStandalonePlayer
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment() {

    private val args: MovieFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentMovieBinding
    private val basePosterPath = "https://image.tmdb.org/t/p/w500"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(homeViewModel) {
            getMovieById(args.myArg)
            getVideoMovieById(args.myArg)
            with(binding) {

                movie.observe(viewLifecycleOwner, {
                    uploadImage(imageView, basePosterPath + it.poster_path)
                    uploadImage(imageVideoView, basePosterPath + it.backdrop_path)
                    productionCompaniesValue.text = getDirectors(it)
                    genreValue.text = getGenres(it)
                    originalLanguageValue.text = movie.value!!.original_language
                    title.text = getTitleAndVote(it)
                    releaseDateValue.text = movie.value!!.release_date
                })

                playButton.setOnClickListener {
                    videos.observe(viewLifecycleOwner, {
                        val intent = YouTubeStandalonePlayer.createVideoIntent(
                            activity,
                            getString(R.string.youtube_api_key),
                            checkSiteAndGetKey(it)
                        )
                        startActivity(intent)
                    })
                }

                imageVideoView.setOnClickListener {
                    videos.observe(viewLifecycleOwner, {
                        val intent = YouTubeStandalonePlayer.createVideoIntent(
                            activity,
                            getString(R.string.youtube_api_key),
                            checkSiteAndGetKey(it)
                        )
                        startActivity(intent)
                    })
                }

            }
        }
    }

    private fun checkSiteAndGetKey(videos: List<Video>): String {
        var keyYT = ""
        for (videoYT in videos) {
            if (videoYT.site == "YouTube") {
                keyYT = videoYT.key
            }
        }
        return keyYT
    }

    private fun uploadImage(image: ImageView, load: String) {
        Glide.with(image.context)
            .load(load)
            .into(image)
    }


    private fun getDirectors(movie: Movie): String {
        var value = ""
        for ((i, director) in movie.production_companies.withIndex()) {
            value += if (i == movie.production_companies.size - 1) {
                director.name
            } else {
                director.name + ", "
            }
        }
        return value
    }

    private fun getGenres(movie: Movie): String {
        var value = ""
        for ((i, genre) in movie.genres.withIndex()) {
            value += if (i == movie.genres.size - 1) {
                genre.name
            } else {
                genre.name + ", "
            }
        }
        return value
    }

    private fun getTitleAndVote(movie: Movie): String {
        return movie.title + " (" + movie.vote_average + "/10)"
    }
}