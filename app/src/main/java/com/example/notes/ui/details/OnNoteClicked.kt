package com.example.notes.ui.details

import com.example.notes.domain.Note

interface OnNoteClicked {
    fun onNoteOnClicked(note: Note)
}