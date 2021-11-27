package com.gmail.eamosse.imdb.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.eamosse.idbdata.data.Movie
import com.gmail.eamosse.imdb.databinding.MovieListItemBinding

class MovieAdapter(private val items: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val basePosterPath = "https://image.tmdb.org/t/p/w500"

    inner class ViewHolder(private val binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mMovieImg: AppCompatImageView = binding.movieImg
        fun bind(item: Movie) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(MovieListItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        val context = holder.mMovieImg.context
        Glide.with(context)
            .load(basePosterPath+items[position].poster_path)
            .into(holder.mMovieImg)
    }
}