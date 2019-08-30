package com.example.moodapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegistroEmocion(
    val fechaRegistro: String = "",
    val horaRegistro: String = "",
    val emocionNombre: String = "",
    val emocionImagenUrl: String = "",
    val emocionSeveridad: Long = 0,
    var documentoId:String? = null,
    var actividad: String? = null,
    var estado:String? = null,
    var actividadId:String? = null

) : Parcelable