package com.example.kitchenstory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.BuildConfig
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "ViewRecipes/"

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

class ViewRecipes : AppCompatActivity() {
    private lateinit var recipesRecyclerView: RecyclerView
//    private lateinit var binding: ActivityMainBinding
    private val recipes = mutableListOf<Recipe>()
    private val API_KEY = BuildConfig.API_KEY
    private val RECIPES_SEARCH_URL = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=$API_KEY"
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "ON CREATE!!")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_recipes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recipesRecyclerView = findViewById(R.id.recipesRV)
        val recipeAdapter = RecipeAdapter(this, recipes)
        recipesRecyclerView.adapter = recipeAdapter

        recipesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            recipesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        val client = AsyncHttpClient()
        client.get(RECIPES_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch recipes: $statusCode, Response: $response\", throwable")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched recipes: $json")
                try {
                    val jsonString = json.jsonArray.toString()
                    val parsedJson = createJson().decodeFromString<Array<Recipe>>(jsonString)

                    parsedJson.let { list ->
                        recipes.addAll(list)
                    }
                    recipeAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })

    }
}