package com.gmail.eamosse.imdb.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gmail.eamosse.idbdata.data.Category
import com.gmail.eamosse.imdb.MainActivity
import com.gmail.eamosse.imdb.databinding.CategoryListItemBinding

class CategoryAdapter(private val items: List<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CategoryListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val mCategoryImg : AppCompatImageView = binding.categoryImg
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
        holder.bind(items[position])
        holder.mCategoryImg.setOnClickListener{
            val nextAction = HomeFragmentDirections.actionHomeFragmentToHomeSecondFragment(items[position].id.toString())
            Navigation.findNavController(it).navigate(nextAction)
        }
    }
}