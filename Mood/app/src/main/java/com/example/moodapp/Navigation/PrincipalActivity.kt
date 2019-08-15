package com.example.moodapp.Navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moodapp.R
import com.google.firebase.auth.FirebaseAuth

class PrincipalActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        auth = FirebaseAuth.getInstance()
        auth.signOut()
    }
}
