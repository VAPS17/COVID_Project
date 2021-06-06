package vitor.treino.covid_project

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class ContentProviderCovid : ContentProvider() {
    private var bdHelper : BDHelper? = null

    override fun onCreate(): Boolean {
        bdHelper = BDHelper(context)

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val bd = bdHelper!!.readableDatabase

        return when (getUriMatcher().match(uri)) {

        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
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
    }
}