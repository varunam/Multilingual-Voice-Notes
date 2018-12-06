package com.kannadavoicenotes.app.kannadavoicenotes.view.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import app.speechtotext.SpeechConvertedListener
import app.speechtotext.SpeechToTextConverter.TEXT_TO_SPEECH_REQUEST_KEY
import com.google.android.material.tabs.TabLayout
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.adapter.ViewPagerAdapter
import com.kannadavoicenotes.app.kannadavoicenotes.utils.AppInfo
import com.kannadavoicenotes.app.kannadavoicenotes.view.fragments.ChooseLanguageFragment
import com.kannadavoicenotes.app.kannadavoicenotes.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), SpeechConvertedListener {

    private var chooseLanguageFragment: ChooseLanguageFragment? = null
    private var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

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
        when {
            item!!.itemId == R.id.change_lang_id -> launchChooseLangFragment()
            item.itemId == R.id.rate_app_id -> {
                val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    goToMarket.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                Intent.FLAG_ACTIVITY_NO_HISTORY or
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                    )
                } else {
                    goToMarket.addFlags(
                        Intent.FLAG_ACTIVITY_NO_HISTORY or
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                    )
                }

                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                        )
                    )
                }
                Toast.makeText(applicationContext, "Please review us on Play-Store", Toast.LENGTH_SHORT).show()
            }
            item.itemId == R.id.share_app_id -> {
                ShareCompat.IntentBuilder.from(this)
                    .setChooserTitle("share via")
                    .setText(
                        "Check out this awesome multi-lingual voice notes app by the developers of \"Learn Kannada SmartApp\"" +
                                "\n\nDownload from here: " +
                                AppInfo.getAppLinkOnPlayStore(applicationContext)
                    ).setType("text/plain")
                    .startChooser()
            }
            item.itemId == R.id.feedback_id -> {
                val company = arrayOf<String>(resources.getString(R.string.hitham_email))
                val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null))
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.want_to_contact))
                intent.putExtra(Intent.EXTRA_EMAIL, company)
                intent.putExtra(Intent.EXTRA_TEXT, getEmailBody())
                if (intent.resolveActivity(packageManager) != null) {
                    Toast.makeText(applicationContext, R.string.FRAMING_EMAIL, Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getEmailBody(): String? {
        val body = StringBuilder()
        body.append(resources.getString(R.string.hello_hitham) + ", \n \n")
        body.append(resources.getString(R.string.please_enter_msg_here) + "\n\n")
        body.append("\n " + resources.getString(R.string.regards) + "\n")
        body.append(resources.getString(R.string.multilingual_user))
        body.append(
            "\n\nSent from my MVN " + AppInfo.getAppVersion(applicationContext)
        )
        return body.toString()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TEXT_TO_SPEECH_REQUEST_KEY) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                //posting result received
                mainViewModel!!.resultReceived.postValue(result[0])
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
