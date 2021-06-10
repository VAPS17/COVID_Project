package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class PatientData(var id: Long = -1, var identification: Long, var name: String, var disease: String, var priority: String, var idHospital:Long) {
    fun toContentValues(): ContentValues {
        val values = ContentValues().apply {
            put(PatientTable.FIELD_IDENTIFICATION, identification)
            put(PatientTable.FIELD_NAME, name)
            put(PatientTable.FIELD_DISEASE, disease)
            put(PatientTable.FIELD_PRIORITY, priority)
            put(PatientTable.FIELD_ID_HOSPITAL, idHospital)
        }

        return values
    }

    companion object{
        fun fromCursor(cursor: Cursor): PatientData {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colIdentification = cursor.getColumnIndex(PatientTable.FIELD_IDENTIFICATION)
            val colName = cursor.getColumnIndex(PatientTable.FIELD_NAME)
            val colDisease = cursor.getColumnIndex(PatientTable.FIELD_DISEASE)
            val colPriority = cursor.getColumnIndex(PatientTable.FIELD_PRIORITY)
            val colIdHospital = cursor.getColumnIndex(PatientTable.FIELD_ID_HOSPITAL)

            val id = cursor.getLong(colId)
            val identification = cursor.getLong(colIdentification)
            val name = cursor.getString(colName)
            val disease = cursor.getString(colDisease)
            val priority = cursor.getString(colPriority)
            val idHospital = cursor.getLong(colIdHospital)

            return PatientData(id, identification, name, disease, priority, idHospital)
        }
    }
}