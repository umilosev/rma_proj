package com.example.rma_proj_esus.cats.details.model

import com.example.rma_proj_esus.cats.api.breeds.model.Weight

data class CatBreedUiModel (
    val id:String,
    val name: String,
    val temperament:String="",
    val origin:String="",
    val description:String,
    val life_span:String,
    val alt_names:String,
    val weight: String,
    val indoor: Int,
    val lap : Int,
    val adaptability : Int,
    val affection_level: Int,
    val child_friendly : Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val grooming: Int,
    val health_issues: Int,
    val intelligence: Int,
    val shedding_level: Int,
    val social_needs: Int,
    val stranger_friendly: Int,
    val vocalisation: Int,
    val experimental: Int,
    val hairless: Int,
    val natural: Int,
    val rare: Int,
    val rex: Int,
    val suppressed_tail: Int,
    val short_legs: Int,
    val wikipedia_url :String?,
)

