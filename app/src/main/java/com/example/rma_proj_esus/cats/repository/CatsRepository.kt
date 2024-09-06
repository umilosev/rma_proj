package com.example.rma_proj_esus.cats.repository

import com.example.rma_proj_esus.cats.room_setup.cats.BreedsDao
import android.content.Context
import android.util.Log
import com.example.rma_proj_esus.cats.api.breeds.BreedsApi
import com.example.rma_proj_esus.cats.api.breeds.model.BreedsApiModel
import com.example.rma_proj_esus.cats.api.photos.PhotosApi
import com.example.rma_proj_esus.cats.api.photos.model.PhotosApiModel
import com.example.rma_proj_esus.cats.room_setup.cats.BreedsEntity
import com.example.rma_proj_esus.cats.room_setup.cats.CatsDatabase
import com.example.rma_proj_esus.cats.room_setup.photos.PhotosDao
import com.example.rma_proj_esus.cats.room_setup.photos.PhotosEntity
import com.example.rma_proj_esus.networking.catBreedsRetrofit


/**
 * Single source of truth for passwords.
 */
object CatsRepository {

    private val breedsApi: BreedsApi = catBreedsRetrofit.create(BreedsApi::class.java)
    private val photosApi: PhotosApi = catBreedsRetrofit.create(PhotosApi::class.java)
    private lateinit var breedsDao: BreedsDao
    private lateinit var photosDao: PhotosDao
    fun initialize(context: Context) {
        Log.d("CatsRepository", "Initializing repository")
        val database = CatsDatabase.getDatabase(context)
        breedsDao = database.breedsDao()
        photosDao = database.photosDao()
        Log.d("BreedsDao", "com.example.rma_proj_esus.cats.room_setup.cats.BreedsDao is initialized: $breedsDao")
        Log.d("CatsRepository", "Database initialized: $database")
    }

    /**
     * Simulates api network request which downloads sample data
     * from network and updates passwords in this repository.
     */
    suspend fun fetchBreeds(): List<BreedsEntity> {
        // Check if breeds are available in the local database
        var breeds = breedsDao.getAllBreeds()
        if (breeds.isEmpty()) {
            // If not, fetch from the API
            val apiBreeds = breedsApi.getAllBreeds()
            // Convert API models to Room entities
            breeds = apiBreeds.map { it.toEntity() }
            // Save them in the local database
            breedsDao.insertBreeds(breeds)
        }
        return breeds
    }

    /**
     * Simulates api network request which updates single password.
     * It does nothing. Just waits for 1 second.
     */
    suspend fun fetchBreedDetails(catID: String): BreedsEntity {
        // First, try to get the breed from the local database
        val breed = breedsDao.getBreedById(catID)
        if (breed != null) {
            return breed
        } else {
            // If not found, fetch from the API
            val apiBreed = breedsApi.getBreed(catID)
            val entity = apiBreed.toEntity()
            // Save it in the local database
            breedsDao.insertBreed(entity)
            return entity
        }
    }

    suspend fun fetchBreedPhotos(catID: String): List<PhotosEntity> {
        // Check if photos for this breed are available in the local database
        val photos = photosDao.getPhotosForBreed(catID)
        return if (photos.isNotEmpty()) {
            photos
        } else {
            // If not, fetch from the API
            val apiPhotos = photosApi.getBreedImages(catID)
            val entities = apiPhotos.map { it.toEntity(catID) }
            // Save them in the local database
            photosDao.insertPhotos(entities)
            entities
        }
    }

    fun BreedsApiModel.toEntity(): BreedsEntity {
        return BreedsEntity(
            id = this.id,
            name = this.name,
            temperament = this.temperament,
            origin = this.origin,
            description = this.description,
            life_span = this.life_span,
            alt_names = this.alt_names,
            weight = this.weight.toString(),
            indoor = this.indoor,
            lap = this.lap,
            adaptability = this.adaptability,
            affection_level = this.affection_level,
            child_friendly = this.child_friendly,
            dog_friendly = this.dog_friendly,
            energy_level = this.energy_level,
            grooming = this.grooming,
            health_issues = this.health_issues,
            intelligence = this.intelligence,
            shedding_level = this.shedding_level,
            social_needs = this.social_needs,
            stranger_friendly = this.stranger_friendly,
            vocalisation = this.vocalisation,
            experimental = this.experimental,
            hairless = this.hairless,
            natural = this.natural,
            rare = this.rare,
            rex = this.rex,
            suppressed_tail = this.suppressed_tail,
            short_legs = this.short_legs,
            wikipedia_url = this.wikipedia_url
        )
    }

    fun PhotosApiModel.toEntity(breedId: String): PhotosEntity {
        return PhotosEntity(
            breedId = breedId, // Link the photo to the breed
            url = this.url
        )
    }

}
