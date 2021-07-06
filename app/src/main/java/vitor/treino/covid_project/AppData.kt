package vitor.treino.covid_project

import androidx.fragment.app.Fragment

class AppData {
    companion object {
        lateinit var activity: MainActivity
        lateinit var fragment: Fragment
        var selectedHospital : HospitalData? = null
        var selectedStaff : StaffData? = null
    }
}