package com.kannadavoicenotes.app.kannadavoicenotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.adapter.HistoryAdapter
import com.kannadavoicenotes.app.kannadavoicenotes.viewmodel.MainViewModel

/**
 * Created by varun.am on 06/12/18
 */
class HistoryFragment : Fragment() {

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
        adapter = HistoryAdapter()
        recyclerView!!.layoutManager = LinearLayoutManager(context!!)
        recyclerView!!.adapter = adapter

    }

    private fun retrieveSavedConvertedText() {
        mainViewModel!!.convertedTextList!!.observe(this, Observer {
            adapter!!.setTextList(ArrayList(it))
        })
    }
}