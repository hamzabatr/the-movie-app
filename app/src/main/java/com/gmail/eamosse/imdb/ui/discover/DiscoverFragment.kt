package com.gmail.eamosse.imdb.ui.discover

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gmail.eamosse.idbdata.data.Category
import com.gmail.eamosse.imdb.R
import com.gmail.eamosse.imdb.databinding.FragmentDiscoverBinding
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
    private var categoryList = emptyList<Category>()
    private var categoryName = mutableListOf<String>()
    private var genreId: Int = 0

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
            with(discoverViewModel) {
                token.observe(viewLifecycleOwner, {
                    getCategories()
                })

                categories.observe(viewLifecycleOwner, {
                    categoryList = it
                    for (i in categoryList) {
                        categoryName.add(i.name)
                    }
                })

                error.observe(viewLifecycleOwner, {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                })
            }
            genreInput.setOnClickListener {
                showMenu()
            }
            discoverButton.isEnabled = false
            discoverButton.setOnClickListener {
                val text =
                    genreInput.text.toString() + " " + actorInput.text.toString() + " " + yearInput.text.toString()
                Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        with(binding) {
            if (genreInput.text.toString() != getString(R.string.genre_help)) genre = true
            if (actorInput.text.toString().isNotBlank() && actorInput.text.toString()
                .isNotEmpty()
            ) actor = true
            if (yearInput.text.toString().isNotBlank() && yearInput.text.toString()
                .isNotEmpty()
            ) year = true

            discoverButton.isEnabled = genre || actor || year
        }
    }

    private fun showMenu() {
        val listPopupWindowButton = binding.genreInput
        val listPopupWindow = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)

        // Set button as the list popup's anchor
        listPopupWindow.anchorView = listPopupWindowButton

        // Set list popup's content
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.genre_list_popup_window_item,
            categoryName
        )
        listPopupWindow.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            // Respond to list popup window item click.
            genreInput.setText(categoryList[position].name)
            genreId = categoryList[position].id
            // Dismiss popup.
            listPopupWindow.dismiss()
        }

        // Show list popup window on button click.
        listPopupWindowButton.setOnClickListener { listPopupWindow.show() }
    }
}
