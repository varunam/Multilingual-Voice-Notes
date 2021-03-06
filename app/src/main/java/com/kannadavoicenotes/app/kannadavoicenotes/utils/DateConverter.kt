package com.kannadavoicenotes.app.kannadavoicenotes.utils

import androidx.room.TypeConverter
import java.util.*


/**
 * Created by varun.am on 06/12/18
 */
class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return (date?.time)!!.toLong()
    }
}