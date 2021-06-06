package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class StaffData(var id: Long = -1, var identification: Long, var profession: String, var name: String, var idHospital: Long) {

    fun toContentValues(): ContentValues {
        val values = ContentValues().apply {
            put(StaffTable.FIELD_IDENTIFICATION, identification)
            put(StaffTable.FIELD_PROFESSION, profession)
            put(StaffTable.FIELD_NAME, name)
            put(StaffTable.FIELD_ID_HOSPITAL, idHospital)
        }

        return values
    }

    companion object{
        fun fromCursor(cursor: Cursor): StaffData {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colIdentification = cursor.getColumnIndex(StaffTable.FIELD_IDENTIFICATION)
            val colProfession = cursor.getColumnIndex(StaffTable.FIELD_PROFESSION)
            val colName = cursor.getColumnIndex(StaffTable.FIELD_NAME)
            val colIdHospital = cursor.getColumnIndex(StaffTable.FIELD_ID_HOSPITAL)

            val id = cursor.getLong(colId)
            val identification = cursor.getLong(colIdentification)
            val profession = cursor.getString(colProfession)
            val name = cursor.getString(colName)
            val idHospital = cursor.getLong(colIdHospital)

            return StaffData(id, identification, profession, name, idHospital)
        }
    }
}