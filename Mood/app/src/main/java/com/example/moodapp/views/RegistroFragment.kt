package com.example.moodapp.views


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
import com.example.moodapp.interfaces.Utils
import com.example.moodapp.models.Usuario
import com.example.moodapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_registro.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistroFragment : Fragment(), View.OnClickListener, Utils {

    private lateinit var auth: FirebaseAuth
    private lateinit var mRegistrarseBoton: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var irLogin: TextView

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.ir_login -> goToLogin()
            R.id.registrarse_boton -> registrarUsuario(
                correo_registro.text.toString(),
                contrasena_registro.text.toString()
            )
        }
    }

    override fun showProgressBar() {
        layout_registro.visibility = View.GONE
        pb_registro.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        layout_registro.visibility = View.VISIBLE
        pb_registro.visibility = View.GONE
    }

    override fun showToast(mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registro, container, false)
        irLogin = view.findViewById(R.id.ir_login)
        mRegistrarseBoton = view.findViewById(R.id.registrarse_boton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        irLogin.setOnClickListener(this)
        mRegistrarseBoton.setOnClickListener(this)
        Glide.with(view.context).load(R.drawable.logo).into(imagen_registro)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    private fun registrarUsuario(email: String, password: String) {
        if (!validarCampos()) {
            return
        }
        showProgressBar()
        auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser!!.uid
                    guardarUsuario(userId)
                } else {
                    Toast.makeText(
                        context, task.exception?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun guardarUsuario(userId: String) {
        val nuevoUsuario = Usuario(
            username_registro.text.toString().trim(),
            correo_registro.text.toString().trim(),
            null,
            userId
        )
        db
            .collection(resources.getString(R.string.coleccion_usuario))
            .document(userId)
            .set(nuevoUsuario)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val bienvenidoUsuario: String =
                        resources.getString(
                            R.string.bienvenido,
                            username_registro.text
                                .toString()
                                .trim()
                        )
                    showToast(bienvenidoUsuario)
                    hideProgressBar()
                } else {
                    showToast(task.exception?.message.toString())
                }
            }
    }

    private fun validarCampos(): Boolean {
        var valido = true

        val username = username_registro.text.toString()
        if (TextUtils.isEmpty(username)) {
            username_registro.error = "Obligatorio."
            valido = false
        } else {
            username_registro.error = null
        }
        val email = correo_registro.text.toString()
        if (TextUtils.isEmpty(email)) {
            correo_registro.error = "Obligatorio."
            valido = false
        } else {
            correo_registro.error = null
        }
        val contrasena = contrasena_registro.text.toString()
        if (TextUtils.isEmpty(contrasena)) {
            contrasena_registro.error = "Obligatorio."
            valido = false
        } else {
            contrasena_registro.error = null
        }

        return valido
    }

    private fun goToLogin(){
        findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
    }

}
