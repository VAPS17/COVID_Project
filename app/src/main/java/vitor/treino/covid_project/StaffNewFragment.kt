package vitor.treino.covid_project

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import vitor.treino.covid_project.databinding.FragmentHospitalNewBinding
import vitor.treino.covid_project.databinding.FragmentStaffNewBinding

class StaffNewFragment: Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: FragmentStaffNewBinding? = null
    private val binding get() = _binding!!

    private lateinit var editTextName: EditText
    private lateinit var editTextIdentification: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var spinnerProfession: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.hide()

        _binding = FragmentStaffNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextName = view.findViewById(R.id.editTextName)
        editTextIdentification = view.findViewById(R.id.editTextIdentification)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        spinnerProfession = view.findViewById(R.id.spinnerProfession)

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_PROFESSION, null, this)

        binding.cancelStaff.setOnClickListener {
            findNavController().navigate(R.id.action_staffNewFragment_to_staffFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_PROFESSION,
            ProfessionTable.TODAS_COLUNAS,
            null, null,
            ProfessionTable.FIELD_NAME
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        updateProfessionSpinner(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        updateProfessionSpinner(null)
    }

    private fun updateProfessionSpinner(data: Cursor?){
        spinnerProfession.adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data,
            arrayOf(ProfessionTable.FIELD_NAME),
            intArrayOf(android.R.id.text1),
            0
        )
    }

    companion object {
        const val ID_LOADER_MANAGER_PROFESSION = 0
    }

}