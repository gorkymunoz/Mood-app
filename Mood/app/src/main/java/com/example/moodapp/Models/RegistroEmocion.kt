package com.example.moodapp.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class RegistroEmocion(
    val fechaRegistro: String,
    val horaRegistro: String,
    val emocionNombre: String,
    val emocionImagenUrl: String,
    val emocionSeveridad: Long,
    val actividad: String?
) : Parcelable