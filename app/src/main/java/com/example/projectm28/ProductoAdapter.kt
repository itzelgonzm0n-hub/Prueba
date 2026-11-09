package com.example.projectm28

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectm28.databinding.ActivityItemProductoBinding


class ProductoAdapter(private var productos: MutableList<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(val binding: ActivityItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ActivityItemProductoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.binding.tvNombre.text = producto.nombre
        holder.binding.tvCodigo.text = "Código: ${producto.codigo}"
        holder.binding.tvCategoria.text = "Categoría: ${producto.categoria}"
        holder.binding.tvStockPrecio.text = "Stock: ${producto.stock} | $${producto.precio}"
    }

    override fun getItemCount(): Int = productos.size

    fun updateList(newList: List<Producto>) {
        productos.clear()
        productos.addAll(newList)
        notifyDataSetChanged()
    }
}
