package com.gmail.eamosse.imdb

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gmail.eamosse.idbdata.repository.MovieRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Activité principale de l'application
 * Ce sera la seule activité de l'application
 */
class MainActivity : AppCompatActivity() {

    private val repository: MovieRepository by inject()
    private lateinit var navController: NavController

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavController()
        GlobalScope.launch {
            Log.d("TOKEN", repository.getToken().toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController = findNavController(R.id.nav_host_fragment)
        val navigated = NavigationUI.onNavDestinationSelected(item, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    /**
     * Méthode utilitaire permettant de gérer la navigation
     */
    private fun initNavController() {
        // Instance de la bottom navigation
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // Navigation controlleur, utilisée pour géter la navigation (ex. affichage de fragment)
        navController = findNavController(R.id.nav_host_fragment)
        // Charger les éléments principaux de la bottom bar
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_discover
            )
        )
        // Indiquer les éléments principaux de la bottom bar
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Finalement, on lie la bottom bar et la nav controller
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}
