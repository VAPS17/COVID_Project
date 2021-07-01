package vitor.treino.covid_project

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import vitor.treino.covid_project.databinding.FragmentHospitalEditBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HospitalEditFragment : Fragment() {

    private var _binding: FragmentHospitalEditBinding? = null

    private lateinit var editTextName: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var editTextAddress: EditText

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.hide()

        _binding = FragmentHospitalEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextName = view.findViewById(R.id.editTextName)
        editTextLocation = view.findViewById(R.id.editTextLocation)
        editTextAddress = view.findViewById(R.id.editTextAddress)

        editTextName.addTextChangedListener(confirmHospitalDataWatcher)
        editTextLocation.addTextChangedListener(confirmHospitalDataWatcher)
        editTextAddress.addTextChangedListener(confirmHospitalDataWatcher)

        editTextName.setText(AppData.selectedHospital!!.name)
        editTextLocation.setText(AppData.selectedHospital!!.location)
        editTextAddress.setText(AppData.selectedHospital!!.address)


        _binding?.editHospital?.setOnClickListener{
            editHospital()
            it.hideKeyboard()
        }

        _binding?.cancelHospital?.setOnClickListener {
            navigateHospital()
        }

    }

    private fun editHospital() {
        val name = editTextName.text.toString()
        val location = editTextLocation.text.toString()
        val address = editTextAddress.text.toString()
        val hospital = AppData.selectedHospital!!

        hospital.name = name
        hospital.location = location
        hospital.address = address

        val uriHospital = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_HOSPITAL,
            hospital.id.toString()
        )

        val register = activity?.contentResolver?.update(
            uriHospital,
            hospital.toContentValues(),
            null,
            null
        )

        if (register != 1) {
            Toast.makeText(
                requireContext(),
                R.string.hErrorE,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            R.string.hEdit,
            Toast.LENGTH_LONG
        ).show()

        navigateHospital()
    }

    private fun navigateHospital(){
        findNavController().navigate(R.id.action_hospitalEditFragment_to_hospitalFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val confirmHospitalDataWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val nameInput: String = editTextName.text.toString().trim()
            val locationInput: String = editTextLocation.text.toString().trim()
            val addressInput: String = editTextAddress.text.toString().trim()

            _binding?.editHospital?.isEnabled = nameInput.isNotEmpty() && locationInput.isNotEmpty() && addressInput.isNotEmpty();
        }
        override fun afterTextChanged(s: Editable) {}
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
