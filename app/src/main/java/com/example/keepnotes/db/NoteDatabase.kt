package com.example.keepnotes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.keepnotes.model.Deleted
import com.example.keepnotes.model.Note

@Database(entities = [Note::class, Deleted::class], version = 14)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object
    {
        private var instance: NoteDatabase? = null

        // instance from NoteDatabase
        fun getInstance(context: Context): NoteDatabase
        {
            if (instance == null)
            {
                instance =
                    Room.databaseBuilder(context, NoteDatabase::class.java, "note-database")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }
    }

}