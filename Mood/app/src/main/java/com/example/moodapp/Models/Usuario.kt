package com.example.moodapp.Models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Usuario(val username: String,
                   val email: String,
                   @ServerTimestamp val createdAt: Date?,
                   val usuario_id: String)