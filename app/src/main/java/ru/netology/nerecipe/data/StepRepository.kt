package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.dto.Step

interface StepRepository {
    val data: LiveData<List<Step>>
    fun move(step: Step, endPosition: Long)
    fun delete(step: Step)
    fun save(stepList: List<Step>)
    fun removeByRecipeId(recipeId: Long)

    companion object {
        const val NEW_STEP_ID = 0L
        var CURRENT_RECIPE_ID = 0L
    }
}
