package com.example.projectm28

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectm28.databinding.ActivityRegistrarCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class registrar_cuenta : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarCuentaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnRegistrarse.setOnClickListener {
            val correo = binding.etCorreo.text.toString().trim()
            val pass = binding.etContrasena.text.toString().trim()
            val confirmar = binding.etConfirmar.text.toString().trim()

            if (correo.isEmpty() || pass.isEmpty() || confirmar.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            } else if (pass != confirmar) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {

                auth.createUserWithEmailAndPassword(correo, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user != null) {

                                val userData = hashMapOf(
                                    "correo" to correo,
                                    "uid" to user.uid,
                                    "fechaRegistro" to System.currentTimeMillis(),

                                    )

                                db.collection("usuarios").document(user.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Cuenta registrada en Firestore", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Error al guardar: ${it.message}", Toast.LENGTH_LONG).show()
                                    }

                                val intent = Intent(this, panel_de_control::class.java)
                                intent.putExtra("correoUsuario", correo)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.tvIrLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
