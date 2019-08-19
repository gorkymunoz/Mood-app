package com.example.moodapp.Navigation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.evernote.android.state.StateSaver
import com.example.moodapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import setupWithNavController

class PrincipalActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var principalBottonNavigation: BottomNavigationView
    private lateinit var auth:FirebaseAuth
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StateSaver.restoreInstanceState(this, savedInstanceState)
        setContentView(R.layout.activity_principal)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

/*
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        principalBottonNavigation = findViewById(R.id.principal_bottom_navigation)
        val navController = findNavController(R.id.principal_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        principalBottonNavigation.setupWithNavController(navController)*/
        auth = FirebaseAuth.getInstance()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        StateSaver.saveInstanceState(this, outState)
        setupBottomNavigationBar()
    }

/*
    override fun onSupportNavigateUp() =
        findNavController(R.id.principal_host_fragment).navigateUp()
*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        when(id){
            R.id.cerrar_sesion -> cerrarSesion()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun cerrarSesion() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.principal_bottom_navigation)

        val navGraphIds = listOf(R.navigation.registra, R.navigation.historial, R.navigation.sugiere)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }
}
