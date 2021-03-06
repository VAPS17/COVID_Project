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
import vitor.treino.covid_project.databinding.FragmentStaffBinding

class StaffFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: FragmentStaffBinding? = null
    private var adapterStaff : AdapterStaff? = null
    private var hospitalID : Long? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).currentMenu = R.menu.menu_staff

        _binding = FragmentStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hospitalID = AppData.selectedHospital!!.id

        val recyclerViewStaff = view.findViewById<RecyclerView>(R.id.recyclerViewStaff)
        adapterStaff = AdapterStaff(this)
        recyclerViewStaff.adapter = adapterStaff
        recyclerViewStaff.layoutManager = LinearLayoutManager(requireContext())

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_STAFF, null, this)


        binding.newStaff.setOnClickListener {
            findNavController().navigate(R.id.action_staffFragment_to_staffNewFragment)
        }

        binding.editStaff.setOnClickListener{
            findNavController().navigate(R.id.action_staffFragment_to_staffEditFragment)
        }

        binding.deleteStaff.setOnClickListener {
            val staff = AppData.selectedStaff!!
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Staff")
                .setMessage("Name: " + staff.name +
                        "\nIdentification: " + staff.identifcation +
                        "\nPhone: " + staff.phone +
                        "\nProfession: " + staff.nameProfession
                )
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { _, _ ->
                    deleteStaff()
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

    private fun deleteStaff(){
        val uriStaff = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_STAFF,
            AppData.selectedStaff!!.id.toString()
        )

        val register = activity?.contentResolver?.delete(
            uriStaff,
            null,
            null
        )

        if (register != 1) {
            Toast.makeText(
                requireContext(),
                R.string.sErrorD,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            R.string.sDeleted,
            Toast.LENGTH_LONG
        ).show()

        reloadStaff()
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_STAFF,
            StaffTable.TODAS_COLUNAS,
            "${StaffTable.FIELD_ID_HOSPITAL}=$hospitalID",
            null,
            StaffTable.FIELD_NAME
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterStaff!!.cursor = data
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapterStaff!!.cursor = null
    }

    fun optionMenuProcessingS(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_hospital -> navigateHospital()
            else -> return false
        }
        return true
    }

    private fun navigateHospital(){
        findNavController().navigate(R.id.action_staffFragment_to_hospitalFragment)
    }

    private fun reloadStaff() {
        findNavController().navigate(R.id.action_staffFragment_to_staffNewFragment)
        findNavController().navigate(R.id.action_staffNewFragment_to_staffFragment)
    }

    companion object {
        const val ID_LOADER_MANAGER_STAFF = 0
    }
}