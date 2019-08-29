package com.example.moodapp.views

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.moodapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sugiere_emociones.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SugiereEmocionesFragment : Fragment(), View.OnClickListener {

    private lateinit var guardarSugerencia:Button
    private lateinit var db:FirebaseFirestore
    private lateinit var auth:FirebaseAuth

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.guardar_sugerencia ->
                GuardarNuevaSugerencia(
                    nombre_sugerencia.text.toString().trim(),
                    descripcion_sugerencia.text.toString().trim(),
                    auth.currentUser!!.uid
                )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sugiere_emociones, container, false)
        guardarSugerencia = view.findViewById(R.id.guardar_sugerencia)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        guardarSugerencia.setOnClickListener(this)
    }

    private fun GuardarNuevaSugerencia(nombreActividad:String,descripcionActividad:String,userId:String) {
        if(!validarCampos()){
            return
        }
        val sugerencia = hashMapOf(
            "nombre" to nombreActividad,
            "descripcion" to descripcionActividad,
            "userId" to userId
        )
        val sugerenciaRef = db
            .collection(resources.getString(R.string.coleccion_sugerencia))
        sugerenciaRef
            .document()
            .set(sugerencia)
            .addOnCompleteListener{
                task -> if (task.isSuccessful){
                actualizarUI()
                Toast.makeText(context, "Se guardó tu sugerencia", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
            }
    }

    fun actualizarUI(){
        nombre_sugerencia.text?.clear()
        descripcion_sugerencia.text?.clear()
    }

    fun validarCampos():Boolean{
        var valido = true
        val nombreActividad = nombre_sugerencia.text.toString().trim()
        if(TextUtils.isEmpty(nombreActividad)){
            layout_nombre_sugerencia.error = resources.getString(R.string.obligatorio)
            valido = false
        }else{
            layout_nombre_sugerencia.error = null
        }
        val descripcionActividad = descripcion_sugerencia.text.toString().trim()
        if(TextUtils.isEmpty(descripcionActividad)){
            layout_descripcion_sugerencia.error = resources.getString(R.string.obligatorio)
            valido = false
        }else{
            layout_descripcion_sugerencia.error = null
        }
        return valido
    }
}
