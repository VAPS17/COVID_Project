package vitor.treino.covid_project

class AppData {
    companion object {
        lateinit var activity: MainActivity
        lateinit var hospitalFragment: HospitalFragment
        lateinit var staffFragment: StaffFragment
        var selectedHospital : HospitalData? = null
    }
}