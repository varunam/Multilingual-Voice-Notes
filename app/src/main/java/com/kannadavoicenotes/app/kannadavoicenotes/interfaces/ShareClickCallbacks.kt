package com.kannadavoicenotes.app.kannadavoicenotes.interfaces

import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedText

/**
 * Created by varun.am on 06/12/18
 */
interface ShareClickCallbacks {
    fun onShareButtonClicked(convertedText: ConvertedText)
}