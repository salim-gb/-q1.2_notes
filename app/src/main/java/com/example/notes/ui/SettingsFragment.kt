package com.example.notes.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.notes.R

class SettingsFragment: Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}