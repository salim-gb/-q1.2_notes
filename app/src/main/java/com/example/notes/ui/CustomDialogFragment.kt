package com.example.notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.noteList.NotesListViewModelFactory
import com.example.notes.noteList.NotesViewModel
import java.util.*

class CustomDialogFragment : DialogFragment() {
    private val viewModel by viewModels<NotesViewModel> {
        NotesListViewModelFactory()
    }

    private var currentNote: Note? = null
    private var currentPosition: Int? = null
    private lateinit var titleET: EditText
    private lateinit var descET: EditText

    companion object {
        const val ARG_NOTE = "ARG_NOTE"
        const val POSITION = "POSITION"

        fun newInstance(note: Note, position: Int) =
            CustomDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_NOTE, note)
                    putInt(POSITION, position)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            it.getParcelable<Note>(ARG_NOTE).let { note ->
                currentNote = note
            }
            currentPosition = it.getInt(POSITION)
        }

        titleET = view.findViewById(R.id.noteTitle)
        descET = view.findViewById(R.id.noteDescription)

        titleET.setText(currentNote?.title)
        descET.setText(currentNote?.description)

        view.findViewById<Button>(R.id.update_btn).setOnClickListener {
            updateNote()
        }
    }

    private fun updateNote() {
        if (titleET.text.isNullOrEmpty() || descET.text.isNullOrEmpty()) {
            return
        } else {
            val note = Note(
                currentNote?.id ?: "",
                titleET.text.toString(),
                descET.text.toString(),
                currentNote?.creationDate ?: Date(),
                currentNote?.imageUrl ?: ""
            )

            viewModel.updateNote(note, currentPosition)
            dismiss()
        }
    }
}