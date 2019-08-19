package com.example.moodapp.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodapp.Adapters.HistorialAdapter
import com.example.moodapp.Models.RegistroEmocion
import com.example.moodapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistorialEmocionesFragment : Fragment() {

    private lateinit var historialRegistros:RecyclerView
    private lateinit var db:FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private val registrosEmociones = mutableListOf<RegistroEmocion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historial_emociones, container, false)
        historialRegistros = view.findViewById(R.id.historial_registros)
        iniciarRVHistorial()
        return view
    }

    fun iniciarRVHistorial(){
        historialRegistros.layoutManager = LinearLayoutManager(this.context,RecyclerView.VERTICAL,false)
        getData(auth.currentUser!!.uid)
        historialRegistros.adapter = HistorialAdapter(registrosEmociones)
    }

    fun getData(userId: String) {
        val registrosRef = db
            .collection(resources.getString(R.string.coleccion_usuario))
            .document(userId)
            .collection(resources.getString(R.string.coleccion_registro))
        registrosRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                return@addSnapshotListener
            }
            for (doc in snapshot!!.documentChanges){
                when(doc.type){
                    DocumentChange.Type.ADDED -> {
                        val registroEmocion = RegistroEmocion(
                            fechaRegistro = doc.document.getString("fechaRegistro")!!,
                            horaRegistro = doc.document.getString("horaRegistro")!!,
                            emocionNombre = doc.document.getString("emocionNombre")!!,
                            emocionImagenUrl = doc.document.getString("emocionImagenUrl")!!,
                            actividad = doc.document.getString("actividad"),
                            emocionSeveridad = doc.document.getLong("emocionSeveridad")!!
                        )
                        registrosEmociones.add(registroEmocion)
                        historialRegistros.adapter!!.notifyDataSetChanged()
                    }
                    else -> return@addSnapshotListener
                }
            }
        }
    }


}
