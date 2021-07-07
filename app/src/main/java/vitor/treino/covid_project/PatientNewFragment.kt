package vitor.treino.covid_project

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.Toast
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import vitor.treino.covid_project.databinding.FragmentPatientNewBinding

class PatientNewFragment: Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: FragmentPatientNewBinding? = null
    private val binding get() = _binding!!
    private var hospitalID : Long? = null

    private lateinit var editTextPatientName: EditText
    private lateinit var editTextPatientIdentification: EditText
    //private lateinit var editTextPatientPriority: EditText
    private lateinit var spinnerDisease: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.hide()

        _binding = FragmentPatientNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hospitalID = AppData.selectedHospital!!.id

        editTextPatientName = view.findViewById(R.id.editTextPatientName)
        editTextPatientIdentification = view.findViewById(R.id.editTextPatientIdentification)

        spinnerDisease = view.findViewById(R.id.spinnerDisease)

        editTextPatientName.addTextChangedListener(confirmPatientDataWatcher)
        editTextPatientIdentification.addTextChangedListener(confirmPatientDataWatcher)

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_DISEASE, null, this)
/*
        binding.addStaff.setOnClickListener {
            saveStaff()
            it.hideKeyboard()
        }
*/
        binding.cancelPatient.setOnClickListener {
            navigatePatient()
            it.hideKeyboard()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigatePatient(){
        findNavController().navigate(R.id.action_patientNewFragment_to_patientFragment)
    }

    private fun updateDiseaseSpinner(data: Cursor?){
        spinnerDisease.adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data,
            arrayOf(DiseaseTable.FIELD_NAME),
            intArrayOf(android.R.id.text1),
            0
        )
    }
/*
    private fun saveStaff(){
        val name = editTextName.text.toString()
        val identification = editTextIdentification.text.toString().toLong()
        val phone = editTextPhone.text.toString().toLong()
        val professionId = spinnerProfession.selectedItemId

        val staff = StaffData(name = name,
            identifcation = identification,
            phone = phone,
            idHospital = hospitalID,
            idProfession = professionId
        )

        val uri = activity?.contentResolver?.insert(
            ContentProviderCovid.ENDERECO_STAFF,
            staff.toContentValues()
        )

        if (uri == null) {
            Snackbar.make(
                editTextName,
                R.string.sErrorI,
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            R.string.sSaved,
            Toast.LENGTH_LONG
        ).show()
        navigateStaff()
    }
*/
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_DISEASE,
            DiseaseTable.TODAS_COLUNAS,
            null, null,
            DiseaseTable.FIELD_NAME
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        updateDiseaseSpinner(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        updateDiseaseSpinner(null)
    }

    private val confirmPatientDataWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val nameInput: String = editTextPatientName.text.toString().trim()
            val identificationInput: String = editTextPatientIdentification.text.toString().trim()

            _binding?.addPatient?.isEnabled = nameInput.isNotEmpty() &&
                    identificationInput.isNotEmpty();
        }
        override fun afterTextChanged(s: Editable) {}
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {
        const val ID_LOADER_MANAGER_DISEASE = 0
    }

}