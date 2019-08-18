package com.example.moodapp.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.moodapp.R
import kotlinx.android.synthetic.main.fragment_registra_emocion.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegistraEmocionFragment : Fragment() {

    private lateinit var layoutFecha:ConstraintLayout
    private lateinit var layoutHora:ConstraintLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_registra_emocion, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(view.context).load(R.drawable.ic_date).into(imagen_fecha)
        Glide.with(view.context).load(R.drawable.ic_watch).into(imagen_hora)
    }


}
