package ru.netology.nerecipe.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.CardRecipeBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.ui.RecipeContentFragment.Companion.recipeId
import ru.netology.nerecipe.util.findMax
import ru.netology.nerecipe.util.findMin

class RecipeAdapter(
    private val interactionListener: RecipeListener,
    private val recipesList: LiveData<List<Recipe>>
) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener, recipesList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: CardRecipeBinding,
        listener: RecipeListener,
        recipesList: LiveData<List<Recipe>>
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val maxSort = findMax(recipesList)
        private val minSort = findMin(recipesList)



        init {
            binding.favoriteButton.setOnClickListener { listener.onFavoriteClicked(recipe) }
            val popupMenu by lazy {

                PopupMenu(binding.root.context, binding.optionsDnd).apply {
                    inflate(R.menu.drag_and_drop_menu)
                    when (recipe.sort) {
                        maxSort -> {
                            this.menu.findItem(R.id.down).isVisible = true
                            this.menu.findItem(R.id.up).isVisible = false
                        }
                        minSort -> {
                            this.menu.findItem(R.id.up).isVisible = true
                            this.menu.findItem(R.id.down).isVisible = false
                        }
                        else -> {
                            this.menu.findItem(R.id.up).isVisible = true
                            this.menu.findItem(R.id.down).isVisible = true
                        }
                    }
                    setOnMenuItemClickListener { menuItem ->

                        when (menuItem.itemId) {

                            R.id.up -> {
                                listener.onMoveUpClicked(recipe.id)
                                binding.optionsDnd.visibility = GONE
                                true
                            }
                            R.id.down -> {
                                listener.onMoveDownClicked(recipe.id)
                                binding.optionsDnd.visibility = GONE
                                true
                            }
                            R.id.remove -> {
                                if (recipe != null) {
                                    listener.onDeleteClicked(recipe)

                                }
                                true
                            }
                            R.id.edit -> {
                                if (recipe != null) {
                                    listener.onEditClicked(recipe)
                                }
                                true
                            }
                            else -> {
                                binding.optionsDnd.visibility = GONE
                                false
                            }
                        }

                    }
                }

            }

            binding.optionsDnd.setOnClickListener {
                popupMenu.show()
            }
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
                optionsDnd.visibility = GONE


                root.setOnClickListener {
                    linkToRecipe(it, recipe.id)
                    optionsDnd.visibility = GONE
                }

                root.setOnLongClickListener{
                    optionsDnd.visibility = VISIBLE
                    true
                }


            }


        }

        private fun linkToRecipe(view: View?, id: Long) {
            view?.findNavController()?.navigate(
                R.id.action_listFragment_to_recipeFragment,
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
