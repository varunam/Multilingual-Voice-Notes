package com.kannadavoicenotes.app.kannadavoicenotes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * Created by varun.am on 05/12/18
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    var resultReceived = MutableLiveData<String>()

}