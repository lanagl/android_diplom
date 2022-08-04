package ru.netology.nerecipe.util

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.Step


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

fun findMaxStepId(list: List<Step>): Long {
    var max = Long.MIN_VALUE
    for (i in list) {
        max = maxOf(max, i.sort)
    }
    return max
}

fun findMinStepId(list: List<Step>): Long {
    var min = Long.MAX_VALUE
    for (i in list) {
        min = minOf(min, i.sort)
    }
    return min
}
