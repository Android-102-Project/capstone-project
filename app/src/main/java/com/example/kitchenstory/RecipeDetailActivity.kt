package com.example.kitchenstory

import android.os.Bundle
import android.util.Log
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.decodeFromString
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "RecipeDetailActivity"

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var recipeImageView: ImageView
    private lateinit var recipeTitleTextView: TextView
    private lateinit var recipeDescriptionTextView: TextView
    private lateinit var recipeIngredientRecyclerView: RecyclerView
    private lateinit var procedureView: TextView
    private val ingredients = mutableListOf<Ingredient>()
    private val API_KEY = BuildConfig.API_KEY
    private val RECIPES_DETAILS_URL =
        "https://api.spoonacular.com/recipes/640352/information?apiKey=$API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_activity_detail)
        val context = this

        // TODO: Find the views for the screen
        recipeImageView = findViewById(R.id.recipeImage)
        recipeTitleTextView = findViewById(R.id.recipeTitle)
        recipeDescriptionTextView = findViewById(R.id.cookingTIme)
        recipeIngredientRecyclerView = findViewById(R.id.ingredient_list)
        procedureView = findViewById(R.id.instructionStepsText)
        // TODO: Get the extra from the Intent
        val recipeId = intent.getSerializableExtra(RECIPE_EXTRA) as Int
        Log.i(TAG, "Recipe ID $recipeId")
        ///Bind the recyclerView
        recipeIngredientRecyclerView = findViewById(R.id.ingredient_list)

        val ingredientAdapter = IngredientAdapter(this, ingredients)
        recipeIngredientRecyclerView.adapter = ingredientAdapter

        recipeIngredientRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            recipeIngredientRecyclerView.addItemDecoration(dividerItemDecoration)
        }


        val client = AsyncHttpClient()
        client.get("https://api.spoonacular.com/recipes/$recipeId/information?apiKey=$API_KEY", object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch recipe: $recipeId")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched recipe: $json")
                try {
                    val jsonObject = json.jsonObject.toString()
                    val recipeDetail = createJson().decodeFromString<RecipeDetail>(jsonObject)
                    val extendedIngredients = recipeDetail.extendedIngredients
                    if (extendedIngredients != null) {
                        for (ingredient in extendedIngredients) {
                            ingredients.add(ingredient)
                        }
                        Log.d("Ingredients",ingredients.size.toString())
                    }
                    ingredientAdapter.notifyDataSetChanged()
                    Log.i(TAG, recipeDetail.toString())

                    Glide.with(context)
                        .load(recipeDetail.image)
                        .into(recipeImageView)
                    recipeTitleTextView.text = recipeDetail.title
                    recipeDescriptionTextView.text = "Total Cooking Time:" + recipeDetail.cookingMinute.toString() + " minutes"



                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })

        //Instruction
        client.get("https://api.spoonacular.com/recipes/$recipeId/analyzedInstructions?apiKey=$API_KEY", object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch instruction: $recipeId")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched instruction: $json")
                try {
                    val instructionsArray = json.jsonArray.getJSONObject(0)
                    val instructionString = StringBuilder()
                    val steps = instructionsArray.getJSONArray("steps")
                    for (i in 0 until steps.length()) {
                        val step = steps.getJSONObject(i)
                        val stepNumber = step.getInt("number")
                        val stepDescription = step.getString("step")
                        instructionString.append("Step $stepNumber: $stepDescription\n\n\n")
                    }
                    procedureView.text = instructionString.toString()
                    Log.d(TAG, "Fetched Steps: $steps")


                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }

    companion object {
        private const val RECIPES_SEARCH_URL =
            "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=bebb87bcdf84410fb678b11fdc5e3840"
    }
}