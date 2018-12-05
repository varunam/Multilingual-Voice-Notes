package com.kannadavoicenotes.app.kannadavoicenotes.view.activities

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import app.speechtotext.SpeechConvertedListener
import app.speechtotext.SpeechToTextConverter
import app.speechtotext.SpeechToTextConverter.TEXT_TO_SPEECH_REQUEST_KEY
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.utils.LanguagePreference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SpeechConvertedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speechToTextConverter = SpeechToTextConverter(this)
        testButton.setOnClickListener {
            speechToTextConverter.start(LanguagePreference.getSelectedLanguage(applicationContext), "Try something")
        }
    }

    override fun onSpeechConvertedToText(resultText: String?) {
        Toast.makeText(applicationContext, resultText, Toast.LENGTH_LONG).show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TEXT_TO_SPEECH_REQUEST_KEY) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                Toast.makeText(applicationContext, result[0], Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
