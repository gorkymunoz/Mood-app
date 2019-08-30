package com.example.moodapp.views


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moodapp.adapters.EmocionAdapter
import com.example.moodapp.R
import kotlinx.android.synthetic.main.fragment_registra_emocion.*
import java.text.SimpleDateFormat
import java.util.*
import android.view.*
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.moodapp.models.Emocion
import com.example.moodapp.models.RegistroEmocion
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistraEmocionFragment : Fragment(), View.OnClickListener {

    private lateinit var database: FirebaseFirestore
    private lateinit var fechaRegistroEmocion: EditText
    private lateinit var horaRegistroEmocion: EditText
    private lateinit var calendar: Calendar
    private lateinit var rvEmocion: RecyclerView
    private lateinit var auth:FirebaseAuth
    private lateinit var layoutSnackBar: CoordinatorLayout

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.fecha_registro_emocion -> mostrarDialogoFecha(calendar)
            R.id.hora_registro_emocion -> mostrarDialogoHora(calendar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseFirestore.getInstance()
        calendar = Calendar.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registra_emocion, container, false)
        fechaRegistroEmocion = view.findViewById(R.id.fecha_registro_emocion)
        horaRegistroEmocion = view.findViewById(R.id.hora_registro_emocion)
        rvEmocion = view.findViewById(R.id.rv_emociones)
        layoutSnackBar = view.findViewById(R.id.layout_snackbar)
        consultarRegistrosSinCalificar()
        iniciarRVEmocion()
        iniciarFechaHora()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(view.context).load(R.drawable.ic_date).into(imagen_fecha)
        Glide.with(view.context).load(R.drawable.ic_watch).into(imagen_hora)
        fechaRegistroEmocion.setOnClickListener(this)
        horaRegistroEmocion.setOnClickListener(this)
    }

    fun consultarRegistrosSinCalificar() {
        val registroRef = database
            .collection(resources.getString(R.string.coleccion_usuario))
            .document(auth.currentUser!!.uid)
            .collection(resources.getString(R.string.coleccion_registro))
        registroRef
            .whereEqualTo("estado", "Sin calificar")
            .get().addOnCompleteListener { task ->
                val result = task.result
                if (task.isSuccessful && !result!!.isEmpty) {

                    tamanoSinCalificar = task.result!!.documents.size
                    if(tamanoSinCalificar>0){
                        val emocionRegistrada:RegistroEmocion= task.result!!.documents[0].toObject(RegistroEmocion::class.java)!!
                        Snackbar.make(
                            layoutSnackBar,
                            task.result!!.documents.size.toString(),
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction(
                                R.string.califica_actividad
                            ) { abrirDialog(emocionRegistrada) }
                            .show()
                    }
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun abrirDialog(emocionRegistrada:RegistroEmocion){
        Log.e("emocion",emocionRegistrada.emocionNombre)
        val fm = fragmentManager!!
        val bundle = bundleOf("registroEmocion" to emocionRegistrada)
        val dialog = CalificarActividadDialogFragment()
        dialog.arguments = bundle
        dialog.show(fm,"asd")
    }

    private fun iniciarRVEmocion() {
        rvEmocion.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        val emociones = getData()
        rvEmocion.adapter = EmocionAdapter(emociones) { emocion: Emocion -> escogerActividad(emocion) }
    }

    private fun mostrarDialogoFecha(calendar: Calendar) {
        val datePicker = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val fechaEscogida = calendar.time
            val fechaFormato = darFormatoFecha(fechaEscogida)
            fechaRegistroEmocion.setText(fechaFormato)

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }

    private fun escogerActividad(emocion: Emocion) {
        if(tamanoSinCalificar==0){
            val registroEmocion = RegistroEmocion(
                fechaRegistro = fechaRegistroEmocion.text.toString(),
                horaRegistro = horaRegistroEmocion.text.toString(),
                emocionNombre = emocion.nombreEmocion!!,
                emocionSeveridad = emocion.severidadEmocion!!,
                emocionImagenUrl = emocion.imagenUrl!!,
                actividad = null,
                estado = "Sin calificar",
                actividadId = null,
                documentoId = null
            )
            val bundle = bundleOf("registroEmocion" to registroEmocion)
            findNavController().navigate(R.id.action_registraEmocionFragment2_to_escogeActividadFragment, bundle)
        }else{
            Toast.makeText(context,"debe calificar",Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoHora(calendar: Calendar) {
        val timePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hora, minuto ->
            calendar.set(Calendar.HOUR_OF_DAY, hora)
            calendar.set(Calendar.MINUTE, minuto)
            val horaEscogida = calendar.time
            val horaFormato = darFormatoHora(horaEscogida)
            horaRegistroEmocion.setText(horaFormato)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
        timePicker.show()
    }

    fun iniciarFechaHora() {
        fechaRegistroEmocion.inputType = InputType.TYPE_NULL
        fechaRegistroEmocion.setText(fechaActual(calendar))

        horaRegistroEmocion.inputType = InputType.TYPE_NULL
        horaRegistroEmocion.setText(horaActual(calendar))
    }

    private fun horaActual(calendar: Calendar): String {
        val hora = calendar.time
        return darFormatoHora(hora)
    }

    @SuppressLint("SimpleDateFormat")
    private fun darFormatoHora(hora: Date): String {
        return SimpleDateFormat("h:mm a").format(hora)
    }

    fun fechaActual(calendar: Calendar): String {
        val date = calendar.time
        return darFormatoFecha(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun darFormatoFecha(fecha: Date): String {
        val dateFormat = SimpleDateFormat("EEEE, dd 'de' MMMM").format(fecha)
        return dateFormat
    }

    fun getData(): List<Emocion> {
        val emociones = mutableListOf<Emocion>()
        val emocionessRef = database.collection("Emociones")
        emocionessRef
            .orderBy("severidad")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                for (doc in snapshot!!.documentChanges) {
                    when (doc.type) {
                        DocumentChange.Type.ADDED -> {
                            val emocion = Emocion(
                                doc.document.getString("nombre"),
                                doc.document.getLong("severidad"),
                                doc.document.getString("imagenUrl")
                            )
                            emociones.add(emocion)
                            rvEmocion.adapter?.notifyItemInserted(emociones.size - 1)
                        }
                        else -> return@addSnapshotListener
                    }
                }
            }
        return emociones
    }

    companion object{
        var tamanoSinCalificar:Int = 0
    }
}
