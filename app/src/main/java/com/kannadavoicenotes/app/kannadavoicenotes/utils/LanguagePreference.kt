package com.kannadavoicenotes.app.kannadavoicenotes.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import app.speechtotext.Language

/**
 * Created by varun.am on 05/12/18
 */
object LanguagePreference {

    private val PREF_LANG_KEY = "pref-lang-key"
    private val LANG_KEY = "lang-key"

    private var langPref: SharedPreferences? = null

    public fun setLanguage(context: Context, language: Language) {
        langPref = context.getSharedPreferences(PREF_LANG_KEY, MODE_PRIVATE)
        val editor = langPref!!.edit()
        editor.putString(LANG_KEY, language.name)
        editor.apply()
    }

    public fun getSelectedLanguage(context: Context): Language {
        langPref = context.getSharedPreferences(PREF_LANG_KEY, MODE_PRIVATE)
        return getLangFromString(langPref!!.getString(LANG_KEY, Language.KANNADA.name))
    }

    private fun getLangFromString(languageInString: String?): Language {
        when (languageInString) {
            Language.KANNADA.name -> return Language.KANNADA
            Language.TAMIL.name -> return Language.TAMIL
            Language.TELUGU.name -> return Language.TELUGU
            Language.MALAYALAM.name -> return Language.MALAYALAM
            Language.HINDI.name -> return Language.HINDI
        }
        return Language.KANNADA
    }
}