package com.kannadavoicenotes.app.kannadavoicenotes.view.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.speechtotext.Language
import app.speechtotext.SpeechToTextConverter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedText
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedTextDatabase
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference
import com.kannadavoicenotes.app.kannadavoicenotes.viewmodel.MainViewModel


/**
 * Created by varun.am on 05/12/18
 */
class SpeechToTextFragment : Fragment() {

    private var mainViewModel: MainViewModel? = null
    private var resultTextView: TextView? = null
    private var copyContent: FloatingActionButton? = null
    private var shareContent: FloatingActionButton? = null

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
        copyContent = view.findViewById(R.id.copy_content_fab_id)
        shareContent = view.findViewById(R.id.share_content_fab_id)
        resultTextView = view.findViewById(R.id.result_text_id)
        hideFabs()
        val micLayout = view.findViewById<ImageView>(R.id.micId)
        micLayout.setOnClickListener {
            val speechToTextConverter = SpeechToTextConverter(activity!!)
            speechToTextConverter.start(
                LanguagePreference.getSelectedLanguage(activity!!),
                getPromptForChosenLanguage()
            )

        }

        copyContent!!.setOnClickListener {
            copyToClipboard(resultTextView!!.text.toString())
        }

        shareContent!!.setOnClickListener {
            val mimeType = "text/plain"
            val shareText = resultTextView!!.text.toString() + "\nShared via Multi-lingual Voice Notes"
            val title = "share via"
            ShareCompat.IntentBuilder.from(activity!!)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(shareText)
                .startChooser()
        }
    }

    private fun getPromptForChosenLanguage(): String? {
        return when (LanguagePreference.getSelectedLanguage(activity!!)) {
            Language.KANNADA -> resources.getString(R.string.kannada_prompt)
            Language.HINDI -> resources.getString(R.string.hindi_prompt)
            Language.MALAYALAM -> resources.getString(R.string.malayalam_prompt)
            Language.TELUGU -> resources.getString(R.string.telugu_prompt)
            Language.TAMIL -> resources.getString(R.string.tamil_prompt)
            else -> resources.getString(R.string.english_prompt)
        }
    }

    private fun showFabs() {
        copyContent!!.show()
        shareContent!!.show()
        val scaleUpAnim = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        copyContent!!.startAnimation(scaleUpAnim)
        shareContent!!.startAnimation(scaleUpAnim)
    }

    private fun hideFabs() {
        copyContent!!.hide()
        shareContent!!.hide()
    }

    private var resultObserver = Observer<String> {
        showFabs()
        resultTextView!!.text = it

        //inserting data
        Thread {
            ConvertedTextDatabase.getInstance(activity!!)!!
                .convertedTextDao()
                .insertConvertedText(
                    ConvertedText(it)
                )
        }.start()
    }

    private fun copyToClipboard(text: String) {
        val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard.primaryClip = clip
        Toast.makeText(activity!!, "Copied to clipboard", Toast.LENGTH_LONG).show()
    }

}