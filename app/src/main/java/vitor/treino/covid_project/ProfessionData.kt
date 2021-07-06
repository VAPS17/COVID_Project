package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class ProfessionData(var id: Long = -1, var name: String) {
    fun toContentValues(): ContentValues {

        return ContentValues().apply {
            put(ProfessionTable.FIELD_NAME, name)
        }
    }

    companion object {
        fun fromCursor(cursor: Cursor): ProfessionData {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colName = cursor.getColumnIndex(ProfessionTable.FIELD_NAME)

            val id = cursor.getLong(colId)
            val name = cursor.getString(colName)

            return ProfessionData(id, name)
        }
    }

}