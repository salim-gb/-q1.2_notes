package com.example.notes.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.domain.Note
import com.example.notes.ui.details.NoteDetailsActivity
import com.example.notes.ui.details.OnNoteClicked
import com.example.notes.ui.list.NotesListFragment

class MainActivity : AppCompatActivity(), OnNoteClicked {

    companion object {
        private const val ARG_NOTE = "ARG_NOTE"
    }

    private var selectedNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onNoteOnClicked(note: Note) {

        selectedNote = note

        if (!resources.getBoolean(R.bool.isLandScape)) {
            Intent(this, NoteDetailsActivity::class.java).also {
                it.putExtra(NoteDetailsActivity.ARG_NOTE, note)
                startActivity(it)

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARG_NOTE, selectedNote)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        selectedNote = savedInstanceState.getParcelable(ARG_NOTE)

        if (resources.getBoolean(R.bool.isLandScape)) {
            val bundle = Bundle()
            bundle.putParcelable(ARG_NOTE, selectedNote)
            supportFragmentManager.setFragmentResult(NotesListFragment.KEY_SELECTED_NOTE, bundle)
        }
    }
}