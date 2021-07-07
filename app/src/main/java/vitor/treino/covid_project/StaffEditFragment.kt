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
import android.widget.EditText
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import vitor.treino.covid_project.databinding.FragmentStaffEditBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class StaffEditFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentStaffEditBinding? = null

    private lateinit var editTextName: EditText
    private lateinit var editTextIdenfication: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var spinnerProfession: Spinner
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.hide()

        _binding = FragmentStaffEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextName = view.findViewById(R.id.editTextName)
        editTextIdenfication = view.findViewById(R.id.editTextIdentification)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        spinnerProfession = view.findViewById(R.id.spinnerProfession)

        editTextName.addTextChangedListener(confirmStaffDataWatcher)
        editTextIdenfication.addTextChangedListener(confirmStaffDataWatcher)
        editTextPhone.addTextChangedListener(confirmStaffDataWatcher)

        editTextName.setText(AppData.selectedStaff!!.name)
        editTextIdenfication.setText(AppData.selectedStaff!!.identifcation.toString())
        editTextPhone.setText(AppData.selectedStaff!!.phone.toString())

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_PROFESSION, null, this)


        _binding?.editStaff?.setOnClickListener{
            editStaff()
            it.hideKeyboard()
        }

        _binding?.cancelStaff?.setOnClickListener {
            navigateStaff()
            it.hideKeyboard()
        }

    }

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

    private fun navigateStaff(){
        findNavController().navigate(R.id.action_staffEditFragment_to_staffFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val confirmStaffDataWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val nameInput: String = editTextName.text.toString().trim()
            val identificationInput: String = editTextIdenfication.text.toString().trim()
            val phoneInput: String = editTextPhone.text.toString().trim()

            _binding?.editStaff?.isEnabled = nameInput.isNotEmpty() &&
                    identificationInput.isNotEmpty() && phoneInput.isNotEmpty();
        }
        override fun afterTextChanged(s: Editable) {}
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
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

    private fun updateSelectedProfession(){
        val idProfession = AppData.selectedStaff!!.idProfession

        val lastProfession = spinnerProfession.count - 1
        for(i in 0..lastProfession){
            if (idProfession == spinnerProfession.getItemIdAtPosition(i)){
                spinnerProfession.setSelection(i)
                return
            }
        }
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
        updateSelectedProfession()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        updateProfessionSpinner(null)
    }

    companion object{
        const val ID_LOADER_MANAGER_PROFESSION = 0
    }
}
