package vitor.treino.covid_project

import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class DBTest {
    private fun getAppContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun getDbHelper() = DBHelper(getAppContext())

    private fun insertHospital(table: HospitalTable, hospital: HospitalData): Long {
        val id = table.insert(hospital.toContentValues())
        assertNotEquals(-1, id)

        return id
    }

    private fun getHospitalBD(table: HospitalTable, id: Long): HospitalData {
        val cursor = table.query(
            HospitalTable.TODAS_COLUNAS,
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
        getAppContext().deleteDatabase(DBHelper.DB_Name)
    }

    @Test
    fun testOpenBD() {
        val db = getDbHelper().readableDatabase

        assert(db.isOpen)
        db.close()
    }

    //TODO: Testes CRUD à tabela Hospital

    @Test
    fun testHospitalInsert() {
        val db = getDbHelper().writableDatabase
        val hospitalTable = HospitalTable(db)

        val hospital = HospitalData(name = "São Pedro", location = "Lisboa", address = "Avenida XXX", state = "FULL", infected = 123, recovered = 321)
        hospital.id = insertHospital(hospitalTable, hospital)

        assertEquals(hospital, getHospitalBD(hospitalTable, hospital.id))

        db.close()
    }

    @Test
    fun testHospitalUpdate() {
        val db = getDbHelper().writableDatabase
        val hospitalTable = HospitalTable(db)

        val hospital = HospitalData(name = "Mateus", location = "Porto", address = "?", state = "?", infected = 456, recovered = 654)
        hospital.id = insertHospital(hospitalTable, hospital)

        hospital.address = "Avenida YYY"
        hospital.state = "EMPTY"

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
        val db = getDbHelper().writableDatabase
        val hospitalTable = HospitalTable(db)

        val hospital = HospitalData(name = "S. Antonio", location = "Mafra", address = "Rua Grande", state = "FULL", infected = 789, recovered = 987)
        hospital.id = insertHospital(hospitalTable, hospital)

        val deletedData = hospitalTable.delete(
            "${BaseColumns._ID}=?",
            arrayOf(hospital.id.toString())
        )

        assertEquals(1, deletedData)

        db.close()
    }

    @Test
    fun testHospitalRead() {
        val db = getDbHelper().writableDatabase
        val hospitalTable = HospitalTable(db)

        val hospital = HospitalData(name = "São João", location = "Alentejo", address = "Bairro das Pinhas", state = "EMPTY", infected = 0, recovered = 100)
        hospital.id = insertHospital(hospitalTable, hospital)

        assertEquals(hospital, getHospitalBD(hospitalTable, hospital.id))

        db.close()
    }
}