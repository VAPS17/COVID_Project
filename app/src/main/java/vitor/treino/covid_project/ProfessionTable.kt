package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class ProfessionTable(db:SQLiteDatabase) {
    private val db: SQLiteDatabase = db

    fun create() {
        db.execSQL("CREATE TABLE $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, $FIELD_NAME TEXT NOT NULL)")
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
        const val TABLE_NAME = "profession"
        const val FIELD_NAME = "name"

        val TODAS_COLUNAS = arrayOf(BaseColumns._ID, FIELD_NAME)
    }

}