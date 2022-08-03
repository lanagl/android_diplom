package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.FilterAdapter
import ru.netology.nerecipe.databinding.FragmentFilterBinding
import ru.netology.nerecipe.viewModel.FilterViewModel
import ru.netology.nerecipe.viewModel.RecipeViewModel

class FilterFragment : Fragment() {

    private val viewModel by viewModels<FilterViewModel>(ownerProducer = ::requireParentFragment)
    private val viewModelRecipe by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentFilterBinding.inflate(inflater,container,false).also { binding->

        val filterAdapter = FilterAdapter(viewModel)

        binding.filterList.adapter=filterAdapter
        filterAdapter.submitList(viewModelRecipe.categories)

        container?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility= View.GONE

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.saveButton.setOnClickListener {
            val checkedCategory = viewModel.checkedCategory
            viewModel.saveCategories(checkedCategory)
            viewModel.checkedCategoryEvent.call()
            findNavController().navigateUp()
        }

    }.root
}
