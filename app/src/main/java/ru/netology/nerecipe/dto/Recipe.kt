package ru.netology.nerecipe.dto

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long,
    val name: String,
    val author: String,
    val category: String,
    val description: String,
    val isFavorite: Boolean,
    val sort: Long,
    )
