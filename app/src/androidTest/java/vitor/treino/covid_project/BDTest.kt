package vitor.treino.covid_project

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

    @Test
    fun openBD() {
        val dbHelper = BDHelper(getAppContext())
        val db = dbHelper.readableDatabase

        assert(db.isOpen)
        db.close()
    }

    @Before
    fun eraseBD() {
        getAppContext().deleteDatabase(BDHelper.DB_Name)
    }
}