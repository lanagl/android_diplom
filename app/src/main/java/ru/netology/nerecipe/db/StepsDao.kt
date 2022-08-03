package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StepsDao {
    @Query("SELECT * FROM steps ORDER BY sort DESC")
    fun getAll(): LiveData<List<StepsEntity>>

    @Query("SELECT * FROM steps WHERE recipeId=:recipeId ORDER BY sort DESC")
    fun getStepsForRecipe(recipeId: Long): LiveData<List<StepsEntity>>

    @Transaction
    @Insert
    fun insertAll(steps: List<StepsEntity>)

    @Update
    fun update(step: StepsEntity)

    @Query("DELETE FROM steps WHERE id=:id")
    fun removeById(id: Long)

    @Query("DELETE FROM steps WHERE recipeId=:id")
    fun removeByRecipeId(id: Long)
}
