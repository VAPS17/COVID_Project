package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class  StaffTable(db: SQLiteDatabase){
    private val db: SQLiteDatabase = db

    fun create() {
        db.execSQL("CREATE TABLE $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, $FIELD_IDENTIFICATION NOT NULL UNIQUE, $FIELD_PHONE NOT NULL UNIQUE, $FIELD_NAME TEXT NOT NULL, $FIELD_ID_HOSPITAL INTEGER NOT NULL, $FIELD_ID_PROFESSION INTEGER NOT NULL, FOREIGN KEY($FIELD_ID_HOSPITAL) REFERENCES ${HospitalTable.TABLE_NAME}, FOREIGN KEY($FIELD_ID_PROFESSION) REFERENCES ${ProfessionTable.TABLE_NAME})")
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

        var colNameProfession = -1
        for(i in 0..lastColumn){
            if (columns[i] == FIELD_EXTERN_PROFESSION_NAME) {
                colNameProfession = i
                break
            }
        }

        if (colNameProfession == -1){
            return db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy)
        }

        var tableColumns = ""
        for (i in 0..lastColumn){
            if (i > 0) tableColumns += ","

            tableColumns += if (i == colNameProfession){
                "${ProfessionTable.TABLE_NAME}.${ProfessionTable.FIELD_NAME} AS $FIELD_EXTERN_PROFESSION_NAME"
            } else {
                "${TABLE_NAME}.${columns[i]}"
            }
        }

        val tables = "$TABLE_NAME INNER JOIN ${ProfessionTable.TABLE_NAME} ON ${ProfessionTable.TABLE_NAME}.${BaseColumns._ID}=$FIELD_ID_PROFESSION"

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
        const val TABLE_NAME = "staff"
        const val FIELD_IDENTIFICATION = "identification"
        const val FIELD_PHONE = "phone"
        const val FIELD_NAME = "name"
        const val FIELD_ID_HOSPITAL = "id_hospital"
        const val FIELD_ID_PROFESSION = "id_profession"
        const val FIELD_EXTERN_PROFESSION_NAME = "name_profession"

        val TODAS_COLUNAS = arrayOf(BaseColumns._ID,
            FIELD_IDENTIFICATION,
            FIELD_PHONE,
            FIELD_NAME,
            FIELD_ID_HOSPITAL,
            FIELD_ID_PROFESSION,
            FIELD_EXTERN_PROFESSION_NAME
        )
    }
}