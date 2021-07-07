package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class DiseaseData(var id: Long = -1, var name: String) {
    fun toContentValues(): ContentValues {

        return ContentValues().apply {
            put(DiseaseTable.FIELD_NAME, name)
        }
    }

    companion object {
        fun fromCursor(cursor: Cursor): DiseaseData {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colName = cursor.getColumnIndex(DiseaseTable.FIELD_NAME)

            val id = cursor.getLong(colId)
            val name = cursor.getString(colName)

            return DiseaseData(id, name)
        }
    }

}