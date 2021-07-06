package vitor.treino.covid_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vitor.treino.covid_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var menu: Menu

    var currentMenu = R.menu.menu_hospital
        set(value){
            field = value
            invalidateOptionsMenu()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        AppData.activity = this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(currentMenu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val optionProcessed = when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "COVID V.1.0", Toast.LENGTH_LONG).show()
                true
            }
            else -> when (currentMenu){
                R.menu.menu_hospital -> (AppData.fragment as HospitalFragment).optionMenuProcessingH(item)
                R.menu.menu_staff -> (AppData.fragment as StaffFragment).optionMenuProcessingS(item)
                else -> false
            }
        }
        return if(optionProcessed) true else super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun updateEditDeleteHospital(showEditDelete: Boolean){
        val editHospital = findViewById<FloatingActionButton>(R.id.editHospital)
        val deleteHospital = findViewById<FloatingActionButton>(R.id.deleteHospital)

        if (showEditDelete) {
            editHospital.show()
            deleteHospital.show()
        } else {
            editHospital.hide()
            deleteHospital.hide()
        }
    }

    fun updateEditDeleteStaff(showEditDelete: Boolean){
        val editStaff = findViewById<FloatingActionButton>(R.id.editStaff)
        val deleteStaff = findViewById<FloatingActionButton>(R.id.deleteStaff)

        if (showEditDelete) {
            editStaff.show()
            deleteStaff.show()
        } else {
            editStaff.hide()
            deleteStaff.hide()
        }
    }
}
