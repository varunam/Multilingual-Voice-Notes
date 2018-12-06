package com.kannadavoicenotes.app.kannadavoicenotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.adapter.HistoryAdapter
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedText
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedTextDatabase
import com.kannadavoicenotes.app.kannadavoicenotes.interfaces.ShareClickCallbacks
import com.kannadavoicenotes.app.kannadavoicenotes.viewmodel.MainViewModel

/**
 * Created by varun.am on 06/12/18
 */
class HistoryFragment : Fragment(), ShareClickCallbacks {

    private var mainViewModel: MainViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: HistoryAdapter? = null

    companion object {
        private val TAG: String = HistoryFragment::class.java.simpleName
        fun newInstance(): HistoryFragment {
            val fragment = HistoryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        retrieveSavedConvertedText()
        recyclerView = view.findViewById(R.id.history_recycler_view_id)
        adapter = HistoryAdapter(this)
        recyclerView!!.layoutManager = LinearLayoutManager(context!!)
        recyclerView!!.adapter = adapter

        touchHelper.attachToRecyclerView(recyclerView)

    }

    private fun retrieveSavedConvertedText() {
        mainViewModel!!.convertedTextList!!.observe(this, Observer {
            adapter!!.setTextList(ArrayList(it))
        })
    }

    override fun onShareButtonClicked(convertedText: ConvertedText) {
        val mimeType = "text/plain"
        val shareText = convertedText.text + "\nShared via Multi-lingual Voice Notes"
        val title = "share via"
        ShareCompat.IntentBuilder.from(activity!!)
            .setChooserTitle(title)
            .setType(mimeType)
            .setText(shareText)
            .startChooser()
    }

    private var touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            Thread {
                ConvertedTextDatabase.getInstance(activity!!)!!
                    .convertedTextDao()
                    .deleteConvertedText(
                        (viewHolder as HistoryAdapter.ViewHolder).text.text.toString()
                    )
            }.start()
        }
    })
}