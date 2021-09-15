package com.example.notes.ui

import androidx.fragment.app.FragmentManager
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.noteDetail.NoteDetailFragment
import com.example.notes.noteInsert.NoteAddFragment
import com.example.notes.noteList.NotesListFragment
import com.example.notes.noteUpdate.NoteUpdateFragment

class Router(private val fragmentManager: FragmentManager) {

    fun showNotesList() {
        fragmentManager.beginTransaction()
            .replace(R.id.container, NotesListFragment())
            .commit()
    }

    fun showNoteAdd() {
        fragmentManager.beginTransaction()
            .replace(R.id.container, NoteAddFragment())
            .addToBackStack(null)
            .commit()
    }

    fun showNoteDetail(note: Note) {
        fragmentManager.beginTransaction()
            .replace(R.id.container, NoteDetailFragment.newInstance(note))
            .addToBackStack(null)
            .commit()
    }

    fun showNoteUpdate(note: Note) {
        fragmentManager.beginTransaction()
            .replace(R.id.container, NoteUpdateFragment.newInstance(note))
            .addToBackStack(null)
            .commit()
    }

    fun showSettingsFragment() {
        fragmentManager.beginTransaction()
            .replace(R.id.container, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }
}