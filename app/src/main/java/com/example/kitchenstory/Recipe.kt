package com.example.kitchenstory

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SearchRecipesResponse(
    @SerialName("recipes")
    val recipes: List<Recipe>?
)

@Keep
@Serializable
data class Recipe(
    @SerialName("id")
    val id: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("imageType")
    val imageType: String?,
    @SerialName("usedIngredientCount")
    val usedIngredientCount: Int?,
    @SerialName("missedIngredientCount")
    val missedIngredientCount: Int?,
    @SerialName("usedIngredients")
    val usedIngredients: List<Ingredient>?,
    @SerialName("unusedIngredients")
    val unusedIngredients: List<Ingredient>?,
)

@Keep
@Serializable
data class Ingredient(
    @SerialName("id")
    val id: Int?,
    @SerialName("amount")
    val amount: Float?,
    @SerialName("unit")
    val unit: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("aisle")
    val aisle: String?,
    @SerialName("originalName")
    val originalName: String?,
    @SerialName("image")
    val image: String?,
)

@Keep
@Serializable
data class RecipeDetail(
    @SerialName("id")
    val id: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("summary")
    val summary: String?,
    @SerialName("dishTypes")
    val dishTypes: List<String>?,
    @SerialName("extendedIngredients")
    val extendedIngredients: List<Ingredient>?,
    @SerialName("instructions")
    val instructions: String?,
    @SerialName("analyzedInstructions")
    val analyzedInstructions: List<AnalyzedInstruction>?,
)

@Keep
@Serializable
data class AnalyzedInstruction(
    @SerialName("name")
    val name: String?,
    @SerialName("steps")
    val steps: List<AnalyzedInstructioStep>?,
)

@Keep
@Serializable
data class AnalyzedInstructioStep(
    @SerialName("number")
    val number: Int?,
    @SerialName("step")
    val step: String?,
    @SerialName("ingredients")
    val ingredients: List<LocalIngredient>?,
)

@Keep
@Serializable
data class LocalIngredient(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("localizedName")
    val localizedName: String?,
    @SerialName("image")
    val image: String?,
)