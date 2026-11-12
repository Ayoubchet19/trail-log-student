package fr.estiam.traillog.data.local

import fr.estiam.traillog.model.Difficulty
import fr.estiam.traillog.model.Trail

fun Trail.toEntity() = TrailEntity(
    id = id,
    name = name,
    region = region,
    distanceKm = distanceKm,
    elevationGainM = elevationGainM,
    difficulty = difficulty.name,
    notes = notes,
    isFavorite = isFavorite,
    updatedAt = updatedAt
)

fun TrailEntity.toDomain() = Trail(
    id = id,
    name = name,
    region = region,
    distanceKm = distanceKm,
    elevationGainM = elevationGainM,
    difficulty = Difficulty.valueOf(difficulty),
    notes = notes,
    isFavorite = isFavorite,
    updatedAt = updatedAt
)