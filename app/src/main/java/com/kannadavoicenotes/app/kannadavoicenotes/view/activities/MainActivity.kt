package com.kannadavoicenotes.app.kannadavoicenotes.view.activities

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import app.speechtotext.SpeechConvertedListener
import app.speechtotext.SpeechToTextConverter.TEXT_TO_SPEECH_REQUEST_KEY
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.view.fragments.ChooseLanguageFragment

class MainActivity : AppCompatActivity(), SpeechConvertedListener {

    var chooseLanguageFragment: ChooseLanguageFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun launchChooseLangFragment() {
        if (chooseLanguageFragment == null)
            chooseLanguageFragment = ChooseLanguageFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container_id, chooseLanguageFragment!!)
            .addToBackStack(ChooseLanguageFragment::class.java.simpleName)
            .commit()
    }

    override fun onSpeechConvertedToText(resultText: String?) {
        Toast.makeText(applicationContext, resultText, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.change_lang_id) {
            launchChooseLangFragment()
        }
        return super.onOptionsItemSelected(item)
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
