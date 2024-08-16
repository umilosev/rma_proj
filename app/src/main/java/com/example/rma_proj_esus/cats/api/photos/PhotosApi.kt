package com.example.rma_proj_esus.cats.api.photos

import com.example.rma_proj_esus.cats.api.photos.model.PhotosApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {
    @GET("images/search?")
    suspend fun getBreedImages(
        @Query("id") catID:String,
    ):List<PhotosApiModel>
}