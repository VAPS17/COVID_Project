package vitor.treino.covid_project

class AppData {
    companion object {
        lateinit var activity: MainActivity
        lateinit var hospitalFragment: HospitalFragment
        var selectedHospital : HospitalData? = null
    }
}