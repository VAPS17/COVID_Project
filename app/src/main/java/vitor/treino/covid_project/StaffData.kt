package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class StaffData(var id: Long = -1, var identification: Long, var profession: String, var name: String, var idHospital: Long) {

}