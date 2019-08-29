package com.example.moodapp.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.moodapp.R
import com.example.moodapp.models.RegistroEmocion

class CalificarActividadDialogFragment: DialogFragment() {

    private lateinit var registroEmocion:RegistroEmocion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registroEmocion = arguments!!.getParcelable("registroEmocion")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root:View = inflater.inflate(R.layout.dialog_califica_actividad,container,false)
        Log.e("nombreActividad",registroEmocion.actividad!!)
        return root
    }

}