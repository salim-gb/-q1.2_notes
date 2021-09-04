package com.example.notes.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.domain.Note

class NoteDetailsActivity : AppCompatActivity() {

    companion object {
        const val ARG_NOTE = "ARG_NOTE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        if (resources.getBoolean(R.bool.isLandScape)) {
            finish()
        } else {
            val note: Note? = intent.getParcelableExtra(ARG_NOTE)

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NoteDetailFragment.newInstance(note), null)
                .commit()
        }
    }
}