package com.gmail.eamosse.imdb.ui.favorites

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gmail.eamosse.imdb.R

class FavoritesFragment() : Fragment(), Parcelable {

    private lateinit var favoritesViewModel: FavoritesViewModel

    constructor(parcel: Parcel) : this()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
            ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        val textView: TextView = root.findViewById(R.id.text_favorites)
        favoritesViewModel.text.observe(
            viewLifecycleOwner,
            {
                textView.text = it
            }
        )
        return root
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoritesFragment> {
        override fun createFromParcel(parcel: Parcel): FavoritesFragment {
            return FavoritesFragment(parcel)
        }

        override fun newArray(size: Int): Array<FavoritesFragment?> {
            return arrayOfNulls(size)
        }
    }
}
