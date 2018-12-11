package com.kannadavoicenotes.app.kannadavoicenotes.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedText
import com.kannadavoicenotes.app.kannadavoicenotes.interfaces.ShareClickCallbacks

/**
 * Created by varun.am on 06/12/18
 */
class HistoryAdapter(shareClickCallbacks: ShareClickCallbacks) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var textList = ArrayList<ConvertedText>()
    private var shareClickCallbacks: ShareClickCallbacks? = null

    init {
        this.shareClickCallbacks = shareClickCallbacks
    }

    fun setTextList(textList: ArrayList<ConvertedText>) {
        this.textList = textList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return textList.size
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        holder.text.text = textList[position].text
        holder.timeStamp.text = textList[position].dateAdded.toString()
        holder.share.setOnClickListener {
            shareClickCallbacks!!.onShareButtonClicked(textList[position])
        }
        holder.copyIcon.setOnClickListener {
            copyToClipboard(holder.copyIcon.context, textList[position].text!!)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout = itemView.findViewById<ConstraintLayout>(R.id.history_item_id)!!
        var text = itemView.findViewById<TextView>(R.id.history_text_id)!!
        var share = itemView.findViewById<ImageView>(R.id.history_share_id)!!
        var timeStamp = itemView.findViewById<TextView>(R.id.history_timestamp_id)!!
        var copyIcon = itemView.findViewById<ImageView>(R.id.history_copy_icon)!!
    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard.primaryClip = clip
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_LONG).show()
    }
}