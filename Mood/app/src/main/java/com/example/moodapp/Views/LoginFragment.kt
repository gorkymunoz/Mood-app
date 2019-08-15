package com.example.moodapp.Views


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.moodapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment(),View.OnClickListener {

    private lateinit var iniciarSesionBoton:Button
    lateinit var irRegistro: TextView
    private lateinit var auth: FirebaseAuth

    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.ir_registro -> goToRegistro()
            R.id.iniciar_sesion_boton -> iniciarSesion(
                correo_login.text.toString().trim(),
                contrasena_login.text.toString().trim()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_login, container, false)
        irRegistro = view.findViewById(R.id.ir_registro)
        iniciarSesionBoton = view.findViewById(R.id.iniciar_sesion_boton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarSesionBoton.setOnClickListener(this)
        irRegistro.setOnClickListener(this)
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

    private fun iniciarSesion(email:String, password:String) {

        if (!validarCampos()){
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context,"Credenciales correctas",Toast.LENGTH_SHORT).show()
                //view?.findNavController()?.navigate(R.id.,null)

                /*val intent = Intent(context, MainNavigationActivity::class.java)
                startActivity(intent)
                activity?.finish()*/

            } else {
                Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun validarCampos():Boolean{
        var valido = true

        val email = correo_login.text.toString()
        if(TextUtils.isEmpty(email)){
            correo_login.error = "Obligatorio."
            valido = false
        }else{
            correo_login.error = null
        }
        val contrasena = contrasena_login.text.toString()
        if(TextUtils.isEmpty(contrasena)){
            contrasena_login.error = "Obligatorio."
            valido = false
        }else{
            contrasena_login.error = null
        }
        return valido
    }


}
