package com.example.projectm28

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectm28.databinding.ActivityPanelDeControlBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration


class panel_de_control : AppCompatActivity() {

    private lateinit var binding: ActivityPanelDeControlBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ProductoAdapter
    private var listener: ListenerRegistration? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanelDeControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        adapter = ProductoAdapter(mutableListOf())
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter

        // Botón Alta
        binding.btnAlta.setOnClickListener {
            val intent = Intent(this, alta_de_producto::class.java)
            startActivity(intent)
        }

        // Botón Baja
        binding.btnBaja.setOnClickListener {
            val intent = Intent(this, baja_de_producto::class.java)
            startActivity(intent)
        }

        binding.btnBuscar.setOnClickListener {
            val intent = Intent(this, busqueda_de_producto::class.java)
            startActivity(intent)
        }
        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        listener = db.collection("productos")
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val lista = snapshot.documents.map { doc ->
                        Producto(
                            nombre = doc.getString("nombre") ?: "",
                            codigo = doc.getString("codigo") ?: "",
                            categoria = doc.getString("categoria") ?: "",
                            stock = doc.getLong("stock")?.toInt() ?: 0,
                            precio = doc.getDouble("precio") ?: 0.0
                        )
                    }
                    adapter.updateList(lista)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}
