package com.example.keepnotes.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.keepnotes.R
import com.example.keepnotes.model.Note
import kotlinx.android.synthetic.main.note_item.view.*
import kotlin.collections.ArrayList

class NoteAdapter(
    private var context: Context,
    private var data: ArrayList<Note>,
    private val click: OnItemClickListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card = view.noteCard!!
        var title = view.noteTitle!!
        var content = view.noteContent!!
        var date = view.noteDate!!
        var image = view.noteImage!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.title.text = data[position].noteTitle
        holder.content.text = data[position].noteContent
        holder.date.text = data[position].noteDate

        holder.card.setBackgroundColor(Color.parseColor(data[position].noteColor))
        holder.title.setBackgroundColor(Color.parseColor(data[position].noteColor))
        holder.content.setBackgroundColor(Color.parseColor(data[position].noteColor))
        holder.date.setBackgroundColor(Color.parseColor(data[position].noteColor))

        if (data[position].noteImage!!.isNotEmpty())
        {
            holder.image.setImageURI(Uri.parse(data[position].noteImage))
            holder.image.visibility = View.VISIBLE
        } else {
            holder.image.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            click.onItemClick(position)
        }

        holder.itemView.setOnLongClickListener {
            click.onItemLongClick(position)
            true
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

}