package com.example.moodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moodapp.models.Actividad
import com.example.moodapp.models.RegistroEmocion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_escoge_actividad.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EscogeActividadFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private val actividades = mutableListOf<Actividad>()
    private val actividadesNombre = mutableListOf<String>()
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var registroEmocion:RegistroEmocion
    lateinit var spActividades: Spinner
    private lateinit var pbEscogerActividad:ProgressBar
    private lateinit var constraintEscogerActividad:ConstraintLayout
    private lateinit var guardarRegistro:Button
    private lateinit var sigueAsi : TextView
    private lateinit var imagenActividad : ImageView
    private lateinit var db : FirebaseFirestore
    private var estado = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_escoge_actividad, container, false)
        spActividades = view.findViewById(R.id.sp_actividades)
        sigueAsi = view.findViewById(R.id.sigue_asi)
        imagenActividad = view.findViewById(R.id.imagen_actividad)
        guardarRegistro = view.findViewById(R.id.guardar_registro)
        pbEscogerActividad = view.findViewById(R.id.pb_escoge_actividad)
        constraintEscogerActividad = view.findViewById(R.id.constraint_escoge_actividad)
        Glide.with(view.context).load(registroEmocion.emocionImagenUrl).into(imagenActividad)
        revisarSeveridad()
        updateUI(estado)
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
        registroEmocion = arguments!!.getParcelable("registroEmocion")!!
    }

    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.guardar_registro -> guardarNuevoRegistro()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        registroEmocion.actividad = actividades[0].nombreActividad
        registroEmocion.actividadId = actividades[0].actividadId
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            registroEmocion.actividad = parent!!.getItemAtPosition(position).toString()
            registroEmocion.actividadId = actividades[position].actividadId
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
                                actividadId = doc.document.id,
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
            llenarSpinner()
            sigueAsi.visibility = View.GONE
        }else{
            registroEmocion.actividad = resources.getString(R.string.sigue_asi)
            registroEmocion.estado = "Calificado"
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
            updateUI(true)
            val userId = auth.currentUser!!.uid
            val registroRef = db
                .collection(resources.getString(R.string.coleccion_usuario))
                .document(userId)
                .collection(resources.getString(R.string.coleccion_registro)).document()
            registroEmocion.documentoId = registroRef.id
            registroRef
                .set(registroEmocion)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        findNavController().popBackStack(R.id.registraEmocionFragment2,false)
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
            updateUI(false)
        } else {
            Toast.makeText(context,
                resources.getString(R.string.debe_escoger_actividad),
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun updateUI(estado:Boolean){
        if(estado){
            pbEscogerActividad.visibility = View.VISIBLE
            constraintEscogerActividad.visibility = View.GONE
        }else{
            pbEscogerActividad.visibility = View.GONE
            constraintEscogerActividad.visibility = View.VISIBLE
        }
    }

}
