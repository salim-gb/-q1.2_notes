package com.example.notes.noteInsert

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.notes.R
import com.example.notes.data.Note
import java.util.*

class NoteAddFragment : Fragment(R.layout.fragment_add_note) {

    companion object {
        const val ARG_NOTE = "ARG_NOTE"
        const val KEY_NOTE_ADDED = "KEY_NOTE_ADDED"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.note_add_toolbar)

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val noteTitle = view.findViewById<EditText>(R.id.add_note_title)
        val noteDescription = view.findViewById<EditText>(R.id.add_note_description)
        view.findViewById<Button>(R.id.add_button).setOnClickListener {

            val title = noteTitle.text.toString()
            val description = noteDescription.text.toString()

            val note = Note("", title, description, Date(), "")

            parentFragmentManager.setFragmentResult(KEY_NOTE_ADDED, bundleOf(ARG_NOTE to note))
            parentFragmentManager.popBackStack()
        }
    }
}