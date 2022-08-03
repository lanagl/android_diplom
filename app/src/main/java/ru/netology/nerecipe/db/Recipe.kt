package ru.netology.nerecipe.db

import ru.netology.nerecipe.dto.Recipe


internal fun RecipeEntity.toModel() = Recipe(
    id = id,
    name = name,
    author = author,
    category = category,
    description = description,
    isFavorite= isFavorite,
    sort = sort
)

internal fun Recipe.toEntity() = RecipeEntity(
    id = id,
    name = name,
    author = author,
    category = category,
    description = description,
    isFavorite= isFavorite,
    sort = sort
)
