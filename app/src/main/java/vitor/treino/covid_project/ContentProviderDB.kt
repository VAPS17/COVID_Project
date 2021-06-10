package vitor.treino.covid_project

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class ContentProviderDB : ContentProvider() {
    private var dbHelper : DBHelper? = null

    override fun onCreate(): Boolean {
        dbHelper = DBHelper(context)

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper!!.readableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_HOSPITAL -> HospitalTable(db).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>,
                null,
                null,
                sortOrder
            )

            URI_HOSPITAL_SPECIFIC -> HospitalTable(db).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!),
                null,
                null,
                null
            )

            URI_STAFF -> StaffTable(db).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>,
                null,
                null,
                sortOrder
            )

            URI_STAFF_SPECIFIC -> StaffTable(db).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!),
                null,
                null,
                null
            )

            URI_PATIENT -> PatientTable(db).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>,
                null,
                null,
                sortOrder
            )

            URI_PATIENT_SPECIFIC -> PatientTable(db).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!),
                null,
                null,
                null
            )

            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return when (getUriMatcher().match(uri)) {
            URI_HOSPITAL -> "$MULTIPLOS_ITEMS/$HOSPITAL"
            URI_HOSPITAL_SPECIFIC -> "$UNICO_ITEM/$HOSPITAL"
            URI_STAFF -> "$MULTIPLOS_ITEMS/$STAFF"
            URI_STAFF_SPECIFIC -> "$UNICO_ITEM/$STAFF"
            URI_PATIENT -> "$MULTIPLOS_ITEMS/$PATIENT"
            URI_PATIENT_SPECIFIC -> "$UNICO_ITEM/$PATIENT"

            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper!!.writableDatabase

        val id = when (getUriMatcher().match(uri)) {
            URI_HOSPITAL -> HospitalTable(db).insert(values!!)
            URI_STAFF -> StaffTable(db).insert(values!!)
            URI_PATIENT -> PatientTable(db).insert(values!!)

            else -> 1L
        }

        if (id == 1L) return null

        return Uri.withAppendedPath(uri, id.toString())
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val db = dbHelper!!.writableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_HOSPITAL_SPECIFIC -> HospitalTable(db).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_STAFF_SPECIFIC -> StaffTable(db).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_PATIENT_SPECIFIC -> PatientTable(db).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            else -> 0
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper!!.writableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_HOSPITAL_SPECIFIC -> HospitalTable(db).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_STAFF_SPECIFIC -> StaffTable(db).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_PATIENT_SPECIFIC -> PatientTable(db).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            else -> 0
        }
    }

    private fun getUriMatcher(): UriMatcher {
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        uriMatcher.addURI(AUTHORITY, HOSPITAL, URI_HOSPITAL)
        uriMatcher.addURI(AUTHORITY, "$HOSPITAL/#", URI_HOSPITAL_SPECIFIC)

        uriMatcher.addURI(AUTHORITY, STAFF, URI_STAFF)
        uriMatcher.addURI(AUTHORITY, "$STAFF/#", URI_STAFF_SPECIFIC)

        uriMatcher.addURI(AUTHORITY, PATIENT, URI_PATIENT)
        uriMatcher.addURI(AUTHORITY, "$PATIENT/#", URI_PATIENT_SPECIFIC)

        return uriMatcher
    }

    companion object {
        private const val AUTHORITY = "vitor.treino.covid_project"

        private const val HOSPITAL = "hospital"
        private const val STAFF = "staff"
        private const val PATIENT = "patient"

        private const val URI_HOSPITAL = 100
        private const val URI_HOSPITAL_SPECIFIC = 101
        private const val URI_STAFF = 200
        private const val URI_STAFF_SPECIFIC = 201
        private const val URI_PATIENT = 300
        private const val URI_PATIENT_SPECIFIC = 301

        private val ENDERECO_BASE = Uri.parse("content//$AUTHORITY")
        public val ENDERECO_HOSPITAL = Uri.withAppendedPath(ENDERECO_BASE, HOSPITAL)

        private const val MULTIPLOS_ITEMS = "vnd.android.cursor.dir"
        private const val UNICO_ITEM = "vnd.android.cursor.item"
    }
}