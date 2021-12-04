package com.gmail.eamosse.imdb.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmail.eamosse.imdb.databinding.FragmentHomeBinding
import com.gmail.eamosse.imdb.ui.home.adapter.CategoryAdapter
import com.gmail.eamosse.imdb.ui.home.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(homeViewModel) {

            token.observe(viewLifecycleOwner, {
                // récupérer les catégories
                getCategories()
            })

            categories.observe(viewLifecycleOwner, {
                binding.categoryList.adapter = CategoryAdapter(it)
            })

            error.observe(viewLifecycleOwner, {
                // afficher l'erreur
            })
        }
    }
}
