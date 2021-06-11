package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class StaffData(var id: Long = -1, var identifcation: Long, var phone: Long, var name: String, var idHospital: Long) {
    fun toContentValues(): ContentValues {
        val values = ContentValues().apply {
            put(StaffTable.FIELD_IDENTIFICATION, identifcation)
            put(StaffTable.FIELD_PHONE, phone)
            put(StaffTable.FIELD_NAME, name)
            put(StaffTable.FIELD_ID_HOSPITAL, idHospital)
        }

        return values
    }

    companion object {
        fun fromCursor(cursor: Cursor): StaffData {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colIdentification = cursor.getColumnIndex(StaffTable.FIELD_IDENTIFICATION)
            val colPhone = cursor.getColumnIndex(StaffTable.FIELD_PHONE)
            val colName = cursor.getColumnIndex(StaffTable.FIELD_NAME)
            val colIdHospital = cursor.getColumnIndex(StaffTable.FIELD_ID_HOSPITAL)

            val id = cursor.getLong(colId)
            val identification = cursor.getLong(colIdentification)
            val phone = cursor.getLong(colPhone)
            val name = cursor.getString(colName)
            val idHospital = cursor.getLong(colIdHospital)

            return StaffData(id, identification, phone, name, idHospital)
        }
    }
}