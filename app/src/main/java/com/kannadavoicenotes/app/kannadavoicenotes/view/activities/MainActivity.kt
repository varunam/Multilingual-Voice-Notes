package com.kannadavoicenotes.app.kannadavoicenotes.view.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import app.speechtotext.SpeechConvertedListener
import com.github.amlcurran.showcaseview.ShowcaseView
import com.github.amlcurran.showcaseview.targets.ViewTarget
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.kannadavoicenotes.app.kannadavoicenotes.BuildConfig
import com.kannadavoicenotes.app.kannadavoicenotes.R
import com.kannadavoicenotes.app.kannadavoicenotes.adapter.ViewPagerAdapter
import com.kannadavoicenotes.app.kannadavoicenotes.utils.AppInfo
import com.kannadavoicenotes.app.kannadavoicenotes.view.fragments.ChooseLanguageFragment
import com.kannadavoicenotes.app.kannadavoicenotes.view.fragments.SpeechToTextFragment.Companion.SpeechToTextKey
import com.kannadavoicenotes.app.kannadavoicenotes.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), SpeechConvertedListener {

    private var chooseLanguageFragment: ChooseLanguageFragment? = null
    private var mainViewModel: MainViewModel? = null
    private var toolbar: Toolbar? = null
    private var interstitialAd: InterstitialAd? = null
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this, BuildConfig.AdmobAppId)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        interstitialAd = InterstitialAd(this)
        interstitialAd!!.adListener = interstitialAdListener
        //TODO make sure productAdId is used
        interstitialAd!!.adUnitId = BuildConfig.InterstitialAdId
        loadInterstitialAd()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        if (!AppInfo.getAppLaunched(applicationContext)) {
            showAppTutor()
        }
    }

    private fun loadInterstitialAd() {
        interstitialAd!!.loadAd(AdRequest.Builder().build())
    }

    private fun showInterstitialAd() {
        if (interstitialAd!!.isLoaded)
            interstitialAd!!.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showInterstitialAd()
    }

    private var tutor: ShowcaseView? = null
    private var counter = 0
    private fun showAppTutor() {
        tutor = ShowcaseView.Builder(this)
            .setTarget {
                val actionBarSize = toolbar!!.height
                val actionBarWidth = toolbar!!.width
                val x = actionBarWidth - actionBarWidth / 5
                val y = actionBarSize
                Point(x, y)
            }
            .setContentTitle("Choose language!")
            .setContentText("This app supports multiple Indian languages. Choose one you want to convert speech to text")
            .setStyle(R.style.ShowcaseViewTheme)
            .blockAllTouches()
            .setOnClickListener {
                if (counter < 1) {
                    tutor!!.setShowcase(ViewTarget(findViewById(R.id.micId)), true)
                    tutor!!.setContentTitle("Click and speak")
                    tutor!!.setContentText("Tap on this mic and start speaking! \nStop speaking to convert speech to text")
                    counter++
                } else if (counter < 2) {
                    tutor!!.setShowcase({
                        val actionBarSize = toolbar!!.height
                        val actionBarWidth = toolbar!!.width
                        val x = actionBarWidth - actionBarWidth / 5
                        val y = actionBarSize
                        Point(x, y)
                    }, true)
                    tutor!!.setButtonText("Finish")
                    tutor!!.setContentTitle("History of voice notes")
                    tutor!!.setContentText("Find all the text converted from speech here.")
                    counter++
                } else {
                    if (tutor != null) {
                        tutor!!.hide()
                        tutor = null
                        AppInfo.setAppLaunched(applicationContext, true)
                    }
                }
            }
            .build()

    }

    private fun launchChooseLangFragment() {
        if (chooseLanguageFragment == null)
            chooseLanguageFragment = ChooseLanguageFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_bottom, 0, 0, R.anim.exit_to_bottom)
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

    private var interstitialAdListener = object : AdListener() {
        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.d(TAG, "Interstitial Ad Loaded successfully!")
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            // Code to be executed when an ad request fails.
            when(errorCode){
                AdRequest.ERROR_CODE_INTERNAL_ERROR -> Log.e(TAG,"Couldn't load InterstitialAd: Unknown error. Might be invalid response from server")
                AdRequest.ERROR_CODE_NETWORK_ERROR -> Log.e(TAG,"Couldn't load InterstitialAd: BAD internet connectivity")
                AdRequest.ERROR_CODE_INVALID_REQUEST -> Log.e(TAG,"Couldn't load InterstitialAd: Invalid request. Check IDs used are correct")
                AdRequest.ERROR_CODE_NO_FILL -> Log.e(TAG,"Couldn't load InterstitialAd: No ads available to load")
                else ->
                    Log.e(TAG, "Banner Ad failed to load. Error Code: $errorCode")
            }
        }

        override fun onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.d(TAG, "InterstitialAd is opened")
        }

        override fun onAdLeftApplication() {
            // Code to be executed when the user has left the app.
            Log.d(TAG, "User left the application")
        }

        override fun onAdClosed() {
            // Code to be executed when when the user is about to return
            // to the app after tapping on an ad.
            Log.d(TAG, "InterstitialAd closed successfully!")
        }

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
                        "Check out this awesome multi-lingual typing assistant app which helps you type words and sentences without typing!!\n" +
                                "The app supports Kannada, Hindi, Tamil, Telugu, Malayalam and many more languages too" +
                                "\n\nDownload it now: " +
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
        if (requestCode == SpeechToTextKey) {
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
