package vitor.treino.covid_project

import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class DBTest {
    private fun getAppContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun getDbHelper() = DBHelper(getAppContext())

    private fun insertHospital(table: HospitalTable, hospital: HospitalData): Long {
        val id = table.insert(hospital.toContentValues())
        assertNotEquals(-1, id)

        return id
    }

    private fun insertProfession(table: ProfessionTable, profession: ProfessionData): Long{
        val id = table.insert(profession.toContentValues())
        assertNotEquals(-1,id)

        return id
    }

    private fun insertStaff(table: StaffTable, staff: StaffData): Long {
        val id = table.insert(staff.toContentValues())
        assertNotEquals(-1, id)

        return id
    }

    private fun insertDisease(table: DiseaseTable, disease: DiseaseData): Long{
        val id = table.insert(disease.toContentValues())
        assertNotEquals(-1,id)

        return id
    }

    private fun insertPatient(table: PatientTable, patient: PatientData): Long{
        val id = table.insert(patient.toContentValues())
        assertNotEquals(-1,id)

        return id
    }

    private fun getHospitalDB(table: HospitalTable, id: Long): HospitalData {
        val cursor = table.query(
            HospitalTable.TODAS_COLUNAS,
            "${HospitalTable.TABLE_NAME}.${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return HospitalData.fromCursor(cursor)
    }

    private fun getProfessionDB(table: ProfessionTable, id: Long): ProfessionData {
        val cursor = table.query(
            ProfessionTable.TODAS_COLUNAS,
            "${ProfessionTable.TABLE_NAME}.${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return ProfessionData.fromCursor(cursor)
    }

    private fun getStaffDB(table: StaffTable, id: Long): StaffData {
        val cursor = table.query(
            StaffTable.TODAS_COLUNAS,
            "${StaffTable.TABLE_NAME}.${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return StaffData.fromCursor(cursor)
    }

    private fun getDiseaseDB(table: DiseaseTable, id: Long): DiseaseData {
        val cursor = table.query(
            DiseaseTable.TODAS_COLUNAS,
            "${DiseaseTable.TABLE_NAME}.${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return DiseaseData.fromCursor(cursor)
    }

    private fun getPatientDB(table: PatientTable, id: Long): PatientData {
        val cursor = table.query(
            PatientTable.TODAS_COLUNAS,
            "${PatientTable.TABLE_NAME}.${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return PatientData.fromCursor(cursor)
    }

    @After
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
        val hospital = HospitalData(name = "São Pedro",
            location = "Lisboa",
            address = "Avenida XXX",
            state = "FULL",
            infected = 123,
            recovered = 321)
        hospital.id = insertHospital(hospitalTable, hospital)

        assertEquals(hospital, getHospitalDB(hospitalTable, hospital.id))

        db.close()
    }

    @Test
    fun testHospitalUpdate() {
        val db = getDbHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "Mateus",
            location = "Porto",
            address = "teste",
            state = "",
            infected = 456,
            recovered = 654)
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
        val hospital = HospitalData(name = "S. Antonio",
            location = "Mafra",
            address = "Rua Grande",
            state = "FULL",
            infected = 789,
            recovered = 987)
        hospital.id = insertHospital(hospitalTable, hospital)

        val deletedData = hospitalTable.delete(
            "${BaseColumns._ID}=?",
            arrayOf(hospital.id.toString())
        )

        assertEquals(1, deletedData)

        db.close()
    }

    //TODO: Testes CRUD à tabela Staff

    @Test
    fun testStaffInsert() {
        val db = getDbHelper().writableDatabase


        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "teste",
            location = "teste",
            address = "teste",
            state = "FULL",
            infected = 123,
            recovered = 321)
        hospital.id = insertHospital(hospitalTable, hospital)

        val professionTable = ProfessionTable(db)
        val profession = ProfessionData(name = "Director")
        profession.id = insertProfession(professionTable, profession)


        val staffTable = StaffTable(db)
        val staff = StaffData(
            identifcation = 11111,
            phone = 961111111,
            name = "Pedro",
            idHospital = hospital.id,
            idProfession = profession.id,
            nameProfession = profession.name)
        staff.id = insertStaff(staffTable, staff)

        assertEquals(staff, getStaffDB(staffTable, staff.id))

        db.close()
    }

    @Test
    fun testStaffUpdate() {
        val db = getDbHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "Mateus",
            location = "Porto",
            address = "?",
            state = "?",
            infected = 456,
            recovered = 654)
        hospital.id = insertHospital(hospitalTable, hospital)

        val professionTable = ProfessionTable(db)
        val profession = ProfessionData(name = "Teste3")
        profession.id = insertProfession(professionTable, profession)


        val staffTable = StaffTable(db)
        val staff = StaffData(
            identifcation = 12,
            phone = 967777777,
            name = "?",
            idHospital = hospital.id,
            idProfession = profession.id,
            nameProfession = profession.name
        )
        staff.id = insertStaff(staffTable, staff)

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
        val hospital = HospitalData(name = "S. Antonio",
            location = "Mafra",
            address = "Rua Grande",
            state = "FULL",
            infected = 789,
            recovered = 987)
        hospital.id = insertHospital(hospitalTable, hospital)

        val professionTable = ProfessionTable(db)
        val profession = ProfessionData(name = "Teste3")
        profession.id = insertProfession(professionTable, profession)


        val staffTable = StaffTable(db)
        val staff = StaffData(
            identifcation = 13579,
            phone = 968888888,
            name = "Carlos",
            idHospital = hospital.id,
            idProfession = profession.id,
            nameProfession = profession.name
        )
        staff.id = insertStaff(staffTable, staff)

        val deletedData = staffTable.delete(
            "${BaseColumns._ID}=?",
            arrayOf(staff.id.toString())
        )

        assertEquals(1, deletedData)

        db.close()
    }

    //TODO: Testes CRUD à tabela Patient

    @Test
    fun testPatientInsert() {
        val db = getDbHelper().writableDatabase


        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "teste",
            location = "teste",
            address = "teste",
            state = "FULL",
            infected = 123,
            recovered = 321)
        hospital.id = insertHospital(hospitalTable, hospital)

        val diseaseTable = DiseaseTable(db)
        val disease = DiseaseData(name = "TesteD")
        disease.id = insertDisease(diseaseTable, disease)


        val patientTable = PatientTable(db)
        val patient = PatientData(
            identifcation = 11111,
            name = "Pedro Santos",
            priority = "LOW",
            idHospital = hospital.id,
            idDisease = disease.id,
            nameDisease = disease.name)
        patient.id = insertPatient(patientTable, patient)

        assertEquals(patient, getPatientDB(patientTable, patient.id))

        db.close()
    }

    @Test
    fun testPatientUpdate() {
        val db = getDbHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "Mateus",
            location = "Porto",
            address = "?",
            state = "?",
            infected = 456,
            recovered = 654)
        hospital.id = insertHospital(hospitalTable, hospital)

        val diseaseTable = DiseaseTable(db)
        val disease = DiseaseData(name = "TesteB")
        disease.id = insertDisease(diseaseTable, disease)


        val patientTable = PatientTable(db)
        val patient = PatientData(
            identifcation = 22222,
            name = "Andre Santos",
            priority = "MEDIUM",
            idHospital = hospital.id,
            idDisease = disease.id,
            nameDisease = disease.name)
        patient.id = insertPatient(patientTable, patient)

        patient.identifcation = 67890
        patient.name = "André Santos"

        val updatedData = patientTable.update(
            patient.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(patient.id.toString())
        )

        assertEquals(1, updatedData)

        assertEquals(patient, getPatientDB(patientTable, patient.id))

        db.close()
    }

    @Test
    fun testPatientDelete() {
        val db = getDbHelper().writableDatabase

        val hospitalTable = HospitalTable(db)
        val hospital = HospitalData(name = "S. Antonio",
            location = "Mafra",
            address = "Rua Grande",
            state = "FULL",
            infected = 789,
            recovered = 987)
        hospital.id = insertHospital(hospitalTable, hospital)

        val diseaseTable = DiseaseTable(db)
        val disease = DiseaseData(name = "TesteF")
        disease.id = insertDisease(diseaseTable, disease)


        val patientTable = PatientTable(db)
        val patient = PatientData(
            identifcation = 44444,
            name = "Mateus Santos",
            priority = "HIGH",
            idHospital = hospital.id,
            idDisease = disease.id,
            nameDisease = disease.name)
        patient.id = insertPatient(patientTable, patient)

        val deletedData = patientTable.delete(
            "${BaseColumns._ID}=?",
            arrayOf(patient.id.toString())
        )

        assertEquals(1, deletedData)

        db.close()
    }

    //TODO: Preencher a tabela "profession"

    @Test
    fun testProfessionInsert() {
        val db = getDbHelper().writableDatabase
        val professionTable = ProfessionTable(db)

        val profession1 = ProfessionData(name = "Director")
        profession1.id = insertProfession(professionTable, profession1)

        val profession2 = ProfessionData(name = "Doctor")
        profession2.id = insertProfession(professionTable, profession2)

        val profession3 = ProfessionData(name = "Nurse")
        profession3.id = insertProfession(professionTable, profession3)

        val profession4 = ProfessionData(name = "Employee")
        profession4.id = insertProfession(professionTable, profession4)

        assertEquals(profession1, getProfessionDB(professionTable, profession1.id))
        assertEquals(profession2, getProfessionDB(professionTable, profession2.id))
        assertEquals(profession3, getProfessionDB(professionTable, profession3.id))
        assertEquals(profession4, getProfessionDB(professionTable, profession4.id))

        db.close()
    }

    //TODO: Preencher a tabela "disease"

    @Test
    fun testDiseaseInsert() {
        val db = getDbHelper().writableDatabase
        val diseaseTable = DiseaseTable(db)

        val disease1 = DiseaseData(name = "COVID-19")
        disease1.id = insertDisease(diseaseTable, disease1)

        val disease2 = DiseaseData(name = "SIDA")
        disease2.id = insertDisease(diseaseTable, disease2)

        val disease3 = DiseaseData(name = "HEPATITE-B")
        disease3.id = insertDisease(diseaseTable, disease3)

        val disease4 = DiseaseData(name = "OUTROS")
        disease4.id = insertDisease(diseaseTable, disease4)

        assertEquals(disease1, getDiseaseDB(diseaseTable, disease1.id))
        assertEquals(disease2, getDiseaseDB(diseaseTable, disease2.id))
        assertEquals(disease3, getDiseaseDB(diseaseTable, disease3.id))
        assertEquals(disease4, getDiseaseDB(diseaseTable, disease4.id))

        db.close()
    }
}