package com.gmail.eamosse.imdb.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.eamosse.idbdata.api.response.MoviesResponse
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.MovieListItemBinding
import com.gmail.eamosse.imdb.ui.home.fragment.HomeSecondFragmentDirections

class MoviesAdapter(
    private val items: MoviesResponse,
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    private val basePosterPath = "https://image.tmdb.org/t/p/w500"

    inner class ViewHolder(private val binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mMovieImg: AppCompatImageView = binding.movieImg
        fun bind(item: MoviesResponse.Movies) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(MovieListItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = items.results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(items.results[position])
            val context = mMovieImg.context
            Glide.with(context)
                .load(basePosterPath + items.results[position].poster_path)
                .error(R.drawable.ic_baseline_image_24)
                .into(mMovieImg)
            mMovieImg.setOnClickListener {
                val nextAction =
                    HomeSecondFragmentDirections.actionNavigationHomeSecondToMovieFragment(items.results[position].id.toString())
                Navigation.findNavController(it).navigate(nextAction)
            }
        }
    }
}
