package vitor.treino.covid_project

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppData.fragment = this
        (activity as MainActivity).supportActionBar?.show()

        _binding = FragmentStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewStaff = view.findViewById<RecyclerView>(R.id.recyclerViewStaff)
        adapterStaff = AdapterStaff(this)
        recyclerViewStaff.adapter = adapterStaff
        recyclerViewStaff.layoutManager = LinearLayoutManager(requireContext())

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_STAFF, null, this)


        binding.newStaff.setOnClickListener {
            //findNavController().navigate(R.id.action_HospitalFragment_to_NovoHospitalFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_STAFF,
            StaffTable.TODAS_COLUNAS,
            null, null,
            StaffTable.FIELD_NAME
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterStaff!!.cursor = data
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapterStaff!!.cursor = null
    }

    companion object {
        const val ID_LOADER_MANAGER_STAFF = 0
    }
}