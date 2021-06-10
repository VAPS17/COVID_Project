package vitor.treino.covid_project

import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterHospital(val fragment: HospitalFragment): RecyclerView.Adapter<AdapterHospital.ViewHolderHospital>() {
    public var cursor: Cursor? = null
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolderHospital(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textViewName =  itemView.findViewById<TextView>(R.id.textViewName)
        private val textViewLocation = itemView.findViewById<TextView>(R.id.textViewLocation)
        private val textViewAddress = itemView.findViewById<TextView>(R.id.textViewAddress)

        fun updateHospital(hospital: HospitalData) {
            textViewName.text = hospital.name
            textViewLocation.text = hospital.location
            textViewAddress.text = hospital.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHospital {
        val itemHospital = fragment.layoutInflater.inflate(R.layout.item_hospital, parent, false)

        return ViewHolderHospital(itemHospital)
    }

    override fun onBindViewHolder(holder: ViewHolderHospital, position: Int) {
        cursor!!.moveToPosition(position)
        holder.updateHospital(HospitalData.fromCursor(cursor!!))
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }
}