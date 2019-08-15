package com.example.moodapp.Views


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_registro.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistroFragment : Fragment(),View.OnClickListener {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var mRegistrarseBoton:Button

    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.ir_login -> goToLogin()
            R.id.registrarse_boton -> registrarUsuario(correo_registro.text.toString(),contrasena_registro.text.toString())
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
        mRegistrarseBoton = view.findViewById(R.id.registrarse_boton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        irLogin.setOnClickListener(this)
        mRegistrarseBoton.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun registrarUsuario(email: String, password: String) {
        if (!validarCampos()) {
            return
        }
        mAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser!!.uid
                    val data = HashMap<String, Any>()
                    data["Username"] = username_registro.text.toString().trim()
                    data["Email"] = correo_registro.text.toString().trim()
                    val bienvenidoUsuario: String = resources.getString(R.string.bienvenido,username_registro.text.toString().trim())
                    Toast.makeText(
                        context, bienvenidoUsuario,
                        Toast.LENGTH_SHORT
                    ).show()

                }else{
                    Toast.makeText(
                        context, task.exception?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validarCampos():Boolean{
        var valido = true

        val username = username_registro.text.toString()
        if(TextUtils.isEmpty(username)){
            username_registro.error = "Obligatorio."
            valido = false
        }else{
            username_registro.error = null
        }
        val email = correo_registro.text.toString()
        if(TextUtils.isEmpty(email)){
            correo_registro.error = "Obligatorio."
            valido = false
        }else{
            correo_registro.error = null
        }
        val contrasena = contrasena_registro.text.toString()
        if(TextUtils.isEmpty(contrasena)){
            contrasena_registro.error = "Obligatorio."
            valido = false
        }else{
            contrasena_registro.error = null
        }

        return valido
    }


}
