package com.gmail.eamosse.imdb.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gmail.eamosse.idbdata.data.Category
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.CategoryListItemBinding
import com.gmail.eamosse.imdb.ui.home.fragment.HomeFragmentDirections

class CategoryAdapter(private val items: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CategoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mCategoryIllustration: ImageView = binding.categoryIllustration
        val mCategoryImg: CardView = binding.cardViewId
        fun bind(item: Category) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(CategoryListItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (items[position].name) {
            "Action" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_action)
            "Adventure" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_adventure)
            "Animation" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_animation)
            "Comedy" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_comedy)
            "Crime" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_crime)
            "Documentary" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_documentary)
            "Drama" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_drama)
            "Family" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_family)
            "Fantasy" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_fantasy)
            "History" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_history)
            "Horror" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_horror)
            "Music" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_music)
            "Mystery" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_mystery)
            "Romance" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_romance)
            "Science Fiction" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_science_fiction)
            "TV Movie" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_tv_movie)
            "Thriller" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_thriller)
            "War" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_war)
            "Western" -> holder.mCategoryIllustration.setImageResource(R.drawable.ic_western)
        }


        holder.bind(items[position])
        holder.mCategoryImg.setOnClickListener {
            val nextAction =
                HomeFragmentDirections.actionHomeFragmentToHomeSecondFragment(items[position].id.toString())
            Navigation.findNavController(it).navigate(nextAction)
        }
    }
}
