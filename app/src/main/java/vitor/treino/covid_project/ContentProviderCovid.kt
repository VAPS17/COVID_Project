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

            URI_PROFESSION -> ProfessionTable(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>?,
                null,
                null,
                sortOrder
            )

            URI_PROFESSION_SPECIFIC -> ProfessionTable(bd).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!),
                null,
                null,
                null
            )

            URI_STAFF -> StaffTable(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>?,
                null,
                null,
                sortOrder
            )

            URI_STAFF_SPECIFIC -> StaffTable(bd).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!),
                null,
                null,
                null
            )

            URI_DISEASE -> DiseaseTable(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>?,
                null,
                null,
                sortOrder
            )

            URI_DISEASE_SPECIFIC -> DiseaseTable(bd).query(
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
            URI_PROFESSION -> "$MULTIPLOS_ITEMS/$PROFESSION"
            URI_PROFESSION_SPECIFIC -> "$UNICO_ITEM/$PROFESSION"
            URI_STAFF -> "$MULTIPLOS_ITEMS/$STAFF"
            URI_STAFF_SPECIFIC -> "$UNICO_ITEM/$STAFF"
            URI_DISEASE -> "$MULTIPLOS_ITEMS/$DISEASE"
            URI_DISEASE_SPECIFIC -> "$UNICO_ITEM/$DISEASE"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val bd = dbHelper!!.writableDatabase

        val id = when (getUriMatcher().match(uri)) {
            URI_HOSPITAL -> HospitalTable(bd).insert(values!!)
            URI_PROFESSION -> ProfessionTable(bd).insert(values!!)
            URI_STAFF -> StaffTable(bd).insert(values!!)
            URI_DISEASE -> DiseaseTable(bd).insert(values!!)
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

            URI_PROFESSION_SPECIFIC -> ProfessionTable(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_STAFF_SPECIFIC -> StaffTable(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_DISEASE_SPECIFIC -> DiseaseTable(bd).delete(
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

            URI_PROFESSION_SPECIFIC -> ProfessionTable(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_STAFF_SPECIFIC -> StaffTable(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_DISEASE_SPECIFIC -> DiseaseTable(bd).update(
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
        uriMatcher.addURI(AUTHORITY, PROFESSION, URI_PROFESSION)
        uriMatcher.addURI(AUTHORITY, "$PROFESSION/#", URI_PROFESSION_SPECIFIC)
        uriMatcher.addURI(AUTHORITY, STAFF, URI_STAFF)
        uriMatcher.addURI(AUTHORITY, "$STAFF/#", URI_STAFF_SPECIFIC)
        uriMatcher.addURI(AUTHORITY, DISEASE, URI_DISEASE)
        uriMatcher.addURI(AUTHORITY, "$DISEASE/#", URI_DISEASE_SPECIFIC)

        return uriMatcher
    }

    companion object {
        private const val AUTHORITY = "vitor.treino.covid_project"

        private const val HOSPITAL = "hospital"
        private const val STAFF = "staff"
        private const val PROFESSION = "profession"
        private const val DISEASE = "disease"

        private const val URI_HOSPITAL = 100
        private const val URI_HOSPITAL_SPECIFIC = 101
        private const val URI_PROFESSION = 200
        private const val URI_PROFESSION_SPECIFIC = 201
        private const val URI_STAFF = 300
        private const val URI_STAFF_SPECIFIC = 301
        private const val URI_DISEASE = 400
        private const val URI_DISEASE_SPECIFIC = 401

        private const val MULTIPLOS_ITEMS = "vnd.android.cursor.dir"
        private const val UNICO_ITEM = "vnd.android.cursor.item"

        private val ENDERECO_BASE = Uri.parse("content://$AUTHORITY")
        val ENDERECO_HOSPITAL = Uri.withAppendedPath(ENDERECO_BASE, HOSPITAL)
        val ENDERECO_PROFESSION = Uri.withAppendedPath(ENDERECO_BASE, PROFESSION)
        val ENDERECO_STAFF = Uri.withAppendedPath(ENDERECO_BASE, STAFF)
        val ENDERECO_DISEASE = Uri.withAppendedPath(ENDERECO_BASE, DISEASE)

    }
}