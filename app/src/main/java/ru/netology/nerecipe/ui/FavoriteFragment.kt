package ru.netology.nerecipe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.FavoriteRecipeAdapter
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.databinding.FragmentFavoriteListBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.viewModel.FilterViewModel

import ru.netology.nerecipe.viewModel.RecipeViewModel

class FavoriteFragment : Fragment() {

    private val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)
    private val viewModelFilter by viewModels<FilterViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentFavoriteListBinding.inflate(inflater,container,false).also { binding->
        val adapter = FavoriteRecipeAdapter(viewModel)
        binding.notesList.adapter = adapter

        binding.navView.selectedItemId=R.id.favoriteFragment

        if(!RecipeRepository.searchText.isNullOrEmpty()){
            binding.additionalFragmentContainer.search.onActionViewExpanded()
            binding.additionalFragmentContainer.search.setQuery(RecipeRepository.searchText, true)
        }
        if(RecipeRepository.checkedCategories.isNotEmpty()){
            binding.additionalFragmentContainer.filter.setImageResource(R.drawable.ic_baseline_filter_alt_24dp)
        }
        else{
            binding.additionalFragmentContainer.filter.setImageResource(R.drawable.ic_outline_filter_alt_24dp)
        }


        binding.navView.setOnItemSelectedListener{ item ->
            findNavController().navigate(item.itemId)
            true
        }

        viewModel.favoriteData.observe(viewLifecycleOwner){posts ->
            setFilteredList(posts, binding, adapter)
        }

        viewModel.searchTextEvent.observe(viewLifecycleOwner){
            setFilteredList(viewModel.favoriteData.value, binding, adapter)
        }

        viewModelFilter.checkedCategoryEvent.observe(viewLifecycleOwner){
            val checkedCategory = RecipeRepository.checkedCategories
            if(checkedCategory.isNotEmpty()){
                binding.additionalFragmentContainer.filter.setImageResource(R.drawable.ic_baseline_filter_alt_24dp)
            }
            else{
                binding.additionalFragmentContainer.filter.setImageResource(R.drawable.ic_outline_filter_alt_24dp)
            }
            setFilteredList(viewModel.favoriteData.value, binding, adapter)
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
            findNavController().navigate(R.id.action_favoriteFragment_to_recipeContentFragment)
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
            findNavController().navigate(R.id.action_favoriteFragment_to_filterFragment)
        }

    }.root

    private fun setFilteredList(list: List<Recipe>?, binding: FragmentFavoriteListBinding, adapter: FavoriteRecipeAdapter) {
        val filteredPosts = viewModel.actualData(list)

        if(filteredPosts.isEmpty()){
            binding.notesList.visibility=GONE
            binding.emptyListInclude.emptyFrame.visibility= VISIBLE
        }
        else{
            binding.notesList.visibility=VISIBLE
            binding.emptyListInclude.emptyFrame.visibility= GONE
            adapter.submitList(filteredPosts)
        }

    }
}
