package com.example.notes.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val ARG_DATE = "ARG_DATE"
        const val ARG_DATE_SELECTED = "ARG_lDATE_SELECTED"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$year.$month.$dayOfMonth "
        val bundle = Bundle()
        bundle.putString(ARG_DATE, date)
        parentFragmentManager.setFragmentResult(ARG_DATE_SELECTED, bundle)
    }
}