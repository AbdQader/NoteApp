package com.example.keepnotes.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
class Note(
    @ColumnInfo(name = "note_title") var noteTitle: String?,
    @ColumnInfo(name = "note_content") var noteContent: String?,
    @ColumnInfo(name = "note_date") var noteDate: String?,
    @ColumnInfo(name = "note_importance") var noteImportance: Boolean?,
    @ColumnInfo(name = "note_image") var noteImage: String?,
    @ColumnInfo(name = "note_color") var noteColor: String?
): Parcelable {
    @PrimaryKey(autoGenerate = true) var noteId: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString()
    ) {
        noteId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(noteTitle)
        parcel.writeString(noteContent)
        parcel.writeString(noteDate)
        parcel.writeValue(noteImportance)
        parcel.writeString(noteImage)
        parcel.writeString(noteColor)
        parcel.writeInt(noteId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}