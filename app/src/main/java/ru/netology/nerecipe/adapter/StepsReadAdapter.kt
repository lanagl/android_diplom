package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.databinding.StepItemBinding
import ru.netology.nerecipe.dto.Step

internal class StepsReadAdapter(
    private val interactionListener: StepsListener
):ListAdapter<Step, StepsReadAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StepItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: StepItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var step: Step


        fun bind(step: Step) {

            this.step = step

            with(binding) {
                stepDescription.text=step.content
                removeButton.visibility = GONE
            }
        }


    }




    private object DiffCallback : DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem == newItem
        }

    }
}
