package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class  PatientTable(db: SQLiteDatabase){
    private val db: SQLiteDatabase = db

    fun create() {
        db.execSQL("CREATE TABLE $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, $FIELD_IDENTIFICATION NOT NULL UNIQUE, $FIELD_NAME TEXT NOT NULL, $FIELD_PRIORITY TEXT NOT NULL, $FIELD_ID_HOSPITAL INTEGER NOT NULL, $FIELD_ID_DISEASE INTEGER NOT NULL, FOREIGN KEY($FIELD_ID_HOSPITAL) REFERENCES ${HospitalTable.TABLE_NAME}, FOREIGN KEY($FIELD_ID_DISEASE) REFERENCES ${DiseaseTable.TABLE_NAME})")
    }

    fun insert(values: ContentValues): Long {
        return db.insert(TABLE_NAME, null, values)
    }

    fun update(values: ContentValues, whereClause: String, whereArgs: Array<String>): Int {
        return db.update(TABLE_NAME, values, whereClause, whereArgs)
    }

    fun delete(whereClause: String, whereArgs: Array<String>): Int {
        return db.delete(TABLE_NAME, whereClause, whereArgs)
    }

    fun query(
        columns: Array<String>,
        selection: String?,
        selectionArgs: Array<String>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): Cursor? {
        val lastColumn = columns.size - 1

        var colNameDisease = -1
        for(i in 0..lastColumn){
            if (columns[i] == FIELD_EXTERN_DISEASE_NAME) {
                colNameDisease = i
                break
            }
        }

        if (colNameDisease == -1){
            return db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy)
        }

        var tableColumns = ""
        for (i in 0..lastColumn){
            if (i > 0) tableColumns += ","

            tableColumns += if (i == colNameDisease){
                "${DiseaseTable.TABLE_NAME}.${DiseaseTable.FIELD_NAME} AS $FIELD_EXTERN_DISEASE_NAME"
            } else {
                "${TABLE_NAME}.${columns[i]}"
            }
        }

        val tables = "$TABLE_NAME INNER JOIN ${DiseaseTable.TABLE_NAME} ON ${DiseaseTable.TABLE_NAME}.${BaseColumns._ID}=$FIELD_ID_DISEASE"

        var sql = "SELECT $tableColumns FROM $tables"

        if (selection != null) sql += " WHERE $selection"

        if (groupBy != null){
            sql += " GROUP BY $groupBy"
            if (having != null) " HAVING $having"
        }

        if (orderBy != null) sql += " ORDER BY $orderBy"

        return db.rawQuery(sql, selectionArgs)
    }

    companion object{
        const val TABLE_NAME = "patient"
        const val FIELD_IDENTIFICATION = "identification"
        const val FIELD_NAME = "name"
        const val FIELD_PRIORITY = "priority"
        const val FIELD_ID_HOSPITAL = "id_hospital"
        const val FIELD_ID_DISEASE = "id_disease"
        const val FIELD_EXTERN_DISEASE_NAME = "d_name_disease"

        val TODAS_COLUNAS = arrayOf(BaseColumns._ID,
            FIELD_IDENTIFICATION,
            FIELD_NAME,
            FIELD_PRIORITY,
            FIELD_ID_HOSPITAL,
            FIELD_ID_DISEASE,
            FIELD_EXTERN_DISEASE_NAME
        )
    }
}