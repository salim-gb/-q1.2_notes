package com.example.notes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.noteDetail.NoteDetailFragment
import com.example.notes.noteList.NotesListFragment

class MainActivity : AppCompatActivity(), OnNoteClickedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NotesListFragment())
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