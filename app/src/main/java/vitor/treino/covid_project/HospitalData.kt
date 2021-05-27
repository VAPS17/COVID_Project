package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class HospitalData(var id: Long = -1, var name: String, var location: String, var address: String, var state: String) {

    fun toContentValues(): ContentValues {
        val values = ContentValues()

        values.put(HospitalTable.FIELD_NAME, name)
        values.put(HospitalTable.FIELD_LOCATION, location)
        values.put(HospitalTable.FIELD_ADDRESS, address)
        values.put(HospitalTable.FIELD_STATE, state)

        return values
    }
}
