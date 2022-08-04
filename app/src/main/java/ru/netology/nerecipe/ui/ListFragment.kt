package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.RecipeAdapter
import ru.netology.nerecipe.data.RecipeRepository.Companion.checkedCategories
import ru.netology.nerecipe.data.RecipeRepository.Companion.searchText
import ru.netology.nerecipe.databinding.FragmentListBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.ui.RecipeContentFragment.Companion.recipeId
import ru.netology.nerecipe.viewModel.FilterViewModel
import ru.netology.nerecipe.viewModel.RecipeViewModel

class ListFragment : Fragment() {

    private val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)
    private val viewModelFilter by viewModels<FilterViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentListBinding.inflate(inflater,container,false).also { binding->
        val listRecipes = viewModel.data
        val adapter = RecipeAdapter(viewModel, listRecipes)
        binding.recipesList.adapter = adapter

        if(!searchText.isNullOrEmpty()){
            binding.additionalFragmentContainer.search.onActionViewExpanded()
            binding.additionalFragmentContainer.search.setQuery(searchText, true)
        }

        if(checkedCategories.isNotEmpty()){
            binding.additionalFragmentContainer.filter.setImageResource(R.drawable.ic_baseline_filter_alt_24dp)
        }
        else{
            binding.additionalFragmentContainer.filter.setImageResource(R.drawable.ic_outline_filter_alt_24dp)
        }

        viewModel.data.observe(viewLifecycleOwner){posts ->
            setFilteredList(posts, binding, adapter)
        }

        viewModel.searchTextEvent.observe(viewLifecycleOwner){
            setFilteredList(viewModel.data.value, binding, adapter)
        }

        viewModelFilter.checkedCategoryEvent.observe(viewLifecycleOwner){
            val checkedCategory = checkedCategories
            if(checkedCategory.isNotEmpty()){
                binding.additionalFragmentContainer.filter.setImageResource(R.drawable.ic_baseline_filter_alt_24dp)
            }
            else{
                binding.additionalFragmentContainer.filter.setImageResource(R.drawable.ic_outline_filter_alt_24dp)
            }
            setFilteredList(viewModel.data.value, binding, adapter)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_recipeContentFragment)
        }


        setFragmentResultListener(requestKey = RecipeContentFragment.REQUEST_KEY){
                requestKey, bundle ->
            if (requestKey!= RecipeContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipeTitle = bundle.getString(RecipeContentFragment.RESULT_KEY_TITLE)?:return@setFragmentResultListener
            val newRecipeDescription = bundle.getString(RecipeContentFragment.RESULT_KEY_DESCRIPTION)?:return@setFragmentResultListener
            val newRecipeCategory = bundle.getString(RecipeContentFragment.RESULT_KEY_CATEGORY)?:return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newRecipeDescription,newRecipeTitle,newRecipeCategory)
        }

        viewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.action_listFragment_to_recipeContentFragment)
        }


        binding.navView.setOnItemSelectedListener{ item ->
            findNavController().navigate(item.itemId)
            item.isEnabled = true
            true
        }

        binding.additionalFragmentContainer.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(s: String): Boolean {
                viewModel.setSearchText(s)
                return true
            }

            override fun onQueryTextSubmit(s: String): Boolean {
                viewModel.setSearchText(s)
                return true
            }
        })

        binding.additionalFragmentContainer.filter.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_filterFragment)
        }

        viewModel.navigateToEditRecipeContentScreenEvent.observe(viewLifecycleOwner){
            val id = it
            if (id != null) {
                findNavController().navigate(R.id.action_listFragment_to_recipeContentFragment,
                    Bundle().apply {
                        recipeId = id
                    })
            }
        }


    }.root

    private fun setFilteredList(list: List<Recipe>?, binding: FragmentListBinding, adapter: RecipeAdapter) {
        val filteredPosts = viewModel.actualData(list)

        if(filteredPosts.isEmpty()){
            binding.recipesList.visibility=GONE
            binding.emptyListInclude.emptyFrame.visibility= VISIBLE
        }
        else{
            binding.recipesList.visibility=VISIBLE
            binding.emptyListInclude.emptyFrame.visibility= GONE
            adapter.submitList(filteredPosts)
        }

    }

}
