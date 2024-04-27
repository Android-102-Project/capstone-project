package com.example.kitchenstory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class IngredientAdapter(private val context: Context, private val ingredients: List<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO: Get the individual article and bind to holder
        val ingredient = ingredients[position]
        holder.bind(ingredient)
    }

    override fun getItemCount() = ingredients.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val ingredientImageView = itemView.findViewById<ImageView>(R.id.ingreImage)
        private val ingredientNameTextView = itemView.findViewById<TextView>(R.id.ingreName)
        private val ingredientQuantityTextView = itemView.findViewById<TextView>(R.id.quantity)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(ingredient: Ingredient) {
            ingredientNameTextView.text = ingredient.name
//            abstractTextView.text = article.abstract
            ingredientQuantityTextView.text = ingredient.amount.toString() + " " + ingredient.unit

            Glide.with(context)
                .load("https://spoonacular.com/cdn/ingredients_100x100/"+ingredient.image)
                .into(ingredientImageView)


        }

        // TODO: Write a helper method to help set up the onBindViewHolder method

        override fun onClick(v: View?) {

        }
    }

}