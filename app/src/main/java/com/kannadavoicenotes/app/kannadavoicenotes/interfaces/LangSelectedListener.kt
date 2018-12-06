package com.kannadavoicenotes.app.kannadavoicenotes.interfaces

import app.speechtotext.Language

/**
 * Created by varun.am on 05/12/18
 */
interface LangSelectedListener {
    fun onLangSelected(language: Language)
}