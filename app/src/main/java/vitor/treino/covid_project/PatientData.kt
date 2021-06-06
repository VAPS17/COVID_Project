package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class PatientData(var id: Long = -1, var identification: Long, var name: String, var disease: String, var priority: String) {

}