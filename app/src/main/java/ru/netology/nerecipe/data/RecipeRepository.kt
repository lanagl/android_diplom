package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.dto.Recipe

interface RecipeRepository {
    val data: LiveData<List<Recipe>>
    val favoriteData: LiveData<List<Recipe>>
    fun getRecipeById(recipeId: Long)
    fun delete(recipeId: Long)
    fun save(recipe: Recipe): Long
    fun setFavorite(recipeId: Long)
    fun moveUp(recipeId: Long)
    fun moveDown(recipeId: Long)

    companion object {
        const val NEW_RECIPE_ID = 0L
        var checkedCategories = listOf<String>()
        var searchText: String? = null
    }
}
