package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class HospitalData(var id: Long = -1, var name: String, var location: String, var address: String, var state: String, var infected: String, var recovered: String) {

    fun toContentValues(): ContentValues {
        val values = ContentValues().apply {
            put(HospitalTable.FIELD_NAME, name)
            put(HospitalTable.FIELD_LOCATION, location)
            put(HospitalTable.FIELD_ADDRESS, address)
            put(HospitalTable.FIELD_STATE, state)
            put(HospitalTable.FIELD_INFECTED, infected)
            put(HospitalTable.FIELD_RECOVERED, recovered)
        }

        return values
    }

    companion object{
        fun fromCursor(cursor: Cursor): HospitalData {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colName = cursor.getColumnIndex(HospitalTable.FIELD_NAME)
            val colLocation = cursor.getColumnIndex(HospitalTable.FIELD_LOCATION)
            val colAddress = cursor.getColumnIndex(HospitalTable.FIELD_ADDRESS)
            val colState = cursor.getColumnIndex(HospitalTable.FIELD_STATE)
            val colInfected = cursor.getColumnIndex(HospitalTable.FIELD_INFECTED)
            val colRecovered = cursor.getColumnIndex(HospitalTable.FIELD_RECOVERED)

            val id = cursor.getLong(colId)
            val name = cursor.getString(colName)
            val location = cursor.getString(colLocation)
            val address = cursor.getString(colAddress)
            val state = cursor.getString(colState)
            val infected = cursor.getString(colInfected)
            val recovered = cursor.getString(colRecovered)

            return HospitalData(id, name, location, address, state, infected, recovered)
        }
    }
}
