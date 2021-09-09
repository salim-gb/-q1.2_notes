package com.example.notes.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Note(
    val title: String,
    val description: String,
    val creationDate: Date
) : Parcelable
