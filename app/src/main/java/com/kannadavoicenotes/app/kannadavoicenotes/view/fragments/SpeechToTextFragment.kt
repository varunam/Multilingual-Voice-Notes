package com.kannadavoicenotes.app.kannadavoicenotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.speechtotext.SpeechToTextConverter
import com.kannadavoicenotes.app.kannadavoicenotes.MainViewModel
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference

/**
 * Created by varun.am on 05/12/18
 */
class SpeechToTextFragment : Fragment() {

    private var mainViewModel: MainViewModel? = null

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
        val micLayout = view.findViewById<ImageView>(R.id.micId)
        micLayout.setOnClickListener {
            val speechToTextConverter = SpeechToTextConverter(activity!!)
            speechToTextConverter.start(LanguagePreference.getSelectedLanguage(activity!!), null)
        }
    }

    private var resultObserver = Observer<String>{

    }

}