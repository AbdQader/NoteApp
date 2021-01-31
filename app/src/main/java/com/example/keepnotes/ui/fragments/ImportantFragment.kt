package com.example.keepnotes.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepnotes.ui.MainActivity
import com.example.keepnotes.R
import com.example.keepnotes.adapter.NoteAdapter
import com.example.keepnotes.ui.NoteViewModel
import com.example.keepnotes.model.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_important.view.*

class ImportantFragment : Fragment(), NoteAdapter.OnItemClickListener {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var data: ArrayList<Note>
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_important, container, false)

        // to change status bar and tool bar color
        requireActivity().window.statusBarColor = Color.WHITE
        requireActivity().toolbar.setBackgroundColor(Color.WHITE)

        setHasOptionsMenu(true) // for menu

        // initialize the "noteViewModel"
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // initialize the array and the adapter
        data = ArrayList()
        adapter = NoteAdapter(requireActivity(), data, this)

        // give the adapter to the recyclerView
        root.rv_important_notes.adapter = adapter
        // give the recyclerView LinearLayout as a layoutManager
        root.rv_important_notes.layoutManager = LinearLayoutManager(requireContext())

        // get all important notes from "noteViewModel"
        noteViewModel.getAllNotes().observe(viewLifecycleOwner, Observer { notes ->
            // to check if the notes is empty or not
            if (notes.isNotEmpty()) {
                data.clear() // to clear the data array
                notes.forEach { note ->
                    if (note.noteImportance!!)
                        data.add(0, note)
                }
            }
            adapter.notifyDataSetChanged() // notify the adapter
        })

        return root
    } // end of onCreateView method

    // when user click on any note in "ImportantFragment"
    override fun onItemClick(position: Int) {
        val note = data[position]
        //val action = ImportantFragmentDirections.actionImportantToAddNote(note)
        val bundle = bundleOf(MainActivity.NOTE to note)
        findNavController().navigate(R.id.action_important_to_addNote, bundle)
    }

    override fun onItemLongClick(position: Int) {}

}