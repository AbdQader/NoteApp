package com.example.keepnotes.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.adapter.DeletedNoteAdapter
import com.example.keepnotes.ui.NoteViewModel
import com.example.keepnotes.model.Deleted
import kotlinx.android.synthetic.main.fragment_deleted.view.*

class DeletedFragment : Fragment(), DeletedNoteAdapter.OnItemClickListener {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: DeletedNoteAdapter
    private lateinit var data: ArrayList<Deleted>
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_deleted, container, false)

        setHasOptionsMenu(true) // for menu

        // initialize the "noteViewModel"
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // initialize the array and the adapter
        data = ArrayList()
        adapter = DeletedNoteAdapter(requireActivity(), data, this)

        // give the adapter to the recyclerView
        root.rv_deleted_notes.adapter = adapter
        // give the recyclerView LinearLayout as a layoutManager
        root.rv_deleted_notes.layoutManager = LinearLayoutManager(requireContext())

        // get all deleted notes from "noteViewModel"
        noteViewModel.getAllDeleted().observe(viewLifecycleOwner, Observer { notes ->
            // to check if the notes is empty or not
            if (notes.isNotEmpty()) {
                data.clear() // to clear the data array
                notes.forEach { note ->
                    data.add(0, note)
                }
            }
            adapter.notifyDataSetChanged() // notify the adapter
        })

        return root
    } // end of onCreateView method

    // for menu "clear"
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.clear).isVisible = true         // show the clear icon
        menu.findItem(R.id.search).isVisible = false       // hide the search icon
        menu.findItem(R.id.done).isVisible = false         // hide the done icon
        menu.findItem(R.id.important).isVisible = false    // hide the important icon
        menu.findItem(R.id.notImportant).isVisible = false // hide the notImportant icon
        menu.findItem(R.id.delete).isVisible = false       // hide the delete icon
        super.onCreateOptionsMenu(menu, inflater)
    }

    // when user click on empty recycle bin icon
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear)
        {
            // this dialog asks the user if he wants to empty the recycle bin or not
            alertDialog(
                "Empty Recycle Bin",
                "are you sure you want to empty recycle bin ?!",
                R.drawable.ic_clear
            ) {
                noteViewModel.deleteAllDeleted() // delete all deleted notes
                data.clear()                     // clear the array "data"
                adapter.notifyDataSetChanged()   // notify the adapter
                Toast.makeText(activity, "Recycle Bin Emptied", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // when the user click on any note
    override fun onItemClick(position: Int) {
        // this dialog asks the user if he wants to restore the note or not
        alertDialog(
            "Restore Note",
            "Do you want to restore this note ?!",
            R.drawable.ic_restore
        ) {
            noteViewModel.insert(data[position].note!!) // add the note to notes table
            noteViewModel.deleteDeleted(data[position]) // remove the note from deleted table
            adapter.notifyDataSetChanged()
            Toast.makeText(activity, "Note Restored", Toast.LENGTH_SHORT).show()
        }
    }

    // when the user long click on any note
    override fun onItemLongClick(position: Int) {
        // this dialog asks the user if he wants to delete the note or not
        alertDialog (
            "Delete Note",
            "Do you want to delete this note forever ?!",
            R.drawable.ic_delete_forever
        ) {
            noteViewModel.deleteDeleted(data[position])
            data.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(activity, "Note Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    // building an alert dialog
    private fun alertDialog (title: String, message: String, icon: Int, mFun: () -> Unit) {
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setCancelable(false)
        alertDialog.setIcon(icon)

        // execute the function here
        alertDialog.setPositiveButton("Yes") { _, _ ->
            mFun()
        }
        // to hide the dialog
        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        // show and create the alertDialog
        alertDialog.create().show()
    }

}