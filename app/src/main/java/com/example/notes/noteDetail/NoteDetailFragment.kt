package com.example.notes.noteDetail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.noteList.NotesListViewModelFactory
import com.example.notes.noteList.NotesViewModel
import com.example.notes.ui.Router

private const val ARG_NOTE = "ARG_NOTE"
private const val KEY_CURRENT_NOTE = "KEY_CURRENT_NOTE"
private const val NOTE_POSITION = "NOTE_POSITION"

class NoteDetailFragment : Fragment(R.layout.fragment_note_details) {

    private val viewModel by viewModels<NotesViewModel> {
        NotesListViewModelFactory()
    }

    private var currentNote: Note? = null
    private var currentPosition: Int? = null

    private lateinit var router: Router

    private lateinit var titleTV: TextView
    private lateinit var descriptionTV: TextView
    private lateinit var dateTV: TextView
    private lateinit var image: ImageView

    companion object {
        fun newInstance(note: Note, position: Int) =
            NoteDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_NOTE, note)
                    putInt(NOTE_POSITION, position)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router = Router(parentFragmentManager)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.note_details_toolbar)

        dateTV = view.findViewById(R.id.date)
        titleTV = view.findViewById(R.id.title)
        image = view.findViewById(R.id.image)
        descriptionTV = view.findViewById(R.id.description)

        arguments?.let {
            it.getParcelable<Note>(ARG_NOTE).let { note ->
                currentNote = note
                displayNote(note)
            }
            currentPosition = it.getInt(NOTE_POSITION)
        }

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.change -> {
                    if (currentNote != null && currentPosition != null) {
                        router.showNoteUpdate(currentNote!!, currentPosition!!)
                    }
                    true
                }
                R.id.delete -> {
                    viewModel.removeNote(currentNote)
                    parentFragmentManager.popBackStack()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        setFragmentResultListener(KEY_CURRENT_NOTE) { _, bundle ->
            bundle.getParcelable<Note>(ARG_NOTE).let {
                currentNote = it
                displayNote(it)
            }
        }

        dateTV.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(parentFragmentManager, "datePicker")
        }
    }

    private fun displayNote(note: Note?) {
        if (note != null) {
            titleTV.text = note.title
            descriptionTV.text = note.description
            dateTV.text = note.creationDate.toString()
            Glide.with(requireContext()).load(note.imageUrl).centerCrop().into(image)
        }
    }
}