package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Transaction
    @Query("SELECT * FROM recipes ORDER BY sort DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY sort DESC")
    fun getAllFavorite(): LiveData<List<RecipeEntity>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getByRecipeId(id: Long): RecipeAndSteps

    @Transaction
    @Query("SELECT * FROM recipes WHERE sort = :sort")
    fun getByRecipeSort(sort: Long): RecipeEntity?

    @Transaction
    @Insert
    fun insert(recipe: RecipeEntity): Long

    @Update
    fun update(recipe: RecipeEntity): Int

    @Query("DELETE FROM recipes WHERE id=:id")
    fun removeById(id: Long)

    @Query("""UPDATE recipes SET 
        isFavorite = CASE WHEN isFavorite THEN 0 ELSE 1 END
        WHERE id=:id
    """)
    fun setFavorite(id: Long)

    @Query("""UPDATE recipes SET 
        sort = :newSort
        WHERE id=:id
    """)
    fun setSort(id: Long, newSort:Long)
}
