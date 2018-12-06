package com.kannadavoicenotes.app.kannadavoicenotes.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by varun.am on 06/12/18
 */
@Entity(tableName = "converted_text_table")
class ConvertedText {

    var text: String? = null
    var dateAdded: Date? = null
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(id: Int, text: String){
        this.id = id
        this.text = text
        this.dateAdded = Date()
    }

    @Ignore
    constructor(text: String){
        this.text = text
        this.dateAdded = Date()
    }
}