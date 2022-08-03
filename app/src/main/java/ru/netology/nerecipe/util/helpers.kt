package ru.netology.nerecipe.util

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.dto.Recipe


fun findMax(list: LiveData<List<Recipe>>): Long {
    var max = Long.MIN_VALUE
    if(list.value!=null){
        for (i in list.value!!) {
            max = maxOf(max, i.sort)
        }
    }
    return max
}

fun findMin(list: LiveData<List<Recipe>>): Long {
    var min = Long.MAX_VALUE
    if(list.value!=null){
        for (i in list.value!!) {
            min = minOf(min, i.sort)
        }
    }
    return min
}
