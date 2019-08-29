package com.example.moodapp.views


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
import com.bumptech.glide.Glide
import com.example.moodapp.navigation.PrincipalActivity
import com.example.moodapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

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
        Glide.with(context!!).load(R.drawable.logo).into(imagen_login)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser!=null){
            usuarioLogueado()
        }
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
                usuarioLogueado()
            } else {
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthInvalidUserException) {
                    correo_login.error = "Correo Invalido"
                    correo_login.requestFocus()
/*                    mStatusTextView.setError("Invalid Emaild Id")
                    mStatusTextView.requestFocus()*/
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    contrasena_login_layout.error = "Credenciales Incorrectas"
  /*                     mStatusTextView.setError("Invalid Password")
                    mStatusTextView.requestFocus()
  */              } catch (e: FirebaseNetworkException) {
                    Toast.makeText(context,"No tiene conexión a Internet",Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
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

    fun usuarioLogueado(){
        val intent = Intent(context, PrincipalActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}
