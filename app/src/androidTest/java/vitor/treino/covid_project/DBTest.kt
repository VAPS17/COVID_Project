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

    private fun insertSatff(table: StaffTable, staff: StaffData): Long {
        val id = table.insert(staff.toContentValues())
        assertNotEquals(-1, id)

        return id
    }

    private fun getHospitalDB(table: HospitalTable, id: Long): HospitalData {
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

    private fun getStaffDB(table: StaffTable, id: Long): StaffData {
        val cursor = table.query(
            StaffTable.TODAS_COLUNAS,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return StaffData.fromCursor(cursor)
    }

    @Before
    fun eraseDB() {
        //getAppContext().deleteDatabase(DBHelper.DB_Name)
    }

    @Test
    fun testOpenDB() {
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

        assertEquals(hospital, getHospitalDB(hospitalTable, hospital.id))

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

        assertEquals(hospital, getHospitalDB(hospitalTable, hospital.id))

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

        assertEquals(hospital, getHospitalDB(hospitalTable, hospital.id))

        db.close()
    }

    //TODO: Testes CRUD à tabela Staff

    @Test
    fun testStaffInsert() {
        val db = getDbHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "São Pedro", location = "Lisboa", address = "Avenida XXX", state = "FULL", infected = 123, recovered = 321)
        hospital.id = insertHospital(hospitalTable, hospital)

        val staffTable = StaffTable(db)
        val staff = StaffData(identifcation = 12345, phone = 966666666, name = "Bruno", idHospital = hospital.id)
        staff.id = insertSatff(staffTable, staff)

        assertEquals(staff, getStaffDB(staffTable, staff.id))

        db.close()
    }

    @Test
    fun testStaffUpdate() {
        val db = getDbHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "Mateus", location = "Porto", address = "?", state = "?", infected = 456, recovered = 654)
        hospital.id = insertHospital(hospitalTable, hospital)

        val staffTable = StaffTable(db)
        val staff = StaffData(identifcation = 12, phone = 967777777, name = "?", idHospital = hospital.id)
        staff.id = insertSatff(staffTable, staff)

        staff.identifcation = 67890
        staff.name = "Paula"

        val updatedData = staffTable.update(
            staff.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(staff.id.toString())
        )

        assertEquals(1, updatedData)

        assertEquals(staff, getStaffDB(staffTable, staff.id))

        db.close()
    }

    @Test
    fun testStaffDelete() {
        val db = getDbHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "S. Antonio", location = "Mafra", address = "Rua Grande", state = "FULL", infected = 789, recovered = 987)
        hospital.id = insertHospital(hospitalTable, hospital)

        val staffTable = StaffTable(db)
        val staff = StaffData(identifcation = 13579, phone = 968888888, name = "Carlos", idHospital = hospital.id)
        staff.id = insertSatff(staffTable, staff)

        val deletedData = staffTable.delete(
            "${BaseColumns._ID}=?",
            arrayOf(staff.id.toString())
        )

        assertEquals(1, deletedData)

        db.close()
    }

    @Test
    fun testStaffRead() {
        val db = getDbHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "São João", location = "Alentejo", address = "Bairro das Pinhas", state = "EMPTY", infected = 0, recovered = 100)
        hospital.id = insertHospital(hospitalTable, hospital)

        val staffTable = StaffTable(db)
        val staff = StaffData(identifcation = 24680, phone = 969999999, name = "Joana", idHospital = hospital.id)
        staff.id = insertSatff(staffTable, staff)

        assertEquals(staff, getStaffDB(staffTable, staff.id))

        db.close()
    }
}