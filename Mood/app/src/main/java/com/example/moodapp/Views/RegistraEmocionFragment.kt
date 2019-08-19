package com.example.moodapp.Views


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evernote.android.state.StateSaver
import com.example.moodapp.Adapters.EmocionAdapter
import com.example.moodapp.R
import kotlinx.android.synthetic.main.fragment_registra_emocion.*
import java.text.SimpleDateFormat
import java.util.*




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegistraEmocionFragment : Fragment(), View.OnClickListener{

    private lateinit var fechaRegistroEmocion : EditText
    private lateinit var horaRegistroEmocion : EditText
    private lateinit var calendar:Calendar
    private lateinit var rvEmocion:RecyclerView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }

    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.fecha_registro_emocion -> mostrarDialogoFecha(calendar)
            R.id.hora_registro_emocion -> mostrarDialogoHora(calendar)
        }
    }

    private fun mostrarDialogoHora(calendar: Calendar) {
        val timePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener{view,hora,minuto ->
            calendar.set(Calendar.HOUR_OF_DAY,hora)
            calendar.set(Calendar.MINUTE,minuto)
            val horaEscogida = calendar.time
            val horaFormato = darFormatoHora(horaEscogida)
            horaRegistroEmocion.setText(horaFormato)
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false)
        timePicker.show()
    }

    private fun mostrarDialogoFecha(calendar: Calendar) {
        val datePicker = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val fechaEscogida = calendar.time
            val fechaFormato = darFormatoFecha(fechaEscogida)
            fechaRegistroEmocion.setText(fechaFormato)

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calendar = Calendar.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_registra_emocion, container, false)
        fechaRegistroEmocion = view.findViewById(R.id.fecha_registro_emocion)
        horaRegistroEmocion = view.findViewById(R.id.hora_registro_emocion)
        rvEmocion = view.findViewById(R.id.rv_emociones)
        iniciarRVEmocion()
        iniciarFechaHora()
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("destruye","me destruyeeeen")
    }

    private fun iniciarRVEmocion() {
        rvEmocion.layoutManager = LinearLayoutManager(this.context,RecyclerView.HORIZONTAL,false)
        rvEmocion.adapter = EmocionAdapter()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StateSaver.restoreInstanceState(this, savedInstanceState)
        Glide.with(view.context).load(R.drawable.ic_date).into(imagen_fecha)
        Glide.with(view.context).load(R.drawable.ic_watch).into(imagen_hora)
        fechaRegistroEmocion.setOnClickListener(this)
        horaRegistroEmocion.setOnClickListener(this)
    }

    fun iniciarFechaHora(){
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

    fun fechaActual(calendar: Calendar):String{
        val date = calendar.time
        return darFormatoFecha(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun darFormatoFecha(fecha: Date):String{
        val dateFormat = SimpleDateFormat("EEEE, dd 'de' MMMM").format(fecha)
        return dateFormat
    }


}
