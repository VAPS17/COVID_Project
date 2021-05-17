package vitor.treino.covid_project

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class PatientTable(db: SQLiteDatabase) {
    private val db: SQLiteDatabase = db

    fun create() {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FIELD_IDENTIFICATION + " TEXT NOT NULL UNIQUE," +
                FIELD_NAME + " TEXT NOT NULL," +
                FIELD_DISEASE + " TEXT NOT NULL," +
                FIELD_PRIORITY + " TEXT NOT NULL," +
                " FOREIGN KEY (" + FIELD_ID_STAFF + ")" +
                " REFERENCES " + StaffTable.TABLE_NAME + ")")
    }

    companion object{
        const val TABLE_NAME = "patient"
        const val FIELD_IDENTIFICATION = "identification"
        const val FIELD_NAME = "name"
        const val FIELD_DISEASE = "disease"
        const val FIELD_PRIORITY = "priority"
        const val FIELD_ID_STAFF = "id_staff"
    }
}