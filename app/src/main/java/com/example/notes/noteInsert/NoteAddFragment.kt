package com.example.notes.noteInsert

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.notes.R

class NoteAddFragment : Fragment(R.layout.fragment_add_note) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.note_add_toolbar)

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}