package com.kannadavoicenotes.app.kannadavoicenotes.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kannadavoicenotes.app.kannadavoicenotes.utils.DateConverter

/**
 * Created by varun.am on 06/12/18
 */
@Database(entities = [ConvertedText::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ConvertedTextDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "convertedText.db"
        private var INSTANCE: ConvertedTextDatabase? = null

        fun getInstance(context: Context): ConvertedTextDatabase? {
            if (INSTANCE == null) {
                synchronized(ConvertedTextDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ConvertedTextDatabase::class.java, DATABASE_NAME
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun convertedTextDao(): ConvertedTextDao
}