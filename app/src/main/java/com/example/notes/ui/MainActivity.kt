package com.example.notes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.domain.Note
import com.example.notes.ui.details.NoteDetailFragment
import com.example.notes.ui.details.OnNoteClicked
import com.example.notes.ui.list.NotesListFragment

class MainActivity : AppCompatActivity(), OnNoteClicked {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NotesListFragment())
                .addToBackStack("HomeFragment")
                .commit()
        }
    }

    override fun onNoteOnClicked(note: Note) {
        if (!resources.getBoolean(R.bool.isLandScape)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NoteDetailFragment.newInstance(note))
                .addToBackStack("ListFragment")
                .commit()
        }
    }
}