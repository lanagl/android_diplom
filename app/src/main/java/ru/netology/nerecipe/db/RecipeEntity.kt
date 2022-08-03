package ru.netology.nerecipe.db

import androidx.room.*


@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Long,
    @ColumnInfo(name="recipeName")
    val name: String,
    @ColumnInfo(name="author")
    val author: String,
    @ColumnInfo(name="category")
    val category: String,
    @ColumnInfo(name="description")
    val description: String,
    @ColumnInfo(name="isFavorite")
    val isFavorite: Boolean,
    @ColumnInfo(name="sort")
    val sort: Long
)

data class RecipeAndSteps(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val steps: List<StepsEntity>
)
