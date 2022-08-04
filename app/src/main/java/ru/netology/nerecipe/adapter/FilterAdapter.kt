package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.data.RecipeRepository.Companion.checkedCategories
import ru.netology.nerecipe.databinding.FilterItemBinding
import ru.netology.nerecipe.dto.Category


internal class FilterAdapter(
    private val interactionListener: FilterListener
):ListAdapter<Category, FilterAdapter.ViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Category>,
        currentList: MutableList<Category>
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun submitList(list: List<Category>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class ViewHolder(
        private val binding: FilterItemBinding,
        listener: FilterListener
    ) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.checkFilterItem.setOnCheckedChangeListener { _, isChecked ->
                val id = binding.checkFilterName.text
                if(isChecked) {
                    listener.onAddFilterItems(id.toString())
                }
                else{
                    listener.onRemoveFilterItems(id.toString())
                }
            }
        }

        fun bind(item: Category) {
            val checkedCategory = checkedCategories

            with(binding) {
                checkFilterItem.text=item.name
                checkFilterName.text=item.id
                checkFilterItem.isChecked = (item.id in checkedCategory || checkedCategory.isEmpty())
            }
        }

    }




    private object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }


}
