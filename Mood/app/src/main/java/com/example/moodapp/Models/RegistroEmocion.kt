package com.example.moodapp.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegistroEmocion(
    val fechaRegistro: String,
    val horaRegistro: String,
    val emocionNombre: String,
    val emocionImagenUrl: String,
    val emocionSeveridad: Long,
    var actividad: String?
) : Parcelable