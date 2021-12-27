package com.gmail.eamosse.imdb.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.eamosse.idbdata.api.response.MoviesResponse
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.OtherMoviesListItemBinding
import com.gmail.eamosse.imdb.ui.search.fragment.SearchFragmentDirections

class SearchAdapter(private val items: MoviesResponse) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val basePosterPath = "https://image.tmdb.org/t/p/w500"

    inner class ViewHolder(private val binding: OtherMoviesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mCardView: CardView = binding.cardViewId
        val mPoster: ImageView = binding.poster
        val mMovieName: AppCompatTextView = binding.movieName
        fun bind(item: MoviesResponse.Movies) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(OtherMoviesListItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = items.results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(items.results[position])

            val context = mPoster.context
            Glide.with(context)
                .load(basePosterPath + items.results[position].poster_path)
                .error(R.drawable.ic_baseline_image_24)
                .into(mPoster)

            mMovieName.text = items.results[position].title

            mCardView.setOnClickListener {
                val nextAction =
                    SearchFragmentDirections.actionNavigationSearchToNavigationMovie(items.results[position].id.toString())
                Navigation.findNavController(it).navigate(nextAction)
            }
        }
    }
}
