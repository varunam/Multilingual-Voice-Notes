package com.kannadavoicenotes.app.kannadavoicenotes.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import app.speechtotext.Language
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.interfaces.LangSelectedListener
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference

/**
 * Created by varun.am on 05/12/18
 */
class LangAdapter() : RecyclerView.Adapter<LangAdapter.ViewHolder>() {

    private var langList: ArrayList<Language>? = null
    private var langSelectedListener: LangSelectedListener? = null

    constructor(langList: ArrayList<Language>, langSelectedListener: LangSelectedListener) : this() {
        this.langList = langList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LangAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_lang_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return langList!!.size
    }

    override fun onBindViewHolder(viewHolder: LangAdapter.ViewHolder, position: Int) {
        viewHolder.langLayout!!.setOnClickListener {
            if (langSelectedListener != null) {
                LanguagePreference.setLanguage(viewHolder.langLayout!!.context, langList!![position])
                langSelectedListener!!.onLangSelected(langList!![position])
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var langLayout: LinearLayout? = null

        init {
            langLayout = itemView.findViewById(R.id.lang_item_id)
        }
    }

}