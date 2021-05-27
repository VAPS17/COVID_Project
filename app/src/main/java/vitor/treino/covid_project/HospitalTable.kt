package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class HospitalTable(db: SQLiteDatabase) {
    private val db: SQLiteDatabase = db

    fun create() {
        db.execSQL("CREATE TABLE $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, $FIELD_NAME TEXT NOT NULL, $FIELD_LOCATION TEXT NOT NULL, $FIELD_ADDRESS TEXT NOT NULL, $FIELD_STATE TEXT NOT NULL)")
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
        return db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy)
    }

    companion object{
        const val TABLE_NAME = "hospital"
        const val FIELD_NAME = "name"
        const val FIELD_LOCATION = "location"
        const val FIELD_ADDRESS = "address"
        const val FIELD_STATE = "state"

        val TODA_COLUNAS = arrayOf(BaseColumns._ID, FIELD_NAME, FIELD_LOCATION, FIELD_ADDRESS, FIELD_STATE)
    }
}