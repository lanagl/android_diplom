package ru.netology.nerecipe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "steps", foreignKeys = [ForeignKey(
    entity = RecipeEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("recipeId"),
    onDelete = ForeignKey.CASCADE
)])
class StepsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Long,
    @ColumnInfo(name="content")
    val content: String?,
    @ColumnInfo(name="sort")
    val sort: Long,
    @ColumnInfo(name="recipeId")
    val recipeId: Long
)
