package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.dto.Recipe

interface RecipeListener {
    fun onDeleteClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onFavoriteClicked(recipe: Recipe)
    fun onMoveUpClicked(recipeId:Long)
    fun onMoveDownClicked(recipeId:Long)
}
