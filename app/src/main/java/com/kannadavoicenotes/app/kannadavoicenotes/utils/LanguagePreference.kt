package com.kannadavoicenotes.app.kannadavoicenotes.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import app.speechtotext.Language
import app.speechtotext.SpeechToTextConverter

/**
 * Created by varun.am on 05/12/18
 */
object LanguagePreference {

    public const val PREF_LANG_KEY = "pref-lang-key"
    public const val LANG_KEY = "lang-key"

    private var langPref: SharedPreferences? = null

    fun setLanguage(context: Context, language: Language) {
        langPref = context.getSharedPreferences(PREF_LANG_KEY, MODE_PRIVATE)
        val editor = langPref!!.edit()
        editor.putString(LANG_KEY, language.name)
        editor.apply()
    }

    fun getSelectedLanguage(context: Context): Language {
        langPref = context.getSharedPreferences(PREF_LANG_KEY, MODE_PRIVATE)
        return getLangFromString(langPref!!.getString(LANG_KEY, Language.KANNADA.name))
    }

    private fun getLangFromString(languageInString: String?): Language {
        for (language in SpeechToTextConverter.getAvailableLanguages()) {
            if (language.name == languageInString)
                return language
        }
        return Language.KANNADA
    }
}