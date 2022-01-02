package com.gmail.eamosse.imdb.ui.discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.NonNull
import com.gmail.eamosse.imdb.R

class DiscoverAdapter(
    mContext: Context,
    private val itemLayout: Int,
    private var actorList: List<Any>
) :
    ArrayAdapter<Any>(mContext, itemLayout, actorList) {
    private val listFilter: ListFilter = ListFilter()

    override fun getCount(): Int {
        return actorList.size
    }

    override fun getItem(position: Int): Any {
        return actorList[position]
    }

    override fun getView(position: Int, view: View?, @NonNull parent: ViewGroup): View {
        var mView = view
        if (view == null) {
            mView = LayoutInflater.from(parent.context)
                .inflate(itemLayout, parent, false)
        }
        val strName: TextView = mView!!.findViewById<View>(R.id.actorInput) as TextView
        strName.text = getItem(position).javaClass.name
        return mView
    }

    @NonNull
    override fun getFilter(): Filter {
        return listFilter
    }

    inner class ListFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            if (prefix == null || prefix.isEmpty()) {
                results.values = ArrayList<Any>()
                results.count = 0
            } else {
                results.values = actorList
                results.count = actorList.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            actorList = if (results.values != null) (
                listOf {
                    results.values
                }
                ) else emptyList()

            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}
