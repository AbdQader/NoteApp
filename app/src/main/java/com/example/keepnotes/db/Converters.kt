package com.example.keepnotes.db

import androidx.room.TypeConverter
import com.example.keepnotes.model.Note
import com.google.gson.Gson

class Converters {

    // this function to convert the note to string
    @TypeConverter
    fun fromNoteToString(note: Note): String
    {
        return Gson().toJson(note)
    }

    // this function to convert the string to note
    @TypeConverter
    fun fromStringToNote(stringNote: String): Note
    {
        return Gson().fromJson(stringNote, Note::class.java)
    }

}