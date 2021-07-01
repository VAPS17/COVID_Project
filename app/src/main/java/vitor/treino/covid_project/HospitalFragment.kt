package vitor.treino.covid_project

import android.app.AlertDialog
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vitor.treino.covid_project.databinding.FragmentHospitalBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HospitalFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: FragmentHospitalBinding? = null
    private var adapterHospital : AdapterHospital? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.show()

        _binding = FragmentHospitalBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewHospital = view.findViewById<RecyclerView>(R.id.recyclerViewHospital)
        adapterHospital = AdapterHospital(this)
        recyclerViewHospital.adapter = adapterHospital
        recyclerViewHospital.layoutManager = LinearLayoutManager(requireContext())

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_HOSPITAL, null, this)


        binding.newHospital.setOnClickListener {
            findNavController().navigate(R.id.action_HospitalFragment_to_NovoHospitalFragment)
        }

        binding.deleteHospital.setOnClickListener {
            val hospital = AppData.selectedHospital!!
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.hTitle)
                .setMessage(getString(R.string.hName)+ " " + hospital.name +
                        "\n" + getString(R.string.hLocation)+ " " + hospital.location +
                        "\n" + getString(R.string.hAddress) + " " + hospital.address)
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { _, _ ->
                    deleteHospital()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteHospital() {
        val uriHospital = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_HOSPITAL,
            AppData.selectedHospital!!.id.toString()
        )

        val register = activity?.contentResolver?.delete(
            uriHospital,
            null,
            null
        )

        if (register != 1) {
            Toast.makeText(
                requireContext(),
                R.string.hErrorD,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            R.string.hDeleted,
            Toast.LENGTH_LONG
        ).show()

        reload()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_HOSPITAL,
            HospitalTable.TODAS_COLUNAS,
            null, null,
            HospitalTable.FIELD_NAME
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterHospital!!.cursor = data
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapterHospital!!.cursor = null
    }

    fun optionMenuProcessing(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_staff -> navigateStaff()
            else -> return false
        }
        return true
    }

    private fun navigateStaff(){
        findNavController().navigate(R.id.action_HospitalFragment_to_NovoHospitalFragment)
        findNavController().navigate(R.id.action_NovoHospitalFragment_to_HospitalFragment)
    }

    private fun reload() {
        findNavController().navigate(R.id.action_HospitalFragment_to_NovoHospitalFragment)
        findNavController().navigate(R.id.action_NovoHospitalFragment_to_HospitalFragment)
    }

    companion object {
        const val ID_LOADER_MANAGER_HOSPITAL = 0
    }
}