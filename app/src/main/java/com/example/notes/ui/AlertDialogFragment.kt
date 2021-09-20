package com.example.notes.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.notes.R


class AlertDialogFragment : DialogFragment() {
    companion object {
        const val KEY = "DialogFragment"
        const val ARG_WHICH = "ARG_WHICH"
        const val ARG_ICON = "ARG_ICON"
        const val ARG_TITLE = "ARG_TITLE"
        const val ARG_MESSAGE = "ARG_MESSAGE"

        fun newInstance(
            @DrawableRes icon: Int,
            @StringRes title: Int,
            @StringRes msg: Int
        ) = AlertDialogFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_ICON, icon)
                putInt(ARG_TITLE, title)
                putInt(ARG_MESSAGE, msg)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val listener = DialogInterface.OnClickListener { _, which ->
            setFragmentResult(KEY, bundleOf(ARG_WHICH to which))
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(requireArguments().getInt(ARG_TITLE))
            .setMessage(requireArguments().getInt(ARG_MESSAGE))
            .setIcon(requireArguments().getInt(ARG_ICON))
            .setPositiveButton(R.string.delete, listener)
            .setNegativeButton(R.string.cancel, listener)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }


}