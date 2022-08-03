package ru.netology.nerecipe.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.CardRecipeBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.ui.RecipeContentFragment.Companion.recipeId

class FavoriteRecipeAdapter(
    private val interactionListener: RecipeListener
) : ListAdapter<Recipe, FavoriteRecipeAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: CardRecipeBinding,
        listener: RecipeListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        init {
            binding.favoriteButton.setOnClickListener { listener.onFavoriteClicked(recipe) }
        }

        fun bind(recipe: Recipe) {

            this.recipe = recipe

            with(binding) {


                recipeTitle.text = recipe.name
                recipeAuthor.text = recipe.author
                recipeDescription.text = recipe.description
                val catId = root.resources.getIdentifier(recipe.category,"string", root.context.packageName)
                recipeCategory.text = root.resources.getString(catId)
                favoriteButton.isChecked = recipe.isFavorite

                this.root.setOnClickListener {
                    linkToRecipe(it, recipe.id)
                }

            }


        }

        private fun linkToRecipe(view: View?, id: Long) {
            view?.findNavController()?.navigate(
                R.id.action_favoriteFragment_to_recipeFragment,
                Bundle().apply {
                    recipeId = id
                })
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

    }
}
