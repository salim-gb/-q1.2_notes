package com.example.notes.noteList

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.ui.OnNoteClickedListener
import com.example.notes.ui.Router
import com.example.notes.ui.SettingsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class NotesListFragment : Fragment(R.layout.fragment_note_list) {
    private lateinit var router: Router
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var notesAdapter: NotesAdapter
    private var onNoteClicked: OnNoteClickedListener? = null
    private var selectedNote: Note? = null

    private val viewModel by viewModels<NotesViewModel> {
        NotesListViewModelFactory()
    }

    companion object {
        const val KEY_SELECTED_NOTE = "KEY_SELECTED_NOTE"
        const val ARG_NOTE = "ARG_NOTE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router = Router(parentFragmentManager)

        notesAdapter = NotesAdapter(this,
            { note -> adapterOnClick(note) },
            { note -> adapterOnLongClick(note) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar: Toolbar = view.findViewById(R.id.topAppBar)
        val navigationView: NavigationView = view.findViewById(R.id.navigationView)

        drawerLayout = view.findViewById(R.id.drawerLayout)

        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        view.findViewById<FloatingActionButton>(R.id.fab).
            setOnClickListener {
                router.showNoteAdd()
            }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    router.showSettingsFragment()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    menuItem.isChecked = true
                }
            }
            true
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.notes_list)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = notesAdapter
        }

        viewModel.notesLiveData.observe(viewLifecycleOwner, {
            it?.let {
                notesAdapter.submitList(it as MutableList<Note>)
            }
        })
    }

    private fun adapterOnClick(note: Note) {
        selectedNote = note
        onNoteClicked?.onNoteOnClicked(note)
    }

    private fun adapterOnLongClick(note: Note) {
        selectedNote = note
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARG_NOTE, selectedNote)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (resources.getBoolean(R.bool.isLandScape)) {
            val bundle = Bundle()
            bundle.putParcelable(ARG_NOTE, selectedNote)
            childFragmentManager.setFragmentResult(KEY_SELECTED_NOTE, bundle)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        requireActivity().menuInflater.inflate(R.menu.menu_notes_list_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                Snackbar.make(requireView(), "Delete ${selectedNote?.title}", Snackbar.LENGTH_SHORT)
                    .show()
                true
            }

            R.id.action_update -> {
                Snackbar.make(requireView(), "Update ${selectedNote?.title}", Snackbar.LENGTH_SHORT)
                    .show()
                true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onNoteClicked = context as? OnNoteClickedListener
    }

    override fun onDetach() {
        onNoteClicked = null
        super.onDetach()
    }
}