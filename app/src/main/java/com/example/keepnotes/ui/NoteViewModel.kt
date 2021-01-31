package com.example.keepnotes.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.keepnotes.model.Deleted
import com.example.keepnotes.model.Note
import com.example.keepnotes.repository.NoteRepository

class NoteViewModel(application: Application): AndroidViewModel(application) {

    private var repository: NoteRepository = NoteRepository(application)

    // insert
    fun insert(note: Note)
    {
        repository.insert(note)
    }

    // update
    fun update(note: Note)
    {
        repository.update(note)
    }

    // delete
    fun delete(note: Note)
    {
        repository.delete(note)
    }

    // get all notes
    fun getAllNotes(): LiveData<List<Note>>
    {
        return repository.getAllNotes()
    }

    // delete all notes
    fun deleteAllNotes()
    {
        repository.deleteAllNotes()
    }

    //** Deleted Table
    fun insertDeleted(deleted: Deleted)
    {
        repository.insertDeleted(deleted)
    }

    fun deleteDeleted(deleted: Deleted)
    {
        repository.deleteDeleted(deleted)
    }

    fun getAllDeleted(): LiveData<List<Deleted>>
    {
        return repository.getAllDeleted()
    }

    fun deleteAllDeleted()
    {
        repository.deleteAllDeleted()
    }

}