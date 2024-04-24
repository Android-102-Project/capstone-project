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

const val RECIPE_EXTRA = "RECIPE_EXTRA"
private const val TAG = "RecipeAdapter"

class RecipeAdapter(private val context: Context, private val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO: Get the individual article and bind to holder
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount() = recipes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val recipeImageView = itemView.findViewById<ImageView>(R.id.recipeImage)
        private val recipeTitleTextView = itemView.findViewById<TextView>(R.id.recipeTitle)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(recipe: Recipe) {
            recipeTitleTextView.text = recipe.title
//            abstractTextView.text = article.abstract

            Glide.with(context)
                .load(recipe.image)
                .into(recipeImageView)
        }

        // TODO: Write a helper method to help set up the onBindViewHolder method

        override fun onClick(v: View?) {
            // TODO: Get selected article
            val recipe = recipes[absoluteAdapterPosition]

            // TODO: Navigate to Details screen and pass selected recipe

            // We can create an Intent, using the current context, and the Class that we are navigating to.
            val intent = Intent(context, RecipeDetailActivity::class.java)

            // We can use .putExtra to pass along the article, with the first argument being the key for the data, and the next being article itself.
            intent.putExtra(RECIPE_EXTRA, recipe.id)

            // We can then call startActivity with the current context, using the intent as its parameter.
            context.startActivity(intent)
        }
    }
}