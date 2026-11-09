package com.example.projectm28

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectm28.databinding.ActivityBusquedaDeProductoBinding
import com.google.firebase.firestore.FirebaseFirestore


class busqueda_de_producto : AppCompatActivity() {

    private lateinit var binding: ActivityBusquedaDeProductoBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusquedaDeProductoBinding.inflate(layoutInflater)
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
                            binding.tvNombre.text = "Nombre del producto: ${doc.getString("nombre") ?: ""}"
                            binding.tvCantidad.text = "Cantidad en stock: ${doc.getLong("stock")?.toString() ?: "0"}"
                            binding.tvPrecio.text = "Precio: $${doc.getDouble("precio") ?: 0.0}"
                            binding.tvCategoria.text = "Categoría: ${doc.getString("categoria") ?: ""}"
                        } else {
                            Toast.makeText(this, "No se encontró el producto", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        binding.btnActualizar.setOnClickListener {
            val codigo = binding.etCodigo.text.toString().trim()

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Ingrese un código", Toast.LENGTH_SHORT).show()
            } else {
                val nombre = binding.tvNombre.text.toString().replace("Nombre del producto: ", "").trim()
                val cantidadTexto = binding.tvCantidad.text.toString().replace("Cantidad en stock: ", "").trim()
                val cantidad = cantidadTexto.toIntOrNull() ?: 0
                val precioTexto = binding.tvPrecio.text.toString().replace("Precio: $", "").trim()
                val precio = precioTexto.toDoubleOrNull() ?: 0.0
                val categoria = binding.tvCategoria.text.toString().replace("Categoría: ", "").trim()

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
                        Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }
}
