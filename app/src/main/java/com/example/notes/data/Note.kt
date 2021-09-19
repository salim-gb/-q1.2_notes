package com.example.notes.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id : String,
    val title: String,
    val description: String,
    val creationDate: Date,
    val imageUrl: String
) : Parcelable
