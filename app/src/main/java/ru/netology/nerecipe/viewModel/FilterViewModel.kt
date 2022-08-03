package ru.netology.nerecipe.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nerecipe.adapter.FilterListener
import ru.netology.nerecipe.data.RecipeRepository.Companion.checkedCategories
import ru.netology.nerecipe.util.SingleLiveEvent

class FilterViewModel(application: Application):AndroidViewModel(application), FilterListener {

    val checkedCategoryEvent = SingleLiveEvent<Unit?>()
    var checkedCategory = checkedCategories



    fun saveCategories(categories: List<String>){
        checkedCategories = categories
    }


    override fun onAddFilterItems(item: String) {
        val tmpCategory = checkedCategory.filter { category->category!=item }.plus(item)
        checkedCategory = tmpCategory
    }

    override fun onRemoveFilterItems(item: String) {
        val tmpCategory = checkedCategory.filter { category-> category != item }
        checkedCategory = tmpCategory
    }

}
