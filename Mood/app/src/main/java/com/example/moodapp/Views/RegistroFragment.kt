package com.example.moodapp.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.moodapp.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistroFragment : Fragment(),View.OnClickListener {
    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.ir_login -> goToLogin()
        }
    }

    private fun goToLogin() {
        findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
    }

    private lateinit var irLogin:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view  =inflater.inflate(R.layout.fragment_registro, container, false)
        irLogin = view.findViewById(R.id.ir_login)
        irLogin.setOnClickListener(this)
        return view
    }


}
