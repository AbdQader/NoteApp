package com.example.keepnotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_note")
class Deleted(
    @ColumnInfo(name = "note") var note: Note?
) {
    @PrimaryKey(autoGenerate = true) var noteNum: Int = 0
}