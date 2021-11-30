package com.gmail.eamosse.imdb.ui.notifications

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

class NotificationsFragment() : Fragment(), Parcelable {

    private lateinit var notificationsViewModel: NotificationsViewModel

    constructor(parcel: Parcel) : this()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(
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

    companion object CREATOR : Parcelable.Creator<NotificationsFragment> {
        override fun createFromParcel(parcel: Parcel): NotificationsFragment {
            return NotificationsFragment(parcel)
        }

        override fun newArray(size: Int): Array<NotificationsFragment?> {
            return arrayOfNulls(size)
        }
    }
}
