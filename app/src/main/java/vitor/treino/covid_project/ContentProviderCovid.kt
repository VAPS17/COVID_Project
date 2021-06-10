package vitor.treino.covid_project

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class ContentProviderCovid: ContentProvider() {
    private var dbHelper: DBHelper? = null

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
        val bd = dbHelper!!.readableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_HOSPITAL -> HospitalTable(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>?,
                null,
                null,
                sortOrder
            )

            URI_HOSPITAL_SPECIFIC -> HospitalTable(bd).query(
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
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val bd = dbHelper!!.writableDatabase

        val id = when (getUriMatcher().match(uri)) {
            URI_HOSPITAL -> HospitalTable(bd).insert(values!!)
            else -> -1L
        }

        if (id == -1L) return null

        return Uri.withAppendedPath(uri, id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val bd = dbHelper!!.writableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_HOSPITAL_SPECIFIC -> HospitalTable(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            else -> 0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val bd = dbHelper!!.writableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_HOSPITAL_SPECIFIC -> HospitalTable(bd).update(
                values!!,
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

        return uriMatcher
    }

    companion object {
        private const val AUTHORITY = "vitor.treino.covid_test"

        private const val HOSPITAL = "hospital"

        private const val URI_HOSPITAL = 100
        private const val URI_HOSPITAL_SPECIFIC = 101

        private const val MULTIPLOS_ITEMS = "vnd.android.cursor.dir"
        private const val UNICO_ITEM = "vnd.android.cursor.item"

        private val ENDERECO_BASE = Uri.parse("content://$AUTHORITY")
        public val ENDERECO_HOSPITAL = Uri.withAppendedPath(ENDERECO_BASE, HOSPITAL)

    }
}