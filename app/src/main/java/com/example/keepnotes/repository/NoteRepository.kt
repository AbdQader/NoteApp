package com.example.keepnotes.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.keepnotes.db.NoteDao
import com.example.keepnotes.db.NoteDatabase
import com.example.keepnotes.model.Deleted
import com.example.keepnotes.model.Note

class NoteRepository(context: Context) {

    private var db: NoteDatabase = NoteDatabase.getInstance(context)
    private var dao: NoteDao = db.noteDao()
    private var getAllNotes = dao.getAllNotes()
    private var getAllDeleted = dao.getAllDeleted()

    // insert
    fun insert(note: Note)
    {
        Thread {
            dao.insert(note)
        }.start()
    }

    // update
    fun update(note: Note)
    {
        Thread {
            dao.update(note)
        }.start()
    }

    // delete
    fun delete(note: Note)
    {
        Thread {
            dao.delete(note)
        }.start()
    }

    // get all notes
    fun getAllNotes(): LiveData<List<Note>>
    {
        return getAllNotes
    }

    // delete all notes
    fun deleteAllNotes()
    {
        Thread {
            dao.deleteAllNotes()
        }.start()
    }

    // insert the note to the deleted table
    fun insertDeleted(deleted: Deleted)
    {
        Thread {
            dao.insertDeleted(deleted)
        }.start()
    }

    // delete the note from the deleted table
    fun deleteDeleted(deleted: Deleted)
    {
        Thread {
            dao.deleteDeleted(deleted)
        }.start()
    }

    // get all notes from the deleted table
    fun getAllDeleted(): LiveData<List<Deleted>>
    {
        return getAllDeleted
    }

    // delete all the note from the deleted table
    fun deleteAllDeleted()
    {
        Thread {
            dao.deleteAllDeleted()
        }.start()
    }

}