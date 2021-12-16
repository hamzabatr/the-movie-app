package com.gmail.eamosse.imdb.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gmail.eamosse.idbdata.api.response.MovieResponse
import com.gmail.eamosse.idbdata.data.Video
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.FragmentMovieBinding
import com.gmail.eamosse.imdb.ui.home.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener


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
                lifecycle.addObserver(youtube)
                youtube.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                        val videoId = checkSiteAndGetKey(videos.value!!)
                        youTubePlayer.loadVideo(videoId, 0F)
                    }
                })

                youtube.addFullScreenListener(object :
                    YouTubePlayerFullScreenListener {

                    override fun onYouTubePlayerEnterFullScreen() {
                        youtube.enterFullScreen()
                    }

                    override fun onYouTubePlayerExitFullScreen() {
                        youtube.exitFullScreen()
                    }
                })


                movie.observe(viewLifecycleOwner, {
                    uploadImage(imageView, basePosterPath + it.poster_path)
                    productionCompaniesValue.text = getDirectors(it)
                    genreValue.text = getGenres(it)
                    originalLanguageValue.text = it.original_language
                    title.text = getTitleAndVote(it)
                    releaseDateValue.text = it.release_date
                })
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
            .error(R.drawable.ic_baseline_image_24)
            .into(image)
    }


    private fun getDirectors(movie: MovieResponse): String {
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

    private fun getGenres(movie: MovieResponse): String {
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

    private fun getTitleAndVote(movie: MovieResponse): String {
        return movie.title + " (" + movie.vote_average + "/10)"
    }

}

