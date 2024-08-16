package com.example.rma_proj_esus.cats.room_setup.cats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreedsDao {

    @Query("SELECT * FROM breeds")
    suspend fun getAllBreeds(): List<BreedsEntity>

    @Query("SELECT * FROM breeds WHERE id = :breedId")
    suspend fun getBreedById(breedId: String): BreedsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreeds(breeds: List<BreedsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreed(breed: BreedsEntity)
}
