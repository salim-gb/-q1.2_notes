package com.example.notes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.data.Note

class MainActivity : AppCompatActivity(), OnNoteClickedListener {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        router = Router(supportFragmentManager)

        if (savedInstanceState == null) {
            router.showNotesList()
        }
    }

    override fun onNoteOnClicked(note: Note) {
        if (!resources.getBoolean(R.bool.isLandScape)) {
            router.showNoteDetail(note)
        }
    }
}