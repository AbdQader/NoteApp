package com.example.keepnotes.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.keepnotes.R
import com.example.keepnotes.model.Deleted
import kotlinx.android.synthetic.main.note_item.view.*

class DeletedNoteAdapter(
    private var context: Context,
    private var data: ArrayList<Deleted>,
    private val click: OnItemClickListener
) : RecyclerView.Adapter<DeletedNoteAdapter.NoteViewHolder>() {

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

        holder.title.text = data[position].note!!.noteTitle
        holder.content.text = data[position].note!!.noteContent
        holder.date.text = data[position].note!!.noteDate

        holder.card.setBackgroundColor(Color.parseColor(data[position].note!!.noteColor))
        holder.title.setBackgroundColor(Color.parseColor(data[position].note!!.noteColor))
        holder.content.setBackgroundColor(Color.parseColor(data[position].note!!.noteColor))
        holder.date.setBackgroundColor(Color.parseColor(data[position].note!!.noteColor))

        if (data[position].note!!.noteImage!!.isNotEmpty())
        {
            holder.image.setImageURI(Uri.parse(data[position].note!!.noteImage))
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