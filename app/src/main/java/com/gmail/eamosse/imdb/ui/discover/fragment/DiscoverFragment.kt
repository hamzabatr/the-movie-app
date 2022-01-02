package com.gmail.eamosse.imdb.ui.discover.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.gmail.eamosse.idbdata.data.Actor
import com.gmail.eamosse.idbdata.data.Category
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.FragmentDiscoverBinding
import com.gmail.eamosse.imdb.ui.discover.adapter.DiscoverAdapter
import com.gmail.eamosse.imdb.ui.discover.viewModel.DiscoverViewModel
import kotlinx.android.synthetic.main.category_list_item.*
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverFragment : Fragment(), TextWatcher {
    private lateinit var binding: FragmentDiscoverBinding
    private val discoverViewModel: DiscoverViewModel by viewModel()
    private var genre: Boolean = false
    private var actor: Boolean = false
    private var year: Boolean = false
    private var genreId: Int = 0
    private lateinit var genreList: MutableList<Category>
    private lateinit var actorList: MutableList<Actor>
    private var actorId: Int = 0
    private var delay: Long = 1000
    private var lastTextEdit: Long = 0
    private var handler: Handler = Handler()
    private val inputFinishChecker = Runnable {
        showMenu(actorInput, "actor")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        with(binding) {
            genreInput.addTextChangedListener(this@DiscoverFragment)
            actorInput.addTextChangedListener(this@DiscoverFragment)
            yearInput.addTextChangedListener(this@DiscoverFragment)
            genreInput.setOnClickListener {
                showMenu(genreInput, "genre")
            }
            discoverButton.isEnabled = false
            discoverButton.setOnClickListener {
                val text =
                    genreId.toString() + "|" + genre.toString() + "|" + actorId.toString() + "|" + actor.toString() + "|" + yearInput.text.toString() + "|" + year.toString()
                val nextAction =
                    DiscoverFragmentDirections.actionDiscoverToDiscoverSecond(
                        text
                    )
                Navigation.findNavController(it).navigate(nextAction)
            }
        }

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (actorInput.text.toString().length >= 3) {
            lastTextEdit = System.currentTimeMillis()
            handler.postDelayed(inputFinishChecker, delay)
        }

        with(binding) {
            if (genreInput.text.toString().isNotBlank() && genreInput.text.toString()
                .isNotEmpty()
            ) genre = true
            if (actorInput.text.toString().isNotBlank() && actorInput.text.toString()
                .isNotEmpty()
            ) actor = true
            if (yearInput.text.toString().isNotBlank() && yearInput.text.toString()
                .isNotEmpty()
            ) year = true

            discoverButton.isEnabled = genre || actor || year
        }
    }

    private fun showMenu(itemInput: AutoCompleteTextView, item: String) {
        val listPopupWindow = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        val itemName = mutableListOf<String>()
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_popup_window_item,
            itemName
        )

        // Set button as the list popup's anchor
        listPopupWindow.anchorView = itemInput

        // Set list popup's content
        with(discoverViewModel) {
            if (item == "genre") {
                getCategories()
                categories.observe(viewLifecycleOwner, {
                    genreList = it.toMutableList()
                    for (i in genreList) {
                        itemName.add(i.name)
                    }
                    adapter.notifyDataSetChanged()
                    listPopupWindow.setAdapter(adapter)
                })
            } else if (item == "actor") {
                if (System.currentTimeMillis() > lastTextEdit + delay - 500) {
                    searchForActor(Uri.encode(actorInput.text.toString()))
                    actor.observe(viewLifecycleOwner, {
//                        actorList = it as MutableList<Actor>
//                        for (i in actorList) {
//                            itemName.add(i.name)
//                        }
                        itemInput.setAdapter(DiscoverAdapter(requireContext(), itemInput.id, it))
                    })
                }
            }

            error.observe(viewLifecycleOwner, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        }

        // Set list popup's item click listener
        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            // Respond to list popup window item click.
            if (item == "genre") {
                itemInput.setText(genreList[position].name)
                genreId = genreList[position].id
                adapter.notifyDataSetChanged()
            } else if (item == "actor") {
                itemInput.setText(actorList[position].name)
                actorId = actorList[position].id
            }
            // Dismiss popup.
            listPopupWindow.dismiss()
        }

        // Show list popup window on button click.
        itemInput.setOnClickListener { listPopupWindow.show() }
    }
}
