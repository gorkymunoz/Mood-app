package com.example.moodapp.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.moodapp.R
import com.google.firebase.auth.FirebaseAuth

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment(),View.OnClickListener {

    lateinit var irRegistro: TextView
    private lateinit var auth: FirebaseAuth

    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.ir_registro -> goToRegistro()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_login, container, false)
        irRegistro = view.findViewById(R.id.ir_registro)
        irRegistro.setOnClickListener(this)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }

    fun goToRegistro(){
        findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
    }


}
