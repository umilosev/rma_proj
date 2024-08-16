package com.example.rma_proj_esus.cats.room_setup.photos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotosDao {
    @Insert
    suspend fun insertPhotos(photos: List<PhotosEntity>)

    @Query("SELECT * FROM photos WHERE breedId = :breedId")
    suspend fun getPhotosForBreed(breedId: String): List<PhotosEntity>

    @Query("SELECT COUNT(*) FROM photos WHERE breedId = :breedId")
    suspend fun getPhotoCountForBreed(breedId: String): Int
}
