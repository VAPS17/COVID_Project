package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class HospitalData(var id: Long = -1, var name: String, var location: String, var address: String, var state: String) {
}
