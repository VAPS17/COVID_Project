package vitor.treino.covid_project

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterStaff(val fragment: StaffFragment) : RecyclerView.Adapter<AdapterHospital.ViewHolderHospital>() {
    var cursor: Cursor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolderStaff(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val textViewStaffName = itemView.findViewById<TextView>(R.id.textViewStaffName)
        private val textViewStaffIdentification =
            itemView.findViewById<TextView>(R.id.textViewStaffIdentification)
        private val textViewStaffPhone = itemView.findViewById<TextView>(R.id.textViewStaffPhone)
        private val textViewProfessionName =
            itemView.findViewById<TextView>(R.id.textViewProfessionName)

        private lateinit var staff: StaffData

        init {
            itemView.setOnClickListener(this)
        }

        fun updateStaff(hospital: StaffData) {
            this.staff = staff

            textViewStaffName.text = staff.name
            textViewStaffIdentification.text = staff.identifcation.toString()
            textViewStaffPhone.text = staff.phone.toString()
            textViewProfessionName.text = staff.nameProfession
        }

        override fun onClick(v: View?) {
            //AdapterHospital.ViewHolderHospital.selected?.desSelect()
            // select()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterHospital.ViewHolderHospital {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: AdapterHospital.ViewHolderHospital, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}