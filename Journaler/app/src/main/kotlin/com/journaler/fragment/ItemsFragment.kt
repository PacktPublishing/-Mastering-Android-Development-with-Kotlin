package com.journaler.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.journaler.R
import com.journaler.activity.NoteActivity
import com.journaler.activity.TodoActivity
import com.journaler.adapter.EntryAdapter
import com.journaler.execution.TaskExecutor
import com.journaler.model.MODE
import com.journaler.provider.JournalerProvider
import kotlinx.android.synthetic.main.fragment_items.*
import java.text.SimpleDateFormat
import java.util.*


class ItemsFragment : BaseFragment() {

    private val TODO_REQUEST = 1
    private val NOTE_REQUEST = 0
    private var adapter: EntryAdapter? = null

    override val logTag = "Items fragment"

    override fun getLayout() = R.layout.fragment_items

    private val loaderCallback = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
            cursor?.let {
                if (adapter == null) {
                    adapter = EntryAdapter(activity, cursor)
                    items.adapter = adapter
                } else {
                    adapter?.swapCursor(cursor)
                }
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>?) {
            adapter?.swapCursor(null)
        }

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                    activity,
                    Uri.parse(JournalerProvider.URL_NOTES),
                    null,
                    null,
                    null,
                    null
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(
                0, null, loaderCallback
        )
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(0, null, loaderCallback)

        val btn = view?.findViewById<FloatingActionButton>(R.id.new_item)
        btn?.let {
            animate(btn, false)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(getLayout(), container, false)
        val btn = view?.findViewById<FloatingActionButton>(R.id.new_item)

        btn?.setOnClickListener {

            animate(btn)

            val items = arrayOf(
                    getString(R.string.todos),
                    getString(R.string.notes)
            )

            val builder = AlertDialog.Builder(this@ItemsFragment.context)
                    .setTitle(R.string.choose_a_type)
                    .setCancelable(true)
                    .setOnCancelListener {
                        animate(btn, false)
                    }
                    .setItems(
                            items,
                            { _, which ->
                                when (which) {
                                    0 -> {
                                        openCreateTodo()
                                    }
                                    1 -> {
                                        openCreateNote()
                                    }
                                    else -> Log.e(logTag, "Unknown option selected [ $which ]")
                                }
                            }
                    )

            builder.show()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TODO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(logTag, "We created new TODO.")
                } else {
                    Log.w(logTag, "We didn't created new TODO.")
                }
            }
            NOTE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(logTag, "We created new note.")
                } else {
                    Log.w(logTag, "We didn't created new note.")
                }
            }
        }
    }

    private fun animate(btn: FloatingActionButton, expand: Boolean = true) {
        val animation1 = ObjectAnimator.ofFloat(btn, "scaleX", if (expand) {
            1.5f
        } else {
            1.0f
        })
        animation1.duration = 2000
        animation1.interpolator = BounceInterpolator()

        val animation2 = ObjectAnimator.ofFloat(btn, "scaleY", if (expand) {
            1.5f
        } else {
            1.0f
        })
        animation2.duration = 2000
        animation2.interpolator = BounceInterpolator()

        val animation3 = ObjectAnimator.ofFloat(btn, "alpha", if (expand) {
            0.3f
        } else {
            1.0f
        })
        animation3.duration = 500
        animation3.interpolator = AccelerateInterpolator()

        val set = AnimatorSet()
        set.play(animation1).with(animation2).before(animation3)
        set.start()
    }

    private fun openCreateNote() {
        val intent = Intent(context, NoteActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRAS_KEY, MODE.CREATE.mode)
        intent.putExtras(data)
        startActivityForResult(intent, NOTE_REQUEST)
    }

    private fun openCreateTodo() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MMM dd YYYY", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("MM:HH", Locale.ENGLISH)

        val intent = Intent(context, TodoActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRAS_KEY, MODE.CREATE.mode)
        data.putString(TodoActivity.EXTRA_DATE, dateFormat.format(date))
        data.putString(TodoActivity.EXTRA_TIME, timeFormat.format(date))
        intent.putExtras(data)
        startActivityForResult(intent, TODO_REQUEST)
    }

}