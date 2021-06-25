package vitor.treino.covid_project

import android.annotation.SuppressLint

class AppData {
    companion object {
        lateinit var activity: MainActivity
        lateinit var hospitalFragment: HospitalFragment
        @SuppressLint("StaticFieldLeak")
        lateinit var hospitalNewFragment: HospitalNewFragment
        lateinit var staffFragment: StaffFragment
        var selectedHospital : HospitalData? = null
    }
}