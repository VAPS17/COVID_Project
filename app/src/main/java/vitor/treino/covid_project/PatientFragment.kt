package vitor.treino.covid_project

import android.app.AlertDialog
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vitor.treino.covid_project.databinding.FragmentPatientBinding

class PatientFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: FragmentPatientBinding? = null
    private var adapterPatient : AdapterPatient? = null
    private var hospitalID : Long? = null
    private var recoveredFlag: Boolean = false

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).currentMenu = R.menu.menu_patient

        _binding = FragmentPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hospitalID = AppData.selectedHospital!!.id

        val recyclerViewPatient = view.findViewById<RecyclerView>(R.id.recyclerViewPatient)
        adapterPatient = AdapterPatient(this)
        recyclerViewPatient.adapter = adapterPatient
        recyclerViewPatient.layoutManager = LinearLayoutManager(requireContext())

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_PATIENT, null, this)

        binding.newPatient.setOnClickListener {
            findNavController().navigate(R.id.action_patientFragment_to_patientNewFragment)
        }

        binding.editPatient.setOnClickListener{
            findNavController().navigate(R.id.action_patientFragment_to_patientEditFragment)
        }

        binding.deletePatient.setOnClickListener {
            val patient = AppData.selectedPatient!!
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Patient")
                .setMessage("Name: " + patient.name +
                        "\nIdentification: " + patient.identifcation +
                        "\nPriority: " + patient.priority +
                        "\nDisease: " + patient.nameDisease
                )
                .setCancelable(false)
                .setPositiveButton("recovered") { _, _ ->
                    recoveredFlag = true
                    deletePatient()
                    updateHospital()
                }
                .setNeutralButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.lost) { _, _ ->
                    recoveredFlag = false
                    deletePatient()
                    updateHospital()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateHospital() {
        val diseaseName = AppData.selectedPatient!!.nameDisease
        val infected = AppData.selectedHospital!!.infected
        val recovered = AppData.selectedHospital!!.recovered
        val hospital = AppData.selectedHospital!!

        if (diseaseName == "COVID-19"){

            if (recoveredFlag){
                hospital.infected = infected - 1
                hospital.recovered = recovered + 1
            } else {
                hospital.infected = infected - 1
            }

            if (infected.toString().toInt() > 2 && infected.toString().toInt() <= 4){
                hospital.state = "Almost Full."
            }

            if (infected.toString().toInt() <= 2){
                hospital.state = "We Have Beds.."
            }

            val uriHospital = Uri.withAppendedPath(
                ContentProviderCovid.ENDERECO_HOSPITAL,
                hospital.id.toString()
            )

            activity?.contentResolver?.update(
                uriHospital,
                hospital.toContentValues(),
                null,
                null
            )
        }
    }

    private fun deletePatient(){
        val uriPatient = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_PATIENT,
            AppData.selectedPatient!!.id.toString()
        )

        val register = activity?.contentResolver?.delete(
            uriPatient,
            null,
            null
        )

        if (register != 1) {
            Toast.makeText(
                requireContext(),
                R.string.pErrorD,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            getString(R.string.pDeleted),
            Toast.LENGTH_LONG
        ).show()

        reloadPatient()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_PATIENT,
            PatientTable.TODAS_COLUNAS,
            "${PatientTable.FIELD_ID_HOSPITAL}=$hospitalID",
            null,
            PatientTable.FIELD_NAME
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterPatient!!.cursor = data
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapterPatient!!.cursor = null
    }

    fun optionMenuProcessingP(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_hospital -> navigateHospital()
            else -> return false
        }
        return true
    }

    private fun navigateHospital(){
        findNavController().navigate(R.id.action_patientFragment_to_hospitalFragment)
    }

    private fun reloadPatient() {
        findNavController().navigate(R.id.action_patientFragment_to_patientNewFragment)
        findNavController().navigate(R.id.action_patientNewFragment_to_patientFragment)
    }

    companion object {
        const val ID_LOADER_MANAGER_PATIENT = 0
    }
}