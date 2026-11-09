package com.example.projectm28

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectm28.databinding.ActivityAltaDeProductoBinding
import com.google.firebase.firestore.FirebaseFirestore


class alta_de_producto : AppCompatActivity() {

    private lateinit var binding: ActivityAltaDeProductoBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAltaDeProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.btnGuardar.setOnClickListener {
            val codigo = binding.etCodigo.text.toString().trim()
            val nombre = binding.etNombre.text.toString().trim()
            val cantidad = binding.etCantidad.text.toString().toIntOrNull() ?: 0
            val precio = binding.etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val categoria = binding.etCategoria.text.toString().trim()

            if (codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val producto = Producto(
                    nombre = nombre,
                    codigo = codigo,
                    categoria = categoria,
                    stock = cantidad,
                    precio = precio
                )

                db.collection("productos").document(codigo)
                    .set(producto)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // cerrar y volver al panel de control
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }
}
