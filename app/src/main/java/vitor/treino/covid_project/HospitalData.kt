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

    companion object{
        fun fromCursor(cursor: Cursor): HospitalData {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colName = cursor.getColumnIndex(HospitalTable.FIELD_NAME)
            val colLocation = cursor.getColumnIndex(HospitalTable.FIELD_LOCATION)
            val colAddress = cursor.getColumnIndex(HospitalTable.FIELD_ADDRESS)
            val colState = cursor.getColumnIndex(HospitalTable.FIELD_STATE)

            val id = cursor.getLong(colId)
            val name = cursor.getString(colName)
            val location = cursor.getString(colLocation)
            val address = cursor.getString(colAddress)
            val state = cursor.getString(colState)

            return HospitalData(id, name, location, address, state)
        }
    }
}
