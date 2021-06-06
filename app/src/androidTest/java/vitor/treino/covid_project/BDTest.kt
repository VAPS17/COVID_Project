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

    private fun insertStaff(table: StaffTable, staff: StaffData): Long {
        val id = table.insert(staff.toContentValues())
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

    private fun getStaffBD(table: StaffTable, id: Long): StaffData {
        val cursor = table.query(
            StaffTable.TODA_COLUNAS,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return StaffData.fromCursor(cursor)
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

    //TODO: Hospital TableCRUD tests

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

    //TODO: Staff Table CRUD tests

    @Test
    fun testStaffInsert() {
        val db = getBdHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "São Pedro", location = "Lisboa", address = "Avenida XXX", state = "Full")
        hospital.id = insertHospital(hospitalTable, hospital)

        val staffTable = StaffTable(db)
        val staff = StaffData(identification = 123, profession = "Doctor", name = "Eduard", idHospital = hospital.id)
        staff.id = insertStaff(staffTable, staff)

        assertEquals(staff, getStaffBD(staffTable, staff.id))

        db.close()
    }

    @Test
    fun testStaffUpdate() {
        val db = getBdHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "São Pedro", location = "Lisboa", address = "Avenida XXX", state = "Full")
        hospital.id = insertHospital(hospitalTable, hospital)

        val staffTable = StaffTable(db)
        val staff = StaffData(identification = 121, profession = "Doctor", name = "Eduard", idHospital = hospital.id)
        staff.id = insertStaff(staffTable, staff)

        staff.identification = 333
        staff.name = "Victor"

        val updatedData = staffTable.update(
            staff.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(staff.id.toString())
        )

        assertEquals(1, updatedData)

        assertEquals(staff, getStaffBD(staffTable, staff.id))

        db.close()
    }

    @Test
    fun testStaffDelete() {
        val db = getBdHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "São Pedro", location = "Lisboa", address = "Avenida XXX", state = "Full")
        hospital.id = insertHospital(hospitalTable, hospital)

        val staffTable = StaffTable(db)
        val staff = StaffData(identification = 121, profession = "Doctor", name = "Eduard", idHospital = hospital.id)
        staff.id = insertStaff(staffTable, staff)

        val deletedData = staffTable.delete(
            "${BaseColumns._ID}=?",
            arrayOf(staff.id.toString())
        )

        assertEquals(1, deletedData)

        db.close()
    }
}