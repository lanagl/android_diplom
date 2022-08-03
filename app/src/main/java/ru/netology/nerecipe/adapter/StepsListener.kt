package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.dto.Step

interface StepsListener {
    fun onRemoveClicked(step: Step)
    fun onSaveClicked(stepList: List<Step>)
}
