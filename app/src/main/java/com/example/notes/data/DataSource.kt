package com.example.notes.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class DataSource {

    private val db = Firebase.firestore
    private val notesLiveData = getNotes()

    private fun getNotes(): MutableLiveData<List<Note>> {

        val mutableData = MutableLiveData<List<Note>>()

        db.collection(NOTES).get().addOnSuccessListener { result ->

            val listData = ArrayList<Note>()
            val date = Date()

            for (document in result) {

                val title: String = document.getString("title") ?: ""
                val description: String = document.getString("description") ?: ""
                val imageUrl: String = document.getString("imageUrl") ?: ""

                listData.add(Note(document.id, title, description, date, imageUrl))
            }

            mutableData.value = listData
        }

        return mutableData
    }

    fun addNote(note: Note) {

        val currentList = notesLiveData.value

        val data = hashMapOf(
            "title" to note.title,
            "description" to note.description,
            "imageUrl" to note.imageUrl,
        )

        db.collection(NOTES)
            .add(data)
            .addOnSuccessListener {
                if (currentList == null) {
                    notesLiveData.postValue(listOf(note))
                } else {
                    val updatedList = currentList.toMutableList()
                    updatedList.add(0, note)
                    notesLiveData.postValue(updatedList)
                }
            }
    }

    fun updateNote(note: Note, position: Int?) {
        val currentList = notesLiveData.value

        val ref = db.collection(NOTES).document(note.id)

        ref.update(
            mapOf(
                "title" to note.title,
                "description" to note.description
            )
        )

        if (currentList != null && position != null) {
            val updatedList = currentList.toMutableList()
            updatedList[position] = note
            notesLiveData.postValue(updatedList)
        }
    }

    fun removeNote(note: Note?) {
        val currentList = notesLiveData.value
        if (currentList != null && note != null) {

            val ref = db.collection(NOTES).document(note.id)

            ref.delete()

            val updatedList = currentList.toMutableList()
            updatedList.remove(note)
            notesLiveData.postValue(updatedList)
        }
    }

    fun getNoteForId(id: String): Note? {
        notesLiveData.value?.let { notes ->
            return notes.firstOrNull { it.id == id }
        }
        return null
    }

    fun getNoteList(): LiveData<List<Note>> {
        return notesLiveData
    }

    companion object {
        const val NOTES = "notes"

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