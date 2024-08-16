package com.example.rma_proj_esus.cats.room_setup.photos

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.rma_proj_esus.cats.room_setup.cats.BreedsEntity

@Entity(
    tableName = "photos",
    foreignKeys = [ForeignKey(
        entity = BreedsEntity::class,
        parentColumns = ["id"],
        childColumns = ["breedId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["breedId"])]  // Add this line
)
data class PhotosEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val breedId: String, // Foreign key to BreedsEntity
    val url: String
)


