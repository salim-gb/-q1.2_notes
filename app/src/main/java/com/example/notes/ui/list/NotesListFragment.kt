package com.example.notes.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.notes.R
import com.example.notes.domain.Note
import com.example.notes.domain.NotesRepositoryImpl
import com.example.notes.ui.SettingsFragment
import com.example.notes.ui.details.NoteDetailFragment
import com.example.notes.ui.details.OnNoteClicked
import com.google.android.material.navigation.NavigationView

class NotesListFragment : Fragment(R.layout.fragment_note_list), NotesListView {

    companion object {
        const val KEY_SELECTED_NOTE = "KEY_SELECTED_NOTE"
        const val ARG_NOTE = "ARG_NOTE"
    }

    private lateinit var drawerLayout: DrawerLayout
    private var selectedNote: Note? = null
    private lateinit var presenter: NotesListPresenter
    private var onNoteClicked: OnNoteClicked? = null
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = NotesListPresenter(this, NotesRepositoryImpl())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view.findViewById(R.id.root)
        presenter.requestNotes()

        val topAppBar: Toolbar = view.findViewById(R.id.topAppBar)
        val navigationView: NavigationView = view.findViewById(R.id.navigationView)
        drawerLayout = view.findViewById(R.id.drawerLayout)

        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

//        childFragmentManager.setFragmentResultListener(
//            KEY_SELECTED_NOTE,
//            viewLifecycleOwner, { _, result ->
//                result.getParcelable<Note>(ARG_NOTE).apply {
//
//                    childFragmentManager.beginTransaction()
//                        .replace(R.id.container, NoteDetailFragment.newInstance(this))
//                        .commit()
//                }
//            }
//        )

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment())
                        .addToBackStack("HomeFragment")
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    menuItem.isChecked = true
                }
            }
            true
        }
    }

    override fun showNotes(notes: List<Note>) {
        for (note in notes) {
            val noteItem = LayoutInflater.from(context).inflate(
                R.layout.item_note,
                container,
                false
            )

            noteItem.setOnClickListener {

                onNoteClicked?.onNoteOnClicked(note)

                broadcastNote(note)
            }

            val noteName = noteItem.findViewById<TextView>(R.id.note_title)
            val noteDescription = noteItem.findViewById<TextView>(R.id.note_description)
            val noteCreationDate = noteItem.findViewById<TextView>(R.id.note_creation_date)
            noteName.text = note.title
            noteDescription.text = note.description
            noteCreationDate.text = note.creationDate.toString()

            container.addView(noteItem)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARG_NOTE, selectedNote)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        selectedNote = savedInstanceState?.getParcelable(ARG_NOTE)

        if (resources.getBoolean(R.bool.isLandScape)) {
            val bundle = Bundle()
            bundle.putParcelable(ARG_NOTE, selectedNote)
            childFragmentManager.setFragmentResult(KEY_SELECTED_NOTE, bundle)
        }
    }

    private fun broadcastNote(note: Note) {
        val bundle = Bundle()
        bundle.putParcelable(ARG_NOTE, note)
        parentFragmentManager.setFragmentResult(KEY_SELECTED_NOTE, bundle)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onNoteClicked = context as? OnNoteClicked
    }

    override fun onDetach() {
        onNoteClicked = null
        super.onDetach()
    }
}