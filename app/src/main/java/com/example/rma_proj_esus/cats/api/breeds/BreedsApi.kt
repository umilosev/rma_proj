package com.example.rma_proj_esus.cats.api.breeds

import com.example.rma_proj_esus.cats.api.breeds.model.BreedsApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface BreedsApi {
    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedsApiModel>

    @GET("breeds/{id}")
    suspend fun getBreed(
        @Path("id") breedId: String,
    ): BreedsApiModel

}


