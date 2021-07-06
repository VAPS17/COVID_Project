package vitor.treino.covid_project

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterStaff(val fragment: StaffFragment) : RecyclerView.Adapter<AdapterStaff.ViewHolderStaff>() {
    var cursor: Cursor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolderStaff(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val textViewStaffName = itemView.findViewById<TextView>(R.id.textViewStaffName)
        private val textViewStaffIdentification = itemView.findViewById<TextView>(R.id.textViewStaffIdentification)
        private val textViewStaffPhone = itemView.findViewById<TextView>(R.id.textViewStaffPhone)
        private val textViewProfessionName = itemView.findViewById<TextView>(R.id.textViewProfessionName)

        private lateinit var staff: StaffData

        init {
            itemView.setOnClickListener(this)
        }

        fun updateStaff(staff: StaffData) {
            this.staff = staff

            textViewStaffName.text = staff.name
            textViewStaffIdentification.text = staff.identifcation.toString()
            textViewStaffPhone.text = staff.phone.toString()
            textViewProfessionName.text = staff.nameProfession
        }

        override fun onClick(v: View?) {
            selected?.desSelect()
            select()
        }

        @SuppressLint("ResourceAsColor")
        private fun select(){
            selected = this
            itemView.setBackgroundColor(R.color.selected)
            AppData.selectedStaff = staff
            AppData.activity.updateEditDelete(true)
        }

        @SuppressLint("ResourceAsColor")
        private fun desSelect(){
            selected = null
            itemView.setBackgroundColor(android.R.color.white)
        }

        companion object{
            @SuppressLint("StaticFieldLeak")
            var selected : ViewHolderStaff? = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStaff {
        val itemStaff = fragment.layoutInflater.inflate(R.layout.item_staff, parent, false)

        return ViewHolderStaff(itemStaff)
    }

    override fun onBindViewHolder(holder: ViewHolderStaff, position: Int) {
        cursor!!.moveToPosition(position)
        holder.updateStaff(StaffData.fromCursor(cursor!!))
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }
}