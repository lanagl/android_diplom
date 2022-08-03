package ru.netology.nerecipe.db

import ru.netology.nerecipe.dto.Step


internal fun StepsEntity.toModel() = Step(
    id = id,
    content = content,
    sort = sort,
    recipeId = recipeId
)

internal fun Step.toEntity() = StepsEntity(
    id = id,
    content = content,
    sort = sort,
    recipeId = recipeId
)
