package ru.netology.nerecipe.dto

data class Category(
    val id: String,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}
