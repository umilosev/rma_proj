package com.example.rma_proj_esus.cats.room_setup.cats

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rma_proj_esus.cats.room_setup.photos.PhotosDao
import com.example.rma_proj_esus.cats.room_setup.photos.PhotosEntity

@Database(
    entities = [BreedsEntity::class, PhotosEntity::class],
    version = 2,
    exportSchema = true
)
abstract class CatsDatabase : RoomDatabase() {
    abstract fun breedsDao(): BreedsDao
    abstract fun photosDao(): PhotosDao

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // SQL statement to create the new table
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS `photos` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `breedId` TEXT NOT NULL,
                `url` TEXT NOT NULL,
                FOREIGN KEY(`breedId`) REFERENCES `BreedsEntity`(`id`) ON DELETE CASCADE
            )
        """)
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: CatsDatabase? = null

        fun getDatabase(context: Context): CatsDatabase {
            Log.d("Provera","We're so in")
            return INSTANCE ?: synchronized(this) {
                Log.d("Context","Context$context")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CatsDatabase::class.java,
                    "cats_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                Log.d("CatsDatabase","Esus pomogi $INSTANCE")
                Log.d("Finish", "We're so back")
                instance
            }
        }
    }
}
