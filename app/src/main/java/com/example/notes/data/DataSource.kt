package com.example.notes.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSource {
    private val initialNoteList = notesList()
    private val notesLiveData = MutableLiveData(initialNoteList)

    fun addNote(note: Note) {
        val currentList = notesLiveData.value
        if (currentList == null) {
            notesLiveData.postValue(listOf(note))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, note)
            notesLiveData.postValue(updatedList)
        }
    }

    fun updateNote(note: Note) {
        val currentList = notesLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
        }
    }

    fun removeNote(note: Note) {
        val currentList = notesLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(note)
            notesLiveData.postValue(updatedList)
        }
    }

    fun getNoteForId(id: Long): Note? {
        notesLiveData.value?.let { notes ->
            return notes.firstOrNull { it.id == id }
        }
        return null
    }

    fun getNoteList(): LiveData<List<Note>> {
        return notesLiveData
    }

    companion object {
        private var INSTANCE: DataSource? = null
        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}