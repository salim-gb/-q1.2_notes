package com.example.notes.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.notes.R
import com.example.notes.domain.Note
import com.example.notes.domain.NotesRepositoryImpl
import com.example.notes.ui.details.OnNoteClicked

class NotesListFragment : Fragment(R.layout.fragment_notes_list), NotesListView {

    companion object {
        const val KEY_SELECTED_NOTE = "KEY_SELECTED_NOTE"
        const val ARG_NOTE = "ARG_NOTE"
    }

    private lateinit var container: LinearLayout
    private lateinit var presenter: NotesListPresenter
    private var onNoteClicked: OnNoteClicked? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = NotesListPresenter(this, NotesRepositoryImpl())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = view.findViewById(R.id.root)
        presenter.requestNotes()
    }

    override fun showNotes(notes: List<Note>) {
        for (note in notes) {
            val noteItem = LayoutInflater.from(context).inflate(
                R.layout.item_note,
                container,
                false
            )

            noteItem.setOnClickListener {
                onNoteClicked?.onNoteOnClicked(note)

                broadcastNote(note)
            }

            val noteName = noteItem.findViewById<TextView>(R.id.note_title)
            val noteDescription = noteItem.findViewById<TextView>(R.id.note_description)
            val noteCreationDate = noteItem.findViewById<TextView>(R.id.note_creation_date)
            noteName.text = note.title
            noteDescription.text = note.description
            noteCreationDate.text = note.creationDate

            container.addView(noteItem)
        }
    }

    private fun broadcastNote(note: Note) {
        val bundle = Bundle()
        bundle.putParcelable(ARG_NOTE, note)
        parentFragmentManager.setFragmentResult(KEY_SELECTED_NOTE, bundle)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnNoteClicked) {
            onNoteClicked = context
        }
    }

    override fun onDetach() {
        onNoteClicked = null
        super.onDetach()
    }
}