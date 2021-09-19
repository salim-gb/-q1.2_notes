package com.example.notes.ui

import com.example.notes.data.Note

interface OnNoteClickedListener {
    fun onNoteOnClicked(note: Note, position: Int)
}