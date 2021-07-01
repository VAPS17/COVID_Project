package vitor.treino.covid_project

import android.app.AlertDialog
import android.database.Cursor
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

        AppData.hospitalFragment = this
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
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    navigateStaff()
                }
                .setNegativeButton("No") { dialog, _ ->
                    // Dismiss the dialog
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

    fun navigateStaff(){
        findNavController().navigate(R.id.action_HospitalFragment_to_NovoHospitalFragment)
    }

    fun optionMenuProcessing(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_staff -> navigateStaff()
            else -> return false
        }
        return true
    }

    companion object {
        const val ID_LOADER_MANAGER_HOSPITAL = 0
    }
}