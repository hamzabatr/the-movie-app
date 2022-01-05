package com.gmail.eamosse.imdb.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gmail.eamosse.imdb.R

class AboutFragment : Fragment() {

    private lateinit var aboutViewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutViewModel =
            ViewModelProviders.of(this).get(AboutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_about, container, false)
        // val textView_hamza: TextView = root.findViewById(R.id.text_about_Hamza)
        // val textView_roussy: TextView = root.findViewById(R.id.text_about_Roussy)
        // val textView_saubanere: TextView = root.findViewById(R.id.text_about_Saubanere)
        aboutViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
            }
        )
        return root
    }
}
