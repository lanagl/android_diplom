package ru.netology.nerecipe.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.db.RecipeDao
import ru.netology.nerecipe.db.toEntity
import ru.netology.nerecipe.db.toModel
import ru.netology.nerecipe.dto.Recipe

class RecipeRepositoryImpl(private val dao: RecipeDao): RecipeRepository {

    override val data: LiveData<List<Recipe>> = dao.getAll().map { entities->
        entities.map { it.toModel() }
    }
    override val favoriteData: LiveData<List<Recipe>> = dao.getAllFavorite().map { entities->
        entities.map { it.toModel() }
    }


    override fun getRecipeById(recipeId: Long) {
        dao.getByRecipeId(recipeId)
    }

    override fun delete(recipeId: Long) {
        dao.removeById(recipeId)
    }

    override fun save(recipe: Recipe): Long {
        val id = recipe.id
        return if(id ==0L){
            dao.insert(recipe.toEntity())
        } else {
            dao.update(recipe.toEntity()).toLong()
            id
        }
    }



    override fun setFavorite(recipeId: Long) {
        dao.setFavorite(recipeId)
    }

    override fun moveUp(recipeId: Long) {
        val item = dao.getByRecipeId(recipeId).recipe.toModel()
        val oldSort = item.sort
        findForSwap(oldSort, +1, recipeId)
    }

    override fun moveDown(recipeId: Long) {
        val item = dao.getByRecipeId(recipeId).recipe.toModel()
        val oldSort = item.sort
        findForSwap(oldSort, -1, recipeId)
    }

    private fun swap(fromItemId:Long, toItemId:Long, fromSort:Long, toSort:Long){
        dao.setSort(toItemId, -10L)
        dao.setSort(fromItemId, toSort)
        dao.setSort(toItemId, fromSort)
    }
    private fun findForSwap(oldSort: Long, step: Long, recipeId: Long){
        val nextItem = dao.getByRecipeSort(oldSort+step)?.toModel()
        if(nextItem!=null){
            swap(nextItem.id, recipeId, nextItem.sort, oldSort)
        }
        else findForSwap(oldSort+step, step, recipeId)
    }

}
