package com.gmail.eamosse.imdb.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.gmail.eamosse.imdb.databinding.FragmentMovieBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment() {

    private val args: MovieFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentMovieBinding
    private val basePosterPath = "https://image.tmdb.org/t/p/w500"
    private val baseVideoPath = "https://www.youtube.com/watch?v=3iv2r_h3dYE"
//    public static final int MEDIA_ERROR_UNSUPPORTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val mediaPlayer: MediaPlayer = MediaPlayer.create(binding.videoView.context, (baseVideoPath).toUri())
//        mediaPlayer.start()
//            binding.videoView.setVideoURI((baseVideoPath).toUri())
//        binding.videoView.start()

//        with(homeViewModel) {
//            token.observe(viewLifecycleOwner, {
//                getMovieById(args.myArg)
//            })

//            movie.observe(viewLifecycleOwner, {
//                Glide.with(binding.imageView.context)
//                    .load(basePosterPath+it.poster_path)
//                    .into(binding.imageView)
//            })

//            videos.observe(viewLifecycleOwner,{

//            })

//        }
    }
}
