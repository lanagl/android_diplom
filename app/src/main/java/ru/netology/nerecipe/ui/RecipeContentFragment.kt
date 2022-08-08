package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.StepsAdapter
import ru.netology.nerecipe.data.StepRepository.Companion.CURRENT_RECIPE_ID
import ru.netology.nerecipe.data.StepRepository.Companion.NEW_STEP_ID
import ru.netology.nerecipe.databinding.FragmentRecipeContentBinding
import ru.netology.nerecipe.dto.Category
import ru.netology.nerecipe.dto.Step
import ru.netology.nerecipe.util.findMaxStepId
import ru.netology.nerecipe.viewModel.RecipeViewModel
import ru.netology.nerecipe.viewModel.StepsViewModel


class RecipeContentFragment : Fragment() {
    private val viewModelSteps by viewModels<StepsViewModel>(ownerProducer = ::requireParentFragment)
    private val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)
    private var stepsList = mutableListOf<Step>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRecipeContentBinding.inflate(inflater, container, false).also { binding ->

        val stepsAdapter = StepsAdapter(viewModelSteps)

        binding.stepsList.adapter = stepsAdapter

        container?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility= GONE

        val id = this.arguments?.recipeId

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            val currentRecipe = recipes.find {
                it.id == id
            }
            with(binding) {
                title.setText(currentRecipe?.name ?: "")
                description.setText(currentRecipe?.description ?: "")
                val adapter =
                    context?.let { ArrayAdapter(it, R.layout.spinner_item, viewModel.categories) }

                category.adapter=adapter

                val currentCategory = viewModel.categories.find { category -> category.id == currentRecipe?.category }

                val position = adapter?.getPosition(currentCategory)

                if (position != null) {
                    category.setSelection(position)
                }
                title.requestFocus()
            }
        }
        viewModelSteps.data.observe(viewLifecycleOwner) { steps ->
            val tmpSteps = steps.filter {
                it.recipeId == id
            }
            stepsList = tmpSteps.toMutableList()
            stepsAdapter.submitList(stepsList)
        }


        viewModelSteps.currentStep.observe(viewLifecycleOwner) {
            stepsAdapter.submitList(stepsList)
        }

        binding.saveButton.setOnClickListener {
            val title = binding.title.text
            val description = binding.description.text
            val category: Category = binding.category.selectedItem as Category


            if (!title.isNullOrBlank() && !description.isNullOrBlank()
            ) {
                viewModel.onSaveButtonClicked(
                    description.toString(),
                    title.toString(),
                    category.id
                )
                if (stepsList.size > 0 && CURRENT_RECIPE_ID != 0L) {
                    val newStepsList = stepsList.map {
                        it.copy(id = NEW_STEP_ID, recipeId = CURRENT_RECIPE_ID)
                    }
                    viewModelSteps.removeByRecipeId(CURRENT_RECIPE_ID)
                    viewModelSteps.onSaveClicked(newStepsList)
                    CURRENT_RECIPE_ID = 0L
                }
            }
            findNavController().navigateUp()
        }

        binding.addButton.setOnClickListener {
            val stepItem = binding.step.text.toString()
            binding.step.text = null
            val newStep = Step(id = NEW_STEP_ID, content = stepItem, sort = 0, recipeId = findMaxStepId(stepsList) +1)
            stepsList.add(newStep)
            viewModelSteps.addStep(newStep)
        }

        viewModelSteps.recipeRemoveEvent.observe(viewLifecycleOwner) {
            stepsList.remove(it)

            stepsAdapter.submitList(stepsList)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }.root


    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY_DESCRIPTION = "recipeForSaveContent"
        const val RESULT_KEY_TITLE = "recipeForSaveTitle"
        const val RESULT_KEY_CATEGORY = "recipeForSaveCategory"
        private const val RECIPE_ID_KEY = "RECIPE_ID_KEY"

        var Bundle.recipeId: Long
            set(value) = putLong(RECIPE_ID_KEY, value)
            get() = getLong(RECIPE_ID_KEY)
    }
}
