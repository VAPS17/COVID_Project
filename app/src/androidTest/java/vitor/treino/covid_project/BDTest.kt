package vitor.treino.covid_project

import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BDTest {
    private fun getAppContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun getBdHelper() = BDHelper(getAppContext())

    private fun insertHospital(table: HospitalTable, hospital: HospitalData): Long {
        val id = table.insert(hospital.toContentValues())
        assertNotEquals(-1, id)

        return id
    }

    private fun getHospitalBD(table: HospitalTable, id: Long): HospitalData {
        val cursor = table.query(
            HospitalTable.TODA_COLUNAS,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return HospitalData.fromCursor(cursor)
    }

    @Before
    fun eraseBD() {
        getAppContext().deleteDatabase(BDHelper.DB_Name)
    }

    @Test
    fun testOpenBD() {
        val db = getBdHelper().readableDatabase

        assert(db.isOpen)
        db.close()
    }

    @Test
    fun testHospitalInsert() {
        val db = getBdHelper().writableDatabase
        val hospitalTable = HospitalTable(db)

        val hospital = HospitalData(name = "São Pedro", location = "Lisboa", address = "Avenida XXX", state = "Full")
        hospital.id = insertHospital(hospitalTable, hospital)

        assertEquals(hospital, getHospitalBD(hospitalTable, hospital.id))

        db.close()
    }

    @Test
    fun testHospitalUpdate() {
        val db = getBdHelper().writableDatabase
        val hospitalTable = HospitalTable(db)

        val hospital = HospitalData(name = "São Pedro", location = "Lisboa", address = "Avenida XXX", state = "Full")
        hospital.id = insertHospital(hospitalTable, hospital)

        hospital.address = "Avenida YYY"
        hospital.state = "Empty"

        val updatedData = hospitalTable.update(
            hospital.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(hospital.id.toString())
        )

        assertEquals(1, updatedData)

        assertEquals(hospital, getHospitalBD(hospitalTable, hospital.id))

        db.close()
    }

    @Test
    fun testHospitalDelete() {
        val db = getBdHelper().writableDatabase
        val hospitalTable = HospitalTable(db)

        val hospital = HospitalData(name = "São Pedro", location = "Lisboa", address = "Avenida XXX", state = "Full")
        hospital.id = insertHospital(hospitalTable, hospital)

        val deletedData = hospitalTable.delete(
            "${BaseColumns._ID}=?",
            arrayOf(hospital.id.toString())
        )

        assertEquals(1, deletedData)

        db.close()
    }
}