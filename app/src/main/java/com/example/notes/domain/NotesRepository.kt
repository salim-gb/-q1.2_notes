package com.example.notes.domain

interface NotesRepository {

    fun getNotes(): List<Note>
    fun addNote()
    fun deleteNote()
    fun changeNote()
}