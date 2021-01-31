package com.example.keepnotes.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.keepnotes.model.Deleted
import com.example.keepnotes.model.Note

@Dao
interface NoteDao {

    //** For Note Table
    @Insert // to insert new note
    fun insert(note: Note)

    @Update // to update the note
    fun update(note: Note)

    @Delete // to delete the note
    fun delete(note: Note)

    // to get all notes
    @Query("SELECT * FROM note")
    fun getAllNotes(): LiveData<List<Note>>

    // to delete all notes
    @Query("DELETE FROM note")
    fun deleteAllNotes()

    //** For Deleted Table
    @Insert
    fun insertDeleted(deleted: Deleted)

    @Delete
    fun deleteDeleted(deleted: Deleted)

    // to get all notes
    @Query("SELECT * FROM deleted_note")
    fun getAllDeleted(): LiveData<List<Deleted>>

    @Query("DELETE FROM deleted_note")
    fun deleteAllDeleted()

}