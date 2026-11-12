package fr.estiam.traillog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trails")
data class TrailEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    val region: String,

    val distanceKm: Double,

    val elevationGainM: Int,

    val difficulty: String,

    val notes: String,

    val isFavorite: Boolean,

    val updatedAt: Long
)
