package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class PatientData(
    var id: Long = -1,
    var identifcation: Long,
    var name: String,
    var priority: String,
    var idHospital: Long?,
    var idDisease: Long,
    var nameDisease: String? = null) {
    fun toContentValues(): ContentValues {

        return ContentValues().apply {
            put(PatientTable.FIELD_IDENTIFICATION, identifcation)
            put(PatientTable.FIELD_NAME, name)
            put(PatientTable.FIELD_PRIORITY, priority)
            put(PatientTable.FIELD_ID_HOSPITAL, idHospital)
            put(PatientTable.FIELD_ID_DISEASE, idDisease)
        }
    }

    companion object {
        fun fromCursor(cursor: Cursor): PatientData {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colIdentification = cursor.getColumnIndex(PatientTable.FIELD_IDENTIFICATION)
            val colName = cursor.getColumnIndex(PatientTable.FIELD_NAME)
            val colPriority = cursor.getColumnIndex(PatientTable.FIELD_PRIORITY)
            val colIdHospital = cursor.getColumnIndex(PatientTable.FIELD_ID_HOSPITAL)
            val colIdDisease = cursor.getColumnIndex(PatientTable.FIELD_ID_DISEASE)
            val colNameDisease = cursor.getColumnIndex(PatientTable.FIELD_EXTERN_DISEASE_NAME)

            val id = cursor.getLong(colId)
            val identification = cursor.getLong(colIdentification)
            val name = cursor.getString(colName)
            val priority = cursor.getString(colPriority)
            val idHospital = cursor.getLong(colIdHospital)
            val idDisease = cursor.getLong(colIdDisease)
            val nameDisease = if (colNameDisease != -1) cursor.getString(colNameDisease) else null

            return PatientData(id, identification, name, priority, idHospital, idDisease,
                nameDisease)
        }
    }
}