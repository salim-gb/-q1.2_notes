package com.example.notes.noteUpdate

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.notes.R
import com.example.notes.data.Note
import com.google.android.material.textfield.TextInputEditText

class NoteUpdateFragment : Fragment(R.layout.fragment_note_update) {
    private lateinit var updateNoteTitle: TextInputEditText
    private lateinit var updateNoteDescription: TextInputEditText
    private lateinit var updateNoteDate: TextView

    companion object {
        private const val ARG_NOTE = "ARG_NOTE"
        const val KEY_CURRENT_NOTE = "KEY_CURRENT_NOTE"
        const val NOTE_TITLE = "NOTE_TITLE"
        const val NOTE_DES = "NOTE_DES"
    }

    fun newInstance(note: Note?) =
        NoteUpdateFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_NOTE, note)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.note_update_toolbar)
        updateNoteTitle = view.findViewById(R.id.update_note_title)
        updateNoteDescription = view.findViewById(R.id.update_note_description)
        updateNoteDate = view.findViewById(R.id.update_note_date)

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.update_button).setOnClickListener {
            updateNote()
        }

        if (arguments?.containsKey(ARG_NOTE) == true) {
            arguments?.getParcelable<Note>(ARG_NOTE).apply {
                updateNoteTitle.setText(this?.title)
                updateNoteDescription.setText(this?.description)
                updateNoteDate.text = this?.creationDate.toString()
            }
        }
    }

    private fun updateNote() {
        if (updateNoteTitle.text.isNullOrEmpty() || updateNoteDescription.text.isNullOrEmpty()) {
            return
        } else {
            val title = updateNoteTitle.text.toString()
            val description = updateNoteDescription.text.toString()
            val bundle = Bundle()
            bundle.putString(NOTE_TITLE, title)
            bundle.putString(NOTE_DES, description)
            childFragmentManager.setFragmentResult(KEY_CURRENT_NOTE, bundle)
        }
        parentFragmentManager.popBackStack()
    }
}