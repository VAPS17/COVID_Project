package vitor.treino.covid_project

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BD_Helper(context: Context?) : SQLiteOpenHelper(context, DB_Name, null, DB_Version){

    override fun onCreate(db: SQLiteDatabase?){
        if (db != null) {

        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object{
        const val DB_Name = "COVID_Project"
        const val DB_Version = 1
    }
}
