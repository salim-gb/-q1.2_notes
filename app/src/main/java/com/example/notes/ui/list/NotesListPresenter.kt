package com.example.notes.ui.list

import com.example.notes.domain.NotesRepository

class NotesListPresenter(
    private val view: NotesListView,
    private val repository: NotesRepository
) {
    fun requestNotes() {
        val result = repository.getNotes()
        view.showNotes(result)
    }
}