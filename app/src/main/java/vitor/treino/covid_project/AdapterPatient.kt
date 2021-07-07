package vitor.treino.covid_project

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterPatient(val fragment: PatientFragment) : RecyclerView.Adapter<AdapterPatient.ViewHolderPatient>() {
    var cursor: Cursor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolderPatient(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val textViewPatientName = itemView.findViewById<TextView>(R.id.textViewPatientName)
        private val textViewPatientIdentification = itemView.findViewById<TextView>(R.id.textViewPatientIdentification)
        private val textViewPatientPriority = itemView.findViewById<TextView>(R.id.textViewPatientPriority)
        private val textViewDiseaseName = itemView.findViewById<TextView>(R.id.textViewDiseaseName)

        private lateinit var patient: PatientData

        init {
            itemView.setOnClickListener(this)
        }

        fun updatePatient(patient: PatientData) {
            this.patient = patient

            textViewPatientName.text = patient.name
            textViewPatientIdentification.text = patient.identifcation.toString()
            textViewPatientPriority.text = patient.priority
            textViewDiseaseName.text = patient.nameDisease
        }

        override fun onClick(v: View?) {
            selected?.desSelect()
            select()
        }

        @SuppressLint("ResourceAsColor")
        private fun select(){
            selected = this
            itemView.setBackgroundColor(R.color.selected)
            AppData.selectedPatient = patient
            AppData.activity.updateEditDeletePatient(true)
        }

        @SuppressLint("ResourceAsColor")
        private fun desSelect(){
            selected = null
            itemView.setBackgroundColor(android.R.color.white)
        }

        companion object{
            @SuppressLint("StaticFieldLeak")
            var selected : ViewHolderPatient? = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPatient {
        val itemPatient = fragment.layoutInflater.inflate(R.layout.item_patient, parent, false)

        return ViewHolderPatient(itemPatient)
    }

    override fun onBindViewHolder(holder: ViewHolderPatient, position: Int) {
        cursor!!.moveToPosition(position)
        holder.updatePatient(PatientData.fromCursor(cursor!!))
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }
}