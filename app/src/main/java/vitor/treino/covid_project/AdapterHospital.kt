package vitor.treino.covid_project

import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterHospital(val fragment: HospitalFragment): RecyclerView.Adapter<AdapterHospital.ViewHolderHospital> {
    public var cursor: Cursor? = null
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolderHospital {
        private val text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHospital {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolderHospital, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}