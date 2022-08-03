package ru.netology.nerecipe.adapter

interface FilterListener {
    fun onAddFilterItems(item: String)
    fun onRemoveFilterItems(item: String)
}
