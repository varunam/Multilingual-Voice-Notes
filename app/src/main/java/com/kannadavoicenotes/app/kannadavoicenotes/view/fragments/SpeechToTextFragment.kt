package com.kannadavoicenotes.app.kannadavoicenotes.view.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdRequest.*
import com.google.android.gms.ads.AdView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedText
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedTextDatabase
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference.LANG_KEY
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference.PREF_LANG_KEY
import com.kannadavoicenotes.app.kannadavoicenotes.viewmodel.MainViewModel


/**
 * Created by varun.am on 05/12/18
 */
class SpeechToTextFragment : Fragment() {

    private var mainViewModel: MainViewModel? = null
    private var resultTextView: TextView? = null
    private var copyContent: FloatingActionButton? = null
    private var shareContent: FloatingActionButton? = null
    private var bannerAdView: AdView? = null
    private var langPref: SharedPreferences? = null

    companion object {
        const val SpeechToTextKey = 100
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
        bannerAdView = view.findViewById(R.id.bannerAdViewId)
        langPref = context!!.getSharedPreferences(PREF_LANG_KEY, MODE_PRIVATE)
        langPref!!.registerOnSharedPreferenceChangeListener(sharedPrefChangedListener)
        bannerAdView!!.adListener = bannerAdListener
        loadBannerAd()
        val resultTextDefaultString =
            resources.getString(R.string.tap_to_speak) + "\nLanguage Selected: " + LanguagePreference.getSelectedLanguage(
                activity!!
            ).name
        resultTextView!!.text = resultTextDefaultString
        hideFabs()
        val micLayout = view.findViewById<ImageView>(R.id.micId)
        micLayout.setOnClickListener {
            val speechToTextConverter = SpeechToTextConverter(activity!!)
            speechToTextConverter.start(
                LanguagePreference.getSelectedLanguage(activity!!),
                getPromptForChosenLanguage(),
                SpeechToTextKey
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

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        bannerAdView!!.loadAd(adRequest)
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

    private var bannerAdListener = object : AdListener() {
        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.d(TAG, "Banner Ad Loaded successfully!")
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            // Code to be executed when an ad request fails.
            when (errorCode) {
                ERROR_CODE_INTERNAL_ERROR -> Log.e(
                    TAG,
                    "Couldn't load bannerAd: Unknown error. Might be invalid response from server"
                )
                ERROR_CODE_NETWORK_ERROR -> Log.e(TAG, "Couldn't load bannerAd: BAD internet connectivity")
                ERROR_CODE_INVALID_REQUEST -> Log.e(
                    TAG,
                    "Couldn't load bannerAd: Invalid request. Check IDs used are correct"
                )
                ERROR_CODE_NO_FILL -> Log.e(TAG, "Couldn't load bannerAd: No ads available to load")
                else ->
                    Log.e(TAG, "Banner Ad failed to load. Error Code: $errorCode")
            }
        }

        override fun onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.d(TAG, "Banner Ad is opened")
        }

        override fun onAdLeftApplication() {
            // Code to be executed when the user has left the app.
            Log.d(TAG, "User left the application")
        }

        override fun onAdClosed() {
            // Code to be executed when when the user is about to return
            // to the app after tapping on an ad.
            Log.d(TAG, "Banner Ad closed successfully!")
        }

    }

    private var sharedPrefChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPref, key ->
            if (key == LANG_KEY) {
                val resultTextDefaultString =
                    resources.getString(R.string.tap_to_speak) + "\nLanguage Selected: " + LanguagePreference.getSelectedLanguage(
                        activity!!
                    ).name
                resultTextView!!.text = resultTextDefaultString
            }
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