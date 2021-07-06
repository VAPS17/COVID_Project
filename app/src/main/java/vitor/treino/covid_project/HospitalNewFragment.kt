package vitor.treino.covid_project

import android.content.Context
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
import vitor.treino.covid_project.databinding.FragmentHospitalNewBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HospitalNewFragment : Fragment() {

    private var _binding: FragmentHospitalNewBinding? = null

    private lateinit var editTextName: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var editTextAddress: EditText
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.hide()

        _binding = FragmentHospitalNewBinding.inflate(inflater, container, false)
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

        _binding?.addHospital?.setOnClickListener{
            saveHospital()
            it.hideKeyboard()
        }

        _binding?.cancelHospital?.setOnClickListener {
            navigateHospital()
        }

    }

    private fun navigateHospital(){
        findNavController().navigate(R.id.action_hospitalNewFragment_to_hospitalFragment)
    }

    private fun saveHospital() {
        val name = editTextName.text.toString()
        val location = editTextLocation.text.toString()
        val address = editTextAddress.text.toString()

        val hospital = HospitalData(name = name, location = location, address = address, state = "", infected = 0, recovered = 0)

        val uri = activity?.contentResolver?.insert(
            ContentProviderCovid.ENDERECO_HOSPITAL,
            hospital.toContentValues()
        )

        if (uri == null){
            Snackbar.make(
                editTextName,
                R.string.hErrorI,
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            R.string.hSaved,
            Toast.LENGTH_LONG
        ).show()
        navigateHospital()
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

            _binding?.addHospital?.isEnabled = nameInput.isNotEmpty() && locationInput.isNotEmpty() && addressInput.isNotEmpty();
        }
        override fun afterTextChanged(s: Editable) {}
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
