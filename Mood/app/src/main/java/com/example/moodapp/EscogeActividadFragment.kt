package com.example.moodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.moodapp.models.Actividad
import com.example.moodapp.models.RegistroEmocion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EscogeActividadFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private var posicion : Int = -1
    private lateinit var auth: FirebaseAuth
    private val actividades = mutableListOf<Actividad>()
    private val actividadesNombre = mutableListOf<String>()
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var registroEmocion:RegistroEmocion
    lateinit var spActividades: Spinner
    private lateinit var guardarRegistro:Button
    private lateinit var sigueAsi : TextView
    private lateinit var imagenActividad : ImageView
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_escoge_actividad, container, false)
        spActividades = view.findViewById(R.id.sp_actividades)
        sigueAsi = view.findViewById(R.id.sigue_asi)
        imagenActividad = view.findViewById(R.id.imagen_actividad)
        guardarRegistro = view.findViewById(R.id.guardar_registro)
        Glide.with(view.context).load(registroEmocion.emocionImagenUrl).into(imagenActividad)
        revisarSeveridad()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        guardarRegistro.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        registroEmocion = arguments!!.getParcelable<RegistroEmocion>("registroEmocion")!!
    }

    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.guardar_registro -> guardarNuevoRegistro()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        posicion = 0
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        posicion = position
        if(position==0){
            registroEmocion.actividad = null
        }else{
            registroEmocion.actividad = parent!!.getItemAtPosition(position).toString()
        }
    }

    fun getActividades(){
        val actividadesRef = db.collection(resources.getString(R.string.coleccion_actividad))
        actividadesRef.orderBy("likes", Query.Direction.DESCENDING)
            .addSnapshotListener{
                snapshot, exception ->
                if (exception!=null){
                    return@addSnapshotListener
                }
                for(doc in snapshot!!.documentChanges){
                    when(doc.type){
                        DocumentChange.Type.ADDED -> {
                            val actividad = Actividad(
                                nombreActividad = doc.document.getString("nombre")!!,
                                likes = doc.document.getLong("likes"),
                                dislikes = doc.document.getLong("dislikes")
                            )
                            actividades.add(actividad)
                            actividadesNombre.add( doc.document.getString("nombre")!!)
                            spinnerAdapter.notifyDataSetChanged()
                        }
                        else -> return@addSnapshotListener
                    }
                }
            }
    }

    fun revisarSeveridad(){
        if(registroEmocion.emocionSeveridad>2){
            getActividades()
            actividadesNombre.add(0,"Escoge una actividad")
            llenarSpinner()
            sigueAsi.visibility = View.GONE
        }else{
            registroEmocion.actividad = resources.getString(R.string.sigue_asi)
            spActividades.visibility = View.GONE
            sigueAsi.visibility = View.VISIBLE
        }
    }

    fun llenarSpinner(){
        spinnerAdapter = ArrayAdapter(context!!,android.R.layout.simple_spinner_dropdown_item,actividadesNombre)
        spActividades.adapter = spinnerAdapter
        spActividades.onItemSelectedListener = this
    }

    fun guardarNuevoRegistro(){
        if(registroEmocion.actividad != null){
            val userId = auth.currentUser!!.uid
            val registroRef = db
                .collection(resources.getString(R.string.coleccion_usuario))
                .document(userId)
                .collection(resources.getString(R.string.coleccion_registro))
            registroRef.document()
                .set(registroEmocion)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Registro completado",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }else{
                        Toast.makeText(
                            context,
                            "Registro no completado",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        } else {
            Toast.makeText(context,
                resources.getString(R.string.debe_escoger_actividad),
                Toast.LENGTH_SHORT)
                .show()
        }
    }

}
