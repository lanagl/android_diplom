package ru.netology.nerecipe.data.impl

import android.annotation.SuppressLint
import androidx.lifecycle.map
import ru.netology.nerecipe.data.StepRepository
import ru.netology.nerecipe.db.StepsDao
import ru.netology.nerecipe.db.toEntity
import ru.netology.nerecipe.db.toModel
import ru.netology.nerecipe.dto.Step

class StepRepositoryImpl(private val dao: StepsDao) : StepRepository {
    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    @SuppressLint("CheckResult")
    override fun move(step: Step, endPosition: Long) {

        dao.getStepsForRecipe(step.recipeId).map { stepList ->
            stepList.map {
                if (it.sort in step.sort..endPosition) {
                    val tmpStep = it.toModel().copy(sort = it.sort + 1)
                    dao.update(tmpStep.toEntity())
                }
            }
        }
        val newStep = step.copy(sort = endPosition)
        dao.update(newStep.toEntity())
    }

    override fun delete(step: Step) {
        dao.removeById(step.id)
    }

    override fun save(stepList: List<Step>) {
        val tmpStepList = stepList.map { step->step.toEntity() }
        dao.insertAll(tmpStepList)
    }

    override fun removeByRecipeId(recipeId:Long) {
        dao.removeByRecipeId(recipeId)
    }
}
