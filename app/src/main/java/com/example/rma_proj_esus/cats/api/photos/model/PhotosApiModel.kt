package com.example.rma_proj_esus.cats.api.photos.model


import kotlinx.serialization.Serializable

@Serializable
data class PhotosApiModel(
    val id:String,
    val url:String,
    val width:Int,
    val height:Int,
)