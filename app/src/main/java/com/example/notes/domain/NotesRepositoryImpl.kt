package com.example.notes.domain

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NotesRepositoryImpl : NotesRepository {
    override fun getNotes(): List<Note> {
        return listOf(
            Note("first Note", "some first description", getDate()),
            Note("second Note", "some sec description", getDate()),
            Note("third Note", "some third description", getDate()),
            Note("fourth Note", "some forth description", getDate()),
            Note("fifth Note", "some fifth description", getDate())
        )
    }

    override fun addNote() {
        TODO("Not yet implemented")
    }

    override fun deleteNote() {
        TODO("Not yet implemented")
    }

    override fun changeNote() {
        TODO("Not yet implemented")
    }

    private fun getDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss")
            current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("yyyy MMM dd  HH:mma", Locale.ENGLISH)
            formatter.format(date)
        }
    }
}