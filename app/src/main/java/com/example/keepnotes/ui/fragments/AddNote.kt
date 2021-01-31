package com.example.keepnotes.ui.fragments

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.keepnotes.ui.MainActivity
import com.example.keepnotes.R
import com.example.keepnotes.ui.NoteViewModel
import com.example.keepnotes.model.Deleted
import com.example.keepnotes.model.Note
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_note.view.*
import kotlinx.android.synthetic.main.note_bottom_sheet.*
import kotlinx.android.synthetic.main.note_bottom_sheet.view.*
import java.util.*

class AddNote : Fragment() {

    private lateinit var viewModel: NoteViewModel
    private lateinit var note: Note
    private lateinit var root: View
    private var isEditMode: Boolean = true
    private var noteImportant: Boolean = false
    private var noteImage: String = ""
    private var noteColor: String = "#FFFFFF"
    private lateinit var menu: Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.add_note, container, false)

        setHasOptionsMenu(true) // for menu

        // initialize the "noteViewModel"
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // to get data from bundle
        val bundle = arguments
        when {
            // when user click on any note in "NotesFragment"
            bundle!!.get(MainActivity.NOTE) != null -> {
                requireActivity().title = "Edit Note" // edit the toolbar title
                isEditMode = true // is edit mode

                note = bundle.getParcelable(MainActivity.NOTE)!! // to get the note from bundle
                root.txtNoteTitle.setText(note.noteTitle)        // edit the txtNoteTitle text
                root.txtNoteContent.setText(note.noteContent)    // edit the txtNoteContent text
                noteImage = note.noteImage!! // give the image to "noteImage" variable
                noteColor = note.noteColor!! // give the color to "noteColor" variable
                noteImportant = note.noteImportance!!
                if (note.noteImage!!.isNotEmpty())
                {
                    root.imgNote.setImageURI(Uri.parse(note.noteImage))
                    root.imgNote.visibility = View.VISIBLE
                } else {
                    root.imgNote.visibility = View.GONE
                }
                // call "viewsColor" function
                viewsColor(note.noteColor!!)
            }
            // when user click on image fab
            bundle.get(MainActivity.NOTE_IMAGE) != null -> {
                val image = bundle.get(MainActivity.NOTE_IMAGE) as Uri
                root.imgNote.visibility = View.VISIBLE
                root.imgNote.setImageURI(image)
                noteImage = image.toString()

                requireActivity().title = "Add Note" // edit the toolbar title
                isEditMode = false // is not edit mode
            }
            else -> {
                requireActivity().title = "Add Note" // edit the toolbar title
                isEditMode = false // is not edit mode
            }
        }

        // call "initBottomSheet" function
        initBottomSheet()

        return root
    } // end of onCreateView method

    // for menu "add note"
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu // to initialize the menu
        inflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.search).isVisible = false      // hide the search icon
        menu.findItem(R.id.done).isVisible = true         // show the done icon
        menu.findItem(R.id.delete).isVisible = isEditMode // show or hide the delete icon
        menu.findItem(R.id.clear).isVisible = false       // hide the clear icon
        if (isEditMode)
        {
            if (note.noteImportance!!)
            {
                menu.findItem(R.id.important).isVisible = true
                menu.findItem(R.id.notImportant).isVisible = false
            } else {
                menu.findItem(R.id.important).isVisible = false
                menu.findItem(R.id.notImportant).isVisible = true
            }
        } else {
            menu.findItem(R.id.important).isVisible = false
            menu.findItem(R.id.notImportant).isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.done ->
            {
                addNoteToDB()
                findNavController().navigate(R.id.action_addNote_to_notes)
            }
            R.id.important -> {
                noteImportant = false
                menu.findItem(R.id.important).isVisible = false
                menu.findItem(R.id.notImportant).isVisible = true
            }
            R.id.notImportant ->
            {
                noteImportant = true
                menu.findItem(R.id.important).isVisible = true
                menu.findItem(R.id.notImportant).isVisible = false
            }
            R.id.delete ->
            {
                addNoteToDeleted()
                deleteNoteFromDB()
                findNavController().navigate(R.id.action_addNote_to_notes)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // this function to add the note in the database
    private fun addNoteToDB() {
        var title = root.txtNoteTitle.text.toString().trim()
        var content = root.txtNoteContent.text.toString().trim()

        if (title.isEmpty() && content.isEmpty())
        {
            Toast.makeText(requireActivity(), "Note Ignored", Toast.LENGTH_SHORT).show()
        } else {
            if (title.isEmpty())
                title = "Untitled"

            if (content.isEmpty())
                content = "Without Content"

            // date format =>"dd MMM yyyy 'AT' hh:mm a"
            val date = DateFormat.format("dd/MM/yyyy", Calendar.getInstance().time).toString()
            val newNote = Note(title, content, date, noteImportant, noteImage, noteColor)
            if (isEditMode)
            {
                newNote.noteId = note.noteId // to get the old id for the note
                viewModel.update(newNote) // edit the note
            } else {
                viewModel.insert(newNote) // add the note
            }
        }

    } // end of addNoteToDB method

    // this fun to delete the note from database/from note table
    private fun deleteNoteFromDB() {
        viewModel.delete(note)
    }

    // this function to add the note in the deleted table
    private fun addNoteToDeleted() {
        viewModel.insertDeleted(Deleted(note))
    }

    //=================================

    // this function to initialize the bottom sheet
    private fun initBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(root.note_bottom_sheet)
        root.note_bottom_sheet.txtBottomSheetTitle.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
            {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        root.note_bottom_sheet.whitNote.setOnClickListener {
            noteColor = "#FFFFFF"
            noteSelectedColor(R.drawable.ic_done, 0, 0, 0, 0, 0)
            viewsColor(noteColor)
        }
        root.note_bottom_sheet.blueNote.setOnClickListener {
            noteColor = "#2196F3"
            noteSelectedColor(0, R.drawable.ic_done, 0, 0, 0, 0)
            viewsColor(noteColor)
        }
        root.note_bottom_sheet.purpleNote.setOnClickListener {
            noteColor = "#9C27B0"
            noteSelectedColor(0, 0, R.drawable.ic_done, 0, 0, 0)
            viewsColor(noteColor)
        }
        root.note_bottom_sheet.greenNote.setOnClickListener {
            noteColor = "#0AEBAF"
            noteSelectedColor(0, 0, 0, R.drawable.ic_done, 0, 0)
            viewsColor(noteColor)
        }
        root.note_bottom_sheet.yellowNote.setOnClickListener {
            noteColor = "#FFEB3B"
            noteSelectedColor(0, 0, 0, 0, R.drawable.ic_done, 0)
            viewsColor(noteColor)
        }
        root.note_bottom_sheet.orangeNote.setOnClickListener {
            noteColor = "#F44336"
            noteSelectedColor(0, 0, 0, 0, 0, R.drawable.ic_done)
            viewsColor(noteColor)
        }
    }

    // this function to give the "done" icon to the selected color
    private fun noteSelectedColor(whit: Int, blue: Int, purple: Int, green: Int, yellow: Int, orange: Int) {
        whitNote.setImageResource(whit)
        blueNote.setImageResource(blue)
        purpleNote.setImageResource(purple)
        greenNote.setImageResource(green)
        yellowNote.setImageResource(yellow)
        orangeNote.setImageResource(orange)
    }

    // this function to give the views "elements" the selected color
    private fun viewsColor(color: String) {
        root.add_note.setBackgroundColor(Color.parseColor(color))
        root.txtNoteTitle.setBackgroundColor(Color.parseColor(color))
        root.txtNoteContent.setBackgroundColor(Color.parseColor(color))
        requireActivity().toolbar.setBackgroundColor(Color.parseColor(color))
        requireActivity().window.statusBarColor = Color.parseColor(color)
    }

}