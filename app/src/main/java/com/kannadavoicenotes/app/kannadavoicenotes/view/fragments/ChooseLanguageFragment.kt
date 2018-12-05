package com.kannadavoicenotes.app.kannadavoicenotes.view.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.speechtotext.Language
import app.speechtotext.SpeechToTextConverter
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.adapter.LangAdapter
import com.kannadavoicenotes.app.kannadavoicenotes.interfaces.LangSelectedListener
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference

/**
 * Created by varun.am on 05/12/18
 */
class ChooseLanguageFragment : Fragment(), LangSelectedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_choose_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.lang_recycler_view_id)
        val adapter = LangAdapter(SpeechToTextConverter.getAvailableLanguages(), this)
        recyclerView.layoutManager = LinearLayoutManager(activity!!)
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): ChooseLanguageFragment {
            val fragment = ChooseLanguageFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onLangSelected(language: Language) {
        LanguagePreference.setLanguage(activity!!, language)
        Toast.makeText(activity!!, "Selected ${language.name}", Toast.LENGTH_LONG).show()
        Handler().postDelayed({
            activity!!.supportFragmentManager.popBackStack()
        }, 1000)
    }
}