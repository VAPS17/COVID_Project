package vitor.treino.covid_project

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterHospital(val fragment: HospitalFragment) : RecyclerView.Adapter<AdapterHospital.ViewHolderHospital>() {
    var cursor: Cursor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolderHospital(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val textViewName = itemView.findViewById<TextView>(R.id.textViewName)
        private val textViewLocation = itemView.findViewById<TextView>(R.id.textViewLocation)
        private val textViewAddress = itemView.findViewById<TextView>(R.id.textViewAddress)

        private lateinit var hospital: HospitalData

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun updateHospital(hospital: HospitalData) {
            this.hospital = hospital

            textViewName.text = hospital.name
            textViewLocation.text = hospital.location
            textViewAddress.text = " | " + hospital.address
        }

        override fun onClick(v: View?) {
            selected?.desSelect()
            select()
        }


        @SuppressLint("ResourceAsColor")
        private fun select(){
            selected = this
            itemView.setBackgroundColor(R.color.selected)
            AppData.selectedHospital = hospital
            AppData.activity.updateEditDelete(true)
        }

        @SuppressLint("ResourceAsColor")
        private fun desSelect(){
            selected = null
            itemView.setBackgroundColor(android.R.color.white)
        }

        companion object{
            @SuppressLint("StaticFieldLeak")
            var selected : ViewHolderHospital? = null
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