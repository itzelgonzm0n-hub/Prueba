package com.example.projectm28

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectm28.databinding.ActivityBajaDeProductoBinding
import com.google.firebase.firestore.FirebaseFirestore


class baja_de_producto : AppCompatActivity() {

    private lateinit var binding: ActivityBajaDeProductoBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBajaDeProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.btnBuscar.setOnClickListener {
            val codigo = binding.etCodigo.text.toString().trim()

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Ingrese un código", Toast.LENGTH_SHORT).show()
            } else {
                db.collection("productos").document(codigo)
                    .get()
                    .addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            binding.tvNombre.setText(doc.getString("nombre") ?: "")
                            binding.tvCantidad.setText(doc.getLong("stock")?.toString() ?: "")
                            binding.tvPrecio.setText(doc.getDouble("precio")?.toString() ?: "")
                            binding.tvCategoria.setText(doc.getString("categoria") ?: "")
                        } else {
                            Toast.makeText(this, "No se encontró el producto", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        binding.btnEliminar.setOnClickListener {
            val codigo = binding.etCodigo.text.toString().trim()

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Ingrese un código", Toast.LENGTH_SHORT).show()
            } else {
                db.collection("productos").document(codigo)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al eliminar: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }
}
