package com.example.notes.noteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notes.data.DataSource
import com.example.notes.data.Note
import java.util.*
import kotlin.random.Random


class NotesViewModel(val dataSource: DataSource) : ViewModel() {

    val notesLiveData = dataSource.getNoteList()

    fun insertNote(noteTitle: String?, noteDescription: String?) {
        if (noteTitle == null || noteDescription == null) {
            return
        }

        val image =
            "https://images.freeimages.com/images/large-previews/89a/one-tree-hill-1360813.jpg"

        val newNote = Note(
            Random.nextLong(),
            noteTitle,
            noteDescription,
            Date(),
            image
        )

        dataSource.addNote(newNote)
    }

    fun updateNote(noteTitle: String?, noteDescription: String?) {

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