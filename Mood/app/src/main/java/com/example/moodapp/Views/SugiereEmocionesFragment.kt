package com.example.moodapp.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.moodapp.R
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SugiereEmocionesFragment : Fragment(), View.OnClickListener {
    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.guardar_sugerencia -> GuardarNuevaSugerencia()
        }
    }

    private fun GuardarNuevaSugerencia() {
        val sugerenciaRef = db.collection(resources.getString(R.string.coleccion_sugerencia))
    }

    private lateinit var guardarSugerencia:Button
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
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
}
