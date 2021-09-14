package com.example.notes.noteDetail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.noteList.NotesListViewModelFactory
import com.example.notes.noteList.NotesViewModel
import com.example.notes.noteUpdate.NoteUpdateFragment

class NoteDetailFragment : Fragment(R.layout.fragment_note_details) {

    private val viewModel by viewModels<NotesViewModel> {
        NotesListViewModelFactory()
    }

    private lateinit var noteTitle: TextView
    private lateinit var noteDescription: TextView
    private lateinit var noteDate: TextView
    private lateinit var noteImage: ImageView
    private var currentNote: Note? = null

    companion object {
        private const val ARG_NOTE = "ARG_NOTE"
        private const val KEY_CURRENT_NOTE = "KEY_CURRENT_NOTE"
        private const val ARG_DATE = "ARG_DATE"

        fun newInstance(note: Note?) =
            NoteDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_NOTE, note)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.note_details_toolbar)

        noteDate = view.findViewById(R.id.date)
        noteTitle = view.findViewById(R.id.title)
        noteImage = view.findViewById(R.id.image)
        noteDescription = view.findViewById(R.id.description)

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.change -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, NoteUpdateFragment.newInstance(currentNote))
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        noteDate.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(parentFragmentManager, "datePicker")
        }

        parentFragmentManager.setFragmentResultListener(
            KEY_CURRENT_NOTE,
            viewLifecycleOwner, { _, result ->
                val title = result.get(NoteUpdateFragment.NOTE_TITLE)
                val description = result.get(NoteUpdateFragment.NOTE_DES)
                viewModel.updateNote(title.toString(), description.toString())
            }
        )

        if (arguments?.containsKey(ARG_NOTE) == true) {
            arguments?.getParcelable<Note>(ARG_NOTE).also {
                currentNote = it
                displayNote(it)
            }
        }
    }

    private fun displayNote(note: Note?) {
        if (note != null) {
            noteTitle.text = note.title
            noteDescription.text = note.description
            noteDate.text = note.creationDate.toString()
            Glide.with(requireContext()).load(note.imageUrl).centerCrop().into(noteImage)
        }
    }
}