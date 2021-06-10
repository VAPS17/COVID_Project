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
import vitor.treino.covid_project.databinding.FragmentHospitalBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HospitalFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentHospitalBinding? = null
    private var adapterHospital: AdapterHospital? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderDB.ENDERECO_HOSPITAL,
            HospitalTable.TODA_COLUNAS,
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

    companion object {
        const val ID_LOADER_MANAGER_HOSPITAL = 0
    }
}