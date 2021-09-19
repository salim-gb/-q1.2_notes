package com.example.notes.noteList

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.noteInsert.NoteAddFragment
import com.example.notes.ui.AlertDialogFragment
import com.example.notes.ui.CustomDialogFragment
import com.example.notes.ui.OnNoteClickedListener
import com.example.notes.ui.Router
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class NotesListFragment : Fragment(R.layout.fragment_note_list) {
    private lateinit var router: Router
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private var onNoteClicked: OnNoteClickedListener? = null
    private var selectedNote: Note? = null
    private var currentPosition: Int? = null

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
            { note, position -> adapterOnClick(note, position) },
            { note, position -> adapterOnLongClick(note, position) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar: Toolbar = view.findViewById(R.id.topAppBar)
        val navigationView: NavigationView = view.findViewById(R.id.navigationView)

        drawerLayout = view.findViewById(R.id.drawerLayout)

        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
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

        recyclerView = view.findViewById(R.id.notes_list)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = notesAdapter
        }

        viewModel.notesLiveData.observe(viewLifecycleOwner, {
            it?.let {
                notesAdapter.submitList(it as MutableList<Note>)
            }
        })

        parentFragmentManager.setFragmentResultListener(
            NoteAddFragment.KEY_NOTE_ADDED, viewLifecycleOwner, { _, result ->
                val note = result.getParcelable<Note>(NoteAddFragment.ARG_NOTE)
                viewModel.insertNote(note)
            })

        setFragmentResultListener(AlertDialogFragment.KEY) { _, bundle ->
            bundle.getInt(AlertDialogFragment.ARG_WHICH).let {
                when (it) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        viewModel.removeNote(selectedNote)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
        }
    }

    private fun adapterOnClick(note: Note, position: Int) {
        selectedNote = note
        onNoteClicked?.onNoteOnClicked(note, position)
    }

    private fun adapterOnLongClick(note: Note, position: Int) {
        selectedNote = note
        currentPosition = position
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARG_NOTE, selectedNote)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (resources.getBoolean(R.bool.isLandScape)) {
            childFragmentManager.setFragmentResult(
                KEY_SELECTED_NOTE,
                bundleOf(ARG_NOTE to selectedNote)
            )
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
                AlertDialogFragment.newInstance(
                    R.drawable.ic_delete_gray_24,
                    R.string.delete_confirm,
                    R.string.delete_confirm_message
                ).show(parentFragmentManager, "AlertDialogFragment")
                true
            }

            R.id.action_update -> {
                CustomDialogFragment.newInstance(selectedNote!!, currentPosition!!)
                    .show(parentFragmentManager, "customDialogFragment")
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