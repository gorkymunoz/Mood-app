package com.example.moodapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moodapp.Models.RegistroEmocion
import com.example.moodapp.R

class HistorialAdapter(private val registros: List<RegistroEmocion>) : RecyclerView.Adapter<HistorialViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_emocion,parent,false)
        return HistorialViewHolder(view)
    }

    override fun getItemCount(): Int {
        return registros.size
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val registro = registros[position]
        holder.horaRegistro.text = registro.horaRegistro
        holder.fechaRegistro.text = registro.fechaRegistro
        holder.nombreActividad.text = registro.actividad
        holder.setImage(registro.emocionImagenUrl)
    }

}

class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

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