package com.example.moodapp.adapters

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moodapp.models.RegistroEmocion
import com.example.moodapp.R
import com.example.moodapp.views.CalificarActividadDialogFragment
import com.example.moodapp.views.RegistraEmocionFragment

class HistorialAdapter(private val registros: List<RegistroEmocion>, private val fragmentManager:FragmentManager) :
    RecyclerView.Adapter<HistorialViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_emocion,parent,false)
        return HistorialViewHolder(view)
    }

    override fun getItemCount(): Int {
        return registros.size
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {

        val registro = registros[position]
        if(registro.estado == "Sin calificar"){
            holder.cardLayout.setBackgroundColor(Color.RED)
            holder.itemView.setOnClickListener {
                abrirDialog(registro)
            }
        }else{
            holder.cardLayout.setBackgroundColor(Color.GREEN)
        }
        holder.horaRegistro.text = registro.horaRegistro
        holder.fechaRegistro.text = registro.fechaRegistro
        holder.nombreActividad.text = registro.actividad
        holder.setImage(registro.emocionImagenUrl)
    }
    fun abrirDialog(registro: RegistroEmocion) {
        Log.e("emocion",registro.emocionNombre)
        val bundle = bundleOf("registroEmocion" to registro)
        val dialog = CalificarActividadDialogFragment()
        dialog.arguments = bundle
        dialog.show(fragmentManager,"Califica actividad")
    }
}

class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val cardLayout = itemView.findViewById<CardView>(R.id.card_registro_historial)
    val nombreActividad = itemView.findViewById<TextView>(R.id.actividad_historial)
    val fechaRegistro = itemView.findViewById<TextView>(R.id.fecha_historial)
    val horaRegistro = itemView.findViewById<TextView>(R.id.hora_historial)
    val imagenHistorial = itemView.findViewById<ImageView>(R.id.imagen_historial)
    fun setImage(url:String){
        Glide.with(itemView.context)
            .load(url)
            .into(imagenHistorial)
    }
}