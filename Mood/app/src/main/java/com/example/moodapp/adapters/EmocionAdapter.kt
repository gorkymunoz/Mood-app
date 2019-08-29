package com.example.moodapp.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moodapp.models.Emocion
import com.example.moodapp.R
import com.example.moodapp.views.RegistraEmocionFragment


class EmocionAdapter(
    private val emociones: List<Emocion>,
    private val clickListener: (Emocion) -> Unit
    ) :
    RecyclerView.Adapter<EmocionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmocionViewHolder {
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.list_emociones,parent,false)
        view.layoutParams.width = obtenerAncho()/emociones.size
        return EmocionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emociones.size
    }

    override fun onBindViewHolder(holder: EmocionViewHolder, position: Int) {
        val emocion = emociones[position]
        holder.nombreEmocion.text = emocion.nombreEmocion
        holder.setImage(emocion.imagenUrl!!)
        holder.bind(emocion,clickListener)
    }

    private fun obtenerAncho():Int{
        return Resources.getSystem().displayMetrics.widthPixels
    }
}

class EmocionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    fun bind(emocion: Emocion,clickListener:(Emocion)->Unit){
        itemView.setOnClickListener { clickListener(emocion)}
    }

    val nombreEmocion = itemView.findViewById<TextView>(R.id.nombre_emocion)
    val imagePost =itemView.findViewById<ImageView>(R.id.imagen_emocion)
    fun setImage(url:String){
        Glide
            .with(itemView.context)
            .load(url)
            .into(imagePost)
    }

}