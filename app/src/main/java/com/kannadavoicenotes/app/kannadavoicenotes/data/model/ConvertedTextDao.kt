package com.kannadavoicenotes.app.kannadavoicenotes.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by varun.am on 06/12/18
 */
@Dao
interface ConvertedTextDao {

    @Query("SELECT * FROM converted_text_table ORDER BY dateAdded DESC")
    fun loadAllConvertedText(): LiveData<List<ConvertedText>>

    @Insert
    fun insertConvertedText(convertedText: ConvertedText)

    @Query("DELETE FROM converted_text_table WHERE text = :convertedText")
    fun deleteConvertedText(convertedText: String)

    @Query("DELETE FROM converted_text_table")
    fun deleteAllConvertedText()
}