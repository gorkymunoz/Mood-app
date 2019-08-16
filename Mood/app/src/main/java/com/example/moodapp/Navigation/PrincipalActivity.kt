package com.example.moodapp.Navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.moodapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class PrincipalActivity : AppCompatActivity() {

    private lateinit var principalBottonNavigation: BottomNavigationView
    private lateinit var auth:FirebaseAuth
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        principalBottonNavigation = findViewById(R.id.principal_bottom_navigation)
        val navController = findNavController(R.id.principal_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        principalBottonNavigation.setupWithNavController(navController)
        auth = FirebaseAuth.getInstance()
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.principal_host_fragment).navigateUp()

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

}
