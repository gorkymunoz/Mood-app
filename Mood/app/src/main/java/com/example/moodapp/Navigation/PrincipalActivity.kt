package com.example.moodapp.Navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moodapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class PrincipalActivity : AppCompatActivity() {

    private lateinit var principalBottonNavigation: BottomNavigationView
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        principalBottonNavigation = findViewById(R.id.principal_bottom_navigation)
        val navController = findNavController(R.id.principal_host_fragment)
        principalBottonNavigation.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()
        //auth.signOut()
    }


}
