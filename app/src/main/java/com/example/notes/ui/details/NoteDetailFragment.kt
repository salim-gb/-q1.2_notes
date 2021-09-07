package com.example.notes.ui.details

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.notes.R
import com.example.notes.domain.Note
import com.example.notes.domain.NotesRepositoryImpl
import com.example.notes.ui.list.NotesListFragment
import com.example.notes.ui.list.NotesListPresenter
import com.example.notes.ui.list.NotesListView
import java.util.*


class NoteDetailFragment : Fragment(R.layout.fragment_note_details), NotesListView {

    private lateinit var presenter: NotesListPresenter
    private lateinit var noteTitle: TextView
    private lateinit var noteDescription: TextView
    private lateinit var noteDate: TextView

    companion object {
        private const val ARG_NOTE = "ARG_NOTE"
        private const val ARG_DATE = "ARG_DATE"

        @JvmStatic
        fun newInstance(note: Note?) =
            NoteDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_NOTE, note)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = NotesListPresenter(this, NotesRepositoryImpl())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteTitle = view.findViewById(R.id.title)
        noteDescription = view.findViewById(R.id.description)
        noteDate = view.findViewById(R.id.date)

        noteDate.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(parentFragmentManager, "datePicker")
        }

        presenter.requestNotes()

        parentFragmentManager.setFragmentResultListener(
            DatePickerFragment.ARG_DATE_SELECTED,
            viewLifecycleOwner, {_, result ->
                result.getString(ARG_DATE).also {
                    noteDate.text = it
                }
            }
        )

        parentFragmentManager.setFragmentResultListener(
            NotesListFragment.KEY_SELECTED_NOTE,
            viewLifecycleOwner, { _, result ->
                result.getParcelable<Note>(ARG_NOTE).also {
                    displayNote(it)
                }
            }
        )

        if (arguments?.containsKey(ARG_NOTE) == true) {
            arguments?.getParcelable<Note>(ARG_NOTE).also {
                displayNote(it)
            }
        }
    }

    private fun displayNote(note: Note?) {
        if (note != null) {
            noteTitle.text = note.title
            noteDescription.text = note.description
            noteDate.text = note.creationDate
        }
    }

    override fun showNotes(notes: List<Note>) {
        displayNote(notes[0])
    }
}