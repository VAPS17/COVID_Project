package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class StaffTable(db: SQLiteDatabase) {
    private val db : SQLiteDatabase = db

    fun create() {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FIELD_IDENTIFICATION + " TEXT NOT NULL UNIQUE," +
                FIELD_PROFESSION + " TEXT NOT NULL," +
                FIELD_NAME + " TEXT NOT NULL," +
                FIELD_N_PATIENTS + " TEXT NOT NULL," +
                " FOREIGN KEY (" + FIELD_ID_HOSPITAL + ")" +
                " REFERENCES " + HospitalTable.TABLE_NAME + ")")
    }

    fun insert(values: ContentValues): Long {
        return db.insert(TABLE_NAME, null, values)
    }

    fun update(values: ContentValues, id: String, whereClause: String, whereArgs: Array<String>): Int {
        return db.update(TABLE_NAME, values, whereClause, whereArgs)
    }

    fun delete(whereClause: String, whereArgs: Array<String>): Int {
        return db.delete(TABLE_NAME, whereClause, whereArgs)
    }

    fun query(
        columns: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        groupBy: String,
        having: String,
        orderBy: String
    ): Cursor? {
        return db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy)
    }

    companion object{
        const val TABLE_NAME = "staff"
        const val FIELD_IDENTIFICATION = "identification"
        const val FIELD_PROFESSION = "profession"
        const val FIELD_NAME = "name"
        const val FIELD_N_PATIENTS = "n_patients"
        const val FIELD_ID_HOSPITAL = "id_hospital"
    }
}