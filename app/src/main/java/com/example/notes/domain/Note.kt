package com.example.notes.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Note(
    val title: String,
    val description: String,
    val creationDate: String
) : Parcelable
