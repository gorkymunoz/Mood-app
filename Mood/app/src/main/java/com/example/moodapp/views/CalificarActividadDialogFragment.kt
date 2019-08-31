package com.example.moodapp.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.moodapp.R
import com.example.moodapp.models.RegistroEmocion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_califica_actividad.*

class CalificarActividadDialogFragment: DialogFragment(), View.OnClickListener {
    override fun onClick(view: View) {

        val id = view.id
        when(id){
            R.id.imagen_like -> calificarRegistro("likes")
            R.id.imagen_dislike -> calificarRegistro("dislikes")
        }
    }

    private lateinit var registroEmocion:RegistroEmocion
    private lateinit var calificacionLike:ImageView
    private lateinit var calificacionDislike:ImageView
    private lateinit var actividadRecomendada:TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registroEmocion = arguments!!.getParcelable("registroEmocion")!!
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root:View = inflater.inflate(R.layout.dialog_califica_actividad,container,false)
        actividadRecomendada = root.findViewById(R.id.actividad_recomendada)
        actividadRecomendada.text = registroEmocion.actividad
        calificacionDislike = root.findViewById(R.id.imagen_dislike)
        calificacionLike = root.findViewById(R.id.imagen_like)
        calificacionDislike.setOnClickListener(this)
        calificacionLike.setOnClickListener(this)
        return root
    }

    fun calificarRegistro(field: String) {

        val actividadRef = database.collection(resources.getString(R.string.coleccion_actividad))
            .document(registroEmocion.actividadId!!)
        val registroRef = database.collection(resources.getString(R.string.coleccion_usuario))
            .document(auth.currentUser!!.uid)
            .collection(resources.getString(R.string.coleccion_registro))
            .document(registroEmocion.documentoId!!)
        val batch = database.batch()
        batch.update(actividadRef, field, FieldValue.increment(1))
        batch.update(registroRef, "estado", "Calificado")
        batch.commit().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this.context, "Ya puedes seguir registrando", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(
                    context,
                    "Existió un error ${task.exception.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}