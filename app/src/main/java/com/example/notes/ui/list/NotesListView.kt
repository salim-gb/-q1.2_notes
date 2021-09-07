package com.example.notes.ui.list

import com.example.notes.domain.Note

interface NotesListView {

    fun showNotes(notes: List<Note>)
}