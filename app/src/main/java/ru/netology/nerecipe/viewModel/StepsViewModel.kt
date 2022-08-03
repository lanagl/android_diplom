package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nerecipe.adapter.StepsListener
import ru.netology.nerecipe.data.StepRepository
import ru.netology.nerecipe.data.impl.StepRepositoryImpl
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.dto.Step
import ru.netology.nerecipe.util.SingleLiveEvent

class StepsViewModel(application: Application):AndroidViewModel(application), StepsListener {
    private val repository: StepRepository = StepRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).stepsDao
    )

    val data by repository::data
    val currentStep = MutableLiveData<Step?>(null)
    val recipeRemoveEvent = SingleLiveEvent<Step?>()

    override fun onRemoveClicked(step: Step) {
        recipeRemoveEvent.value=step
    }

    override fun onSaveClicked(stepList: List<Step>) {

        repository.save(stepList)
    }

    fun addStep(step: Step){
        currentStep.value=step
    }

    fun removeByRecipeId(recipeId: Long){
        repository.removeByRecipeId(recipeId)
    }


}
