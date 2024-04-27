package com.example.kitchenstory

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kitchenstory.databinding.EachNewingredientItemBinding

class AddedIngredientsAdapter (private val list: MutableList<AddedIngredientsData>):
    RecyclerView.Adapter<AddedIngredientsAdapter.AddedIngredientsViewHolder>() {

    private  val TAG = "IngredientAdapter"
    private var listener:AddedIngredientsAdapterInterface? = null

    fun setListener(listener:AddedIngredientsAdapterInterface){
        this.listener = listener
    }
    inner class AddedIngredientsViewHolder(val binding: EachNewingredientItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedIngredientsViewHolder {
        val binding = EachNewingredientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddedIngredientsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AddedIngredientsViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.currIngredient.text = this.newIngredient
                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteItemClicked(this)
                }
            }
        }
    }

    interface AddedIngredientsAdapterInterface{
        fun onDeleteItemClicked(addedIngredientsData: AddedIngredientsData)
    }

}