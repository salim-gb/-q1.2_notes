package com.example.notes.noteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notes.data.DataSource
import com.example.notes.data.Note
import java.util.*


class NotesViewModel(val dataSource: DataSource) : ViewModel() {

    val notesLiveData = dataSource.getNoteList()

    fun insertNote(note: Note?) {
        if (note?.title == null || note.description == null) {
            return
        }

        val image =
            "https://images.freeimages.com/images/large-previews/89a/one-tree-hill-1360813.jpg"

        val newNote = Note(
            "",
            note.title,
            note.description,
            Date(),
            image
        )

        dataSource.addNote(newNote)
    }

    fun updateNote(note: Note, position: Int?) {
        dataSource.updateNote(note, position)
    }

    fun removeNote(note: Note?) {
        dataSource.removeNote(note)
    }
}

class NotesListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(
                dataSource = DataSource.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}