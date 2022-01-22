package com.gmail.eamosse.imdb.ui.discover.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.FragmentDiscoverBinding
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
    private var idList: MutableList<Int> = ArrayList()
    private var genreId: Int = 0
    private var actorId: Int = 0
    private var delay: Long = 1000
    private var lastTextEdit: Long = 0
    private var handler: Handler = Handler()
    private val actorInputFinishChecker = Runnable {
        with(binding) {
            if (!actorInput.text.isNullOrEmpty()) {
                showMenu(actorInput, "actor", requireContext())
            }
        }
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
            genreInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showMenu(
                        genreInput, "genre", requireContext()
                    )
                }
            }
            genreInput.setOnClickListener {
                showMenu(genreInput, "genre", requireContext())
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
            handler.postDelayed(actorInputFinishChecker, delay)
        }

        with(binding) {
            genre = genreInput.text.toString().isNotBlank() && genreInput.text.toString()
                .isNotEmpty()
            actor = actorInput.text.toString().isNotBlank() && actorInput.text.toString()
                .isNotEmpty()
            yearInput.text.toString().isNotBlank() && yearInput.text.toString()
                .isNotEmpty()

            discoverButton.isEnabled = genre || actor || year
        }
    }

    private fun showMenu(itemInput: AutoCompleteTextView, item: String, context: Context) {
        val listPopupWindow = ListPopupWindow(context, null, R.attr.listPopupWindowStyle)
        val itemName: MutableList<String> = ArrayList()
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_popup_window_item,
            itemName
        )

        // Set button as the list popup's anchor
        listPopupWindow.anchorView = itemInput

        // Set list popup's item click listener
        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            val nameTemp = itemName[position]
            adapter.clear()
            adapter.notifyDataSetChanged()
            itemInput.setText(nameTemp)

            // Respond to list popup window item click.
            if (item == "genre") {
                genreId = idList[position]
            } else if (item == "actor") {
                actorId = idList[position]
            }
            idList.clear()

            // Dismiss popup.
            listPopupWindow.dismiss()
        }

        // Set list popup's content
        with(discoverViewModel) {
            if (item == "genre") {
                getCategories()
                categories.observe(viewLifecycleOwner, {
                    it.forEach { c ->
                        adapter.add(c.name)
                        idList.add(c.id)
                    }
                })
                if (!genre) listPopupWindow.show()
            } else if (item == "actor") {
                searchForActor(
                    Uri.encode(
                        actorInput.text.toString()
                    )
                )
                actors.observe(viewLifecycleOwner, {
                    it.forEach { a ->
                        adapter.add(a.name)
                        idList.add(a.id)
                    }
                })
                if (!actor) listPopupWindow.show()
            }

            error.observe(viewLifecycleOwner, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        }

        adapter.notifyDataSetChanged()
        listPopupWindow.setAdapter(adapter)

        // Show list popup window on button click.
        itemInput.setOnClickListener {
            listPopupWindow.show()
        }
    }
}
