package com.kannadavoicenotes.app.kannadavoicenotes.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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

    constructor(langList: List<Language>, langSelectedListener: LangSelectedListener) : this() {
        this.langList = ArrayList(langList)
        this.langSelectedListener = langSelectedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LangAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_lang_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return langList!!.size
    }

    override fun onBindViewHolder(viewHolder: LangAdapter.ViewHolder, position: Int) {
        viewHolder.langTitle!!.text = langList!![position].name

        if (langList!![position].name == LanguagePreference.getSelectedLanguage(viewHolder.langLayout!!.context).name) {
            viewHolder.langSelected!!.visibility = View.VISIBLE
        } else
            viewHolder.langSelected!!.visibility = View.GONE

        viewHolder.langLayout!!.setOnClickListener {
            if (langSelectedListener != null) {
                LanguagePreference.setLanguage(viewHolder.langLayout!!.context, langList!![position])
                langSelectedListener!!.onLangSelected(langList!![position])
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var langLayout: LinearLayout? = null
        var langTitle: TextView? = null
        var langSelected: ImageView? = null

        init {
            langLayout = itemView.findViewById(R.id.lang_item_id)
            langTitle = itemView.findViewById(R.id.lang_text_id)
            langSelected = itemView.findViewById(R.id.lang_chosen_mark_id)
        }
    }

}