package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.StepsReadAdapter
import ru.netology.nerecipe.databinding.FragmentRecipeBinding
import ru.netology.nerecipe.ui.RecipeContentFragment.Companion.recipeId
import ru.netology.nerecipe.viewModel.RecipeViewModel
import ru.netology.nerecipe.viewModel.StepsViewModel

class RecipeFragment : Fragment() {

    private val viewModelSteps by viewModels<StepsViewModel>(ownerProducer = ::requireParentFragment)
    private val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentRecipeBinding.inflate(inflater,container,false).also { binding->

        val stepsAdapter = StepsReadAdapter(viewModelSteps)
        binding.stepsList.adapter=stepsAdapter
        container?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility= GONE

        val id = this.arguments?.recipeId

        val recipe = viewModel.data.value?.find {
            it.id == id
        }

        val popupMenu by lazy {
            PopupMenu(context, binding.submenu).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            if (recipe != null) {
                                viewModel.onDeleteClicked(recipe)
                                findNavController().navigateUp()
                            }
                            true
                        }
                        R.id.edit -> {
                            if (recipe != null) {
                                viewModel.onEditClicked(recipe)
                            }
                            true
                        }
                        else -> false
                    }

                }
            }
        }

        viewModel.data.observe(viewLifecycleOwner){recipes->
            val currentRecipe = recipes.find {
                it.id == id
            }

                with(binding) {
                    title.text = currentRecipe?.name
                    description.text = currentRecipe?.description
                    description.text = currentRecipe?.description

                    val recipeCategory = currentRecipe?.category?:R.string.European.toString()

                    val catId = root.resources.getIdentifier(
                        recipeCategory,
                        "string",
                        root.context.packageName
                    )

                    category.text = root.resources.getString(catId)
                    submenu.setOnClickListener { popupMenu.show() }


            }
        }




        viewModelSteps.data.observe(viewLifecycleOwner){steps ->
            val currentSteps = steps.filter {
                it.recipeId == id
            }
            stepsAdapter.submitList(currentSteps)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.navigateToEditRecipeContentScreenEvent.observe(viewLifecycleOwner){
            if (id != null) {
            findNavController().navigate(R.id.action_recipeFragment_to_recipeContentFragment,
                Bundle().apply {
                    recipeId = id
                })
            }
        }

    }.root
}
