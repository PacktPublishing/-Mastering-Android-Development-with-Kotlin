package com.journaler.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.journaler.R
import com.journaler.activity.NoteActivity
import com.journaler.activity.TodoActivity
import com.journaler.model.MODE


class ItemsFragment : BaseFragment() {

    override val logTag = "Items fragment"

    override fun getLayout(): Int {
        return R.layout.fragment_items
    }

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(getLayout(), container, false)
        val btn = view?.findViewById<FloatingActionButton>(R.id.new_item)

        btn?.setOnClickListener {
            val items = arrayOf(
                    getString(R.string.todos),
                    getString(R.string.notes)
            )

            val builder = AlertDialog.Builder(this@ItemsFragment.context)
                    .setTitle(R.string.choose_a_type)
                    .setItems(
                            items,
                            { _, which ->
                                when (which) {
                                    0 -> {
                                        openCreateNote()
                                    }
                                    1 -> {
                                        openCreateTodo()
                                    }
                                    else -> Log.e(logTag, "Unknown option selected [ $which ]")
                                }
                            }
                    )

            builder.show()
        }

        return view
    }

    private fun openCreateNote() {
        val intent = Intent(context, NoteActivity::class.java)
        intent.putExtra(MODE.EXTRAS_KEY, MODE.CREATE.mode)
        startActivity(intent)
    }

    private fun openCreateTodo() {
        val intent = Intent(context, TodoActivity::class.java)
        intent.putExtra(MODE.EXTRAS_KEY, MODE.CREATE.mode)
        startActivity(intent)
    }

}