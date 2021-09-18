package com.example.notes.noteUpdate

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.noteList.NotesListViewModelFactory
import com.example.notes.noteList.NotesViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

private const val ARG_NOTE = "ARG_NOTE"
private const val KEY_CURRENT_NOTE = "KEY_CURRENT_NOTE"
private const val NOTE_POSITION = "POSITION"

class NoteUpdateFragment : Fragment(R.layout.fragment_note_update) {

    private var currentNote: Note? = null
    private var currentPosition: Int? = null
    private lateinit var titleET: TextInputEditText
    private lateinit var descriptionET: TextInputEditText
    private lateinit var dateTV: TextView

    private val viewModel by viewModels<NotesViewModel> {
        NotesListViewModelFactory()
    }

    companion object {
        fun newInstance(note: Note, position: Int) =
            NoteUpdateFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_NOTE, note)
                    putInt(NOTE_POSITION, position)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentNote = it.getParcelable(ARG_NOTE)
            currentPosition = it.getInt(NOTE_POSITION)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.note_update_toolbar)

        titleET = view.findViewById(R.id.update_note_title)
        descriptionET = view.findViewById(R.id.update_note_description)
        dateTV = view.findViewById(R.id.update_note_date)

        titleET.setText(currentNote?.title).toString()
        descriptionET.setText(currentNote?.description).toString()
        dateTV.text = currentNote?.creationDate.toString()

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.update_button).setOnClickListener {
            updateNote()
        }
    }

    private fun updateNote() {
        if (titleET.text.isNullOrEmpty() || descriptionET.text.isNullOrEmpty()) {
            return
        } else {
            val note = Note(
                currentNote?.id ?: "",
                titleET.text.toString(),
                descriptionET.text.toString(),
                currentNote?.creationDate ?: Date(),
                currentNote?.imageUrl ?: ""
            )
            viewModel.updateNote(note, currentPosition)

            parentFragmentManager.setFragmentResult(KEY_CURRENT_NOTE, bundleOf(ARG_NOTE to note))
        }
        parentFragmentManager.popBackStack()
    }
}