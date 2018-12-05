package com.kannadavoicenotes.app.kannadavoicenotes.view.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.speechtotext.SpeechToTextConverter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference
import com.kannadavoicenotes.app.kannadavoicenotes.viewmodel.MainViewModel


/**
 * Created by varun.am on 05/12/18
 */
class SpeechToTextFragment : Fragment() {

    private var mainViewModel: MainViewModel? = null
    private var resultTextView: TextView? = null

    companion object {
        private val TAG: String = SpeechToTextFragment::class.java.simpleName
        fun newInstance(): SpeechToTextFragment {
            val fragment = SpeechToTextFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(container!!.context).inflate(R.layout.layout_speech_to_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mainViewModel!!.resultReceived.observe(this, resultObserver)
        resultTextView = view.findViewById(R.id.result_text_id)
        resultTextView!!.text = ""
        val micLayout = view.findViewById<ImageView>(R.id.micId)
        micLayout.setOnClickListener {
            val speechToTextConverter = SpeechToTextConverter(activity!!)
            speechToTextConverter.start(LanguagePreference.getSelectedLanguage(activity!!), null)
        }

        val copyContent = view.findViewById<FloatingActionButton>(R.id.copy_content_fab_id)
        copyContent.setOnClickListener {
            copyToClipboard(resultTextView!!.text.toString())
        }
    }

    private var resultObserver = Observer<String> {
        resultTextView!!.text = it
    }

    private fun copyToClipboard(text: String) {
        val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard.primaryClip = clip
        Toast.makeText(activity!!, "Copied to clipboard", Toast.LENGTH_LONG).show()
    }

}