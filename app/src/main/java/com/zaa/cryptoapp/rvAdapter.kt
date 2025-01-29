package com.zaa.cryptoapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.zaa.cryptoapp.databinding.RvItemBinding

// Modify the adapter to accept a click listener
class rvAdapter(
    val context: Context,
    var data: ArrayList<Model>,
    private val onItemClick: (Model) -> Unit // lambda for item click
) : RecyclerView.Adapter<rvAdapter.viewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun changeData(filterData: ArrayList<Model>) {
        data = filterData
        notifyDataSetChanged()
    }

    inner class viewHolder(val binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Add click listener to each item view
        init {
            binding.root.setOnClickListener {
                // Pass the Model object of the clicked item to the MainActivity's click handler
                onItemClick(data[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = RvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        setAnimation(holder.itemView)
        holder.binding.name.text = data[position].name
        holder.binding.symbol.text = data[position].symbol
        holder.binding.price.text = data[position].price
    }

    private fun setAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        view.startAnimation(anim)
    }
}
