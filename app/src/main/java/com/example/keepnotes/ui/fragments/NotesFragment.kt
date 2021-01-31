package com.example.keepnotes.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepnotes.ui.MainActivity
import com.example.keepnotes.R
import com.example.keepnotes.adapter.NoteAdapter
import com.example.keepnotes.ui.NoteViewModel
import com.example.keepnotes.model.Note
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.fragment_notes.view.*
import java.util.*
import kotlin.collections.ArrayList

class NotesFragment : Fragment(), NoteAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var data: ArrayList<Note>
    private lateinit var noteSearch: ArrayList<Note>
    private lateinit var root: View
    private val RC_SELECT_IMAGE = 7

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_notes, container, false)

        // to change status bar and tool bar color
        requireActivity().window.statusBarColor = Color.WHITE
        requireActivity().toolbar.setBackgroundColor(Color.WHITE)

        setHasOptionsMenu(true) // for menu

        // initialize the "noteViewModel"
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // initialize the array and the adapter
        data = ArrayList()
        noteSearch = ArrayList()
        adapter = NoteAdapter(requireActivity(), data, this)

        // give the adapter to the recyclerView
        root.rv_notes.adapter = adapter
        root.rv_notes.layoutManager = LinearLayoutManager(requireContext())

        // when user click on fab_not floating action button
        root.fab_note.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_notes_to_addNote)
        }

        // when user click on fab_image floating action button
        root.fab_image.setOnClickListener {
            requestPermission()
        }

        // to get all notes from "NoteViewModel"
        noteViewModel.getAllNotes().observe(viewLifecycleOwner, Observer { notes ->
            // to check if the notes is empty or not
            if (notes.isNotEmpty())
            {
                data.clear() // to clear the data array
                notes.forEach { note ->
                    data.add(0, note)
                }
            }
            adapter.notifyDataSetChanged() // notify the adapter
        })

        return root
    } // end of onCreateView method

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == RC_SELECT_IMAGE)
        {
            if (data != null && data.data != null)
            {
                val selectedImage = data.data // the image that the user selected it
                val bundle = bundleOf(MainActivity.NOTE_IMAGE to selectedImage)
                findNavController().navigate(R.id.action_notes_to_addNote, bundle)
            }
        }
    }

    // for menu "search"
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.search).isVisible = true        // show the search icon
        menu.findItem(R.id.done).isVisible = false         // hide the done icon
        menu.findItem(R.id.important).isVisible = false    // hide the important icon
        menu.findItem(R.id.notImportant).isVisible = false // hide the notImportant icon
        menu.findItem(R.id.delete).isVisible = false       // hide the delete icon
        menu.findItem(R.id.clear).isVisible = false        // hide the clear icon

        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.search)) as SearchView
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    // when user click on any note in "NotesFragment"
    override fun onItemClick(position: Int) {
        val note = data[position]
        //val action = NotesFragmentDirections.actionNotesToAddNote(note)
        val bundle = bundleOf(MainActivity.NOTE to note)
        findNavController().navigate(R.id.action_notes_to_addNote, bundle)
    }

    override fun onItemLongClick(position: Int) {}

    // to request the storage permission from the user
    private fun requestPermission() {
        Dexter
            .withContext(activity)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, RC_SELECT_IMAGE)
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    showDialog()
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    token!!.continuePermissionRequest()
                }
            })
            .check()
    }

    // to show the dialog for the user, if denied the permission
    private fun showDialog () {
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setTitle("Missed Permission")
        alertDialog.setMessage("we need this permission to make you choose image from gallery!")
        alertDialog.setCancelable(false)
        alertDialog.setIcon(R.drawable.ic_info)

        // to request permission again
        alertDialog.setPositiveButton("Show Permission") { _, _ ->
            requestPermission()
        }
        // to hide the dialog
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        // show and create the alertDialog
        alertDialog.create().show()
    }

    // for search
    override fun onQueryTextSubmit(query: String): Boolean {
        search(query)
        if (noteSearch.isEmpty())
        {
            Snackbar.make(root, "There are no notes found!", Snackbar.LENGTH_LONG).show()
        }
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        search(newText)
        if (noteSearch.isEmpty() && newText.isNotEmpty())
        {
            Snackbar.make(root, "There are no notes found!", Snackbar.LENGTH_LONG).show()
        }
        return false
    }

    // this function to filter search result
    private fun search(searchText: String) {
        noteSearch.clear()
        for (i in 0 until data.size)
        {
            val title = data[i].noteTitle!!
            val content = data[i].noteContent!!
            if (title.toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))
                || content.toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))
            ) {
                noteSearch.add(data[i])
            }
        }

        adapter = NoteAdapter(requireActivity(), noteSearch, this)
        rv_notes.adapter = adapter
    }

}