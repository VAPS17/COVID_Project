package vitor.treino.covid_project

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import vitor.treino.covid_project.databinding.FragmentPatientEditBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PatientEditFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentPatientEditBinding? = null

    private lateinit var editTextPatientName: EditText
    private lateinit var editTextPatientIdenfication: EditText
    private lateinit var radioGroupPriority: RadioGroup
    private lateinit var spinnerDisease: Spinner
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.hide()

        _binding = FragmentPatientEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextPatientName = view.findViewById(R.id.editTextPatientName)
        editTextPatientIdenfication = view.findViewById(R.id.editTextPatientIdentification)

        spinnerDisease = view.findViewById(R.id.spinnerDisease)

        editTextPatientName.addTextChangedListener(confirmPatientDataWatcher)
        editTextPatientIdenfication.addTextChangedListener(confirmPatientDataWatcher)

        editTextPatientName.setText(AppData.selectedPatient!!.name)
        editTextPatientIdenfication.setText(AppData.selectedPatient!!.identifcation.toString())

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_DISEASE, null, this)

/*
        _binding?.editPatient?.setOnClickListener{
            editStaff()
            it.hideKeyboard()
        }

        _binding?.cancelStaff?.setOnClickListener {
            navigateStaff()
            it.hideKeyboard()
        }
 */

    }
/*
    private fun editStaff() {
        val name = editTextName.text.toString()
        val identification = editTextIdenfication.text.toString().toLong()
        val phone = editTextPhone.text.toString().toLong()
        val idProfession = spinnerProfession.selectedItemId
        val staff = AppData.selectedStaff!!

        staff.name = name
        staff.identifcation = identification
        staff.phone = phone
        staff.idProfession = idProfession

        val uriStaff = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_STAFF,
            staff.id.toString()
        )

        val register = activity?.contentResolver?.update(
            uriStaff,
            staff.toContentValues(),
            null,
            null
        )

        if (register != 1) {
            Toast.makeText(
                requireContext(),
                R.string.sErrorE,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            R.string.sEdited,
            Toast.LENGTH_LONG
        ).show()

        navigateStaff()
    }
*/
    private fun navigateStaff(){
        findNavController().navigate(R.id.action_staffEditFragment_to_staffFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val confirmPatientDataWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val nameInput: String = editTextPatientName.text.toString().trim()
            val identificationInput: String = editTextPatientIdenfication.text.toString().trim()

            _binding?.editPatient?.isEnabled = nameInput.isNotEmpty() &&
                    identificationInput.isNotEmpty()
        }
        override fun afterTextChanged(s: Editable) {}
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
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

    private fun updateSelectedDisease(){
        val idDisease = AppData.selectedPatient!!.idDisease

        val lastDisease = spinnerDisease.count - 1
        for(i in 0..lastDisease){
            if (idDisease == spinnerDisease.getItemIdAtPosition(i)){
                spinnerDisease.setSelection(i)
                return
            }
        }
    }

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
        updateSelectedDisease()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        updateDiseaseSpinner(null)
    }

    companion object{
        const val ID_LOADER_MANAGER_DISEASE = 0
    }
}
