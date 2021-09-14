package com.example.notes.noteList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notes.R
import com.example.notes.data.Note

class NotesAdapter(private val onClick: (Note) -> Unit) :
    ListAdapter<Note, NotesAdapter.NotesViewHolder>(NoteDiffCallback) {

    class NotesViewHolder(itemView: View, val onClick: (Note) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val noteTitle: TextView = itemView.findViewById(R.id.note_title)
        private val noteDescription: TextView = itemView.findViewById(R.id.note_description)
        private val noteCreationDate: TextView = itemView.findViewById(R.id.note_creation_date)
        private val noteImage: ImageView = itemView.findViewById(R.id.note_image)
        private var currentNote: Note? = null

        init {
            itemView.setOnClickListener {
                currentNote?.let {
                    onClick(it)
                }
            }
        }

        fun bind(note: Note) {
            currentNote = note

            noteTitle.text = note.title
            noteDescription.text = note.description
            noteCreationDate.text = note.creationDate.toString()
            Glide.with(itemView).load(note.imageUrl).centerCrop().into(noteImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note_list, parent, false)
        return NotesViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }
}

object NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }
}