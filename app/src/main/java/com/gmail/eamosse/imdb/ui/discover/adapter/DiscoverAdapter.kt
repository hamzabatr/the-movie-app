package com.gmail.eamosse.imdb.ui.discover.adapter

import android.content.Context
import android.net.Uri
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListPopupWindow
import android.widget.Toast
import com.gmail.eamosse.idbdata.data.Actor
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.ui.discover.viewModel.DiscoverViewModel
import kotlinx.android.synthetic.main.fragment_discover.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverAdapter(
    private val itemInput: AutoCompleteTextView,
    private val item: String,
    context: Context
) {
    private val discoverViewModel: DiscoverViewModel by viewModel()
    private val listPopupWindow = ListPopupWindow(context, null, R.attr.listPopupWindowStyle)
    private val itemName = mutableListOf<String>()

    fun showMenu() {
        // Set button as the list popup's anchor
        listPopupWindow.anchorView = itemInput

        // Set list popup's content
        with(discoverViewModel)
        {
            if (item == "genre") {
                getCategories()
                categories.observe(viewLifecycleOwner, {
                    genreList = it.toMutableList()
                    for (i in genreList) {
                        itemName.add(i.name)
                    }
                })
            } else if (item == "actor") {
                if (System.currentTimeMillis() > lastTextEdit + delay - 500) {
                    searchForActor(Uri.encode(actorInput.text.toString()))
                    actor.observe(viewLifecycleOwner, {
                        actorList = it as MutableList<Actor>
                        for (i in actorList) {
                            itemName.add(i.name)
                        }
//                        itemInput.setAdapter(DiscoverAdapter(requireContext(), itemInput.id, it))
                    })
                }
            }

            error.observe(viewLifecycleOwner, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_popup_window_item,
            itemName
        )
        adapter.notifyDataSetChanged()
        listPopupWindow.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindow.setOnItemClickListener
        { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            // Respond to list popup window item click.
            if (item == "genre") {
                itemInput.setText(genreList[position].name)
                genreId = genreList[position].id
            } else if (item == "actor") {
                itemInput.setText(actorList[position].name)
                actorId = actorList[position].id
            }
            adapter.notifyDataSetChanged()
            // Dismiss popup.
            listPopupWindow.dismiss()
        }

        // Show list popup window on button click.
        itemInput.setOnClickListener
        { listPopupWindow.show() }
    }
}
