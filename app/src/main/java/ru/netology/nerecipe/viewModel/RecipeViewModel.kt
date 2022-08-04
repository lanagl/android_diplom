package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.RecipeListener
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.RecipeRepository.Companion.checkedCategories
import ru.netology.nerecipe.data.RecipeRepository.Companion.searchText
import ru.netology.nerecipe.data.StepRepository.Companion.CURRENT_RECIPE_ID
import ru.netology.nerecipe.data.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.dto.Category
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.util.findMax
import ru.netology.nerecipe.util.SingleLiveEvent

class RecipeViewModel(
    application: Application
):AndroidViewModel(application),
RecipeListener{
    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )

    val categories: List<Category> = listOf(
        Category("European", application.resources.getString(R.string.European)),
        Category("Asian", application.resources.getString(R.string.Asian)),
        Category("PanAsian", application.resources.getString(R.string.PanAsian)),
        Category("Eastern", application.resources.getString(R.string.Eastern)),
        Category("American", application.resources.getString(R.string.American)),
        Category("Russian", application.resources.getString(R.string.Russian)),
        Category("Mediterranean", application.resources.getString(R.string.Mediterranean))
    )

    val data by repository::data
    val favoriteData by repository::favoriteData
    private val currentRecipe = MutableLiveData<Recipe?>(null)
    val navigateToRecipeContentScreenEvent = SingleLiveEvent<Unit?>()
    val navigateToEditRecipeContentScreenEvent = SingleLiveEvent<Long>()
    val searchTextEvent = SingleLiveEvent<Unit?>()




    fun onSaveButtonClicked(content: String, title: String, category: String ) {
        if(content.isBlank()) return
        val newRecipe = currentRecipe.value?.copy(description = content, name = title, category=category)?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            author = "Author",
            description = content,
            name = title,
            category = category,
            isFavorite = false,
            sort = findMax(data) +1
        )
        currentRecipe.value = null
        val recipeId = repository.save(newRecipe)
        CURRENT_RECIPE_ID = recipeId
    }

    override fun onDeleteClicked(recipe: Recipe) {
        repository.delete(recipe.id)
    }

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value=recipe
        navigateToEditRecipeContentScreenEvent.value = recipe.id
    }

    override fun onFavoriteClicked(recipe: Recipe) {
        repository.setFavorite(recipe.id)
    }

    override fun onMoveUpClicked(recipeId: Long) {
        repository.moveUp(recipeId)
    }

    override fun onMoveDownClicked(recipeId: Long) {
        repository.moveDown(recipeId)
    }

    fun setSearchText(text: String?){
        searchText = text
        searchTextEvent.call()
    }

    fun actualData(startData: List<Recipe>?): List<Recipe> {
        val checkedCategory = checkedCategories
        val filterData = if (checkedCategory.isNotEmpty()){
            startData?.filter { item-> checkedCategory.find { cat-> cat == item.category } ==item.category }
        } else startData

        val searchData = if(!searchText.isNullOrEmpty()){
            filterData?.filter { item->
                item.description.contains(searchText!!) || item.author.contains(searchText!!) || item.name.contains(searchText!!)
            } ?: emptyList()
        } else filterData

        return searchData?: emptyList()
    }

}
