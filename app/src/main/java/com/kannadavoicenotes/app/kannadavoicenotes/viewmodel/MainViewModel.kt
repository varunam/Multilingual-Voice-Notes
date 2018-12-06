package com.kannadavoicenotes.app.kannadavoicenotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedText
import com.kannadavoicenotes.app.kannadavoicenotes.data.model.ConvertedTextDatabase

/**
 * Created by varun.am on 05/12/18
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    var resultReceived = MutableLiveData<String>()
    var convertedTextList: LiveData<List<ConvertedText>>? = null

    init {
        convertedTextList = ConvertedTextDatabase.getInstance(application.applicationContext)!!
            .convertedTextDao()
            .loadAllConvertedText()
    }

}