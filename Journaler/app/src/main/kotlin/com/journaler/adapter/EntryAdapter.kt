package com.journaler.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.journaler.R
import com.journaler.model.Entry

class EntryAdapter(
        private val ctx: Context,
        private val items: List<Entry>
) : BaseAdapter() {

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        p1?.let {
            return p1
        }
        val inflater = LayoutInflater.from(ctx)
        val view = inflater.inflate(R.layout.adapter_entry, null)
        val label = view.findViewById<TextView>(R.id.title)
        label.text = items[p0].title
        return view
    }

    override fun getItem(p0: Int): Entry = items[p0]

    override fun getItemId(p0: Int): Long = items[p0].id

    override fun getCount(): Int = items.size

}