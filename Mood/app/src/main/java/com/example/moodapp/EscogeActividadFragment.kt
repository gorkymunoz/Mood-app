package com.example.moodapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.moodapp.Models.RegistroEmocion


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EscogeActividadFragment : Fragment() {

    private lateinit var registroEmocion:RegistroEmocion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_escoge_actividad, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registroEmocion = arguments!!.getParcelable<RegistroEmocion>("registroEmocion")!!
        Toast.makeText(context,registroEmocion.emocionNombre,Toast.LENGTH_SHORT).show()
    }


}
