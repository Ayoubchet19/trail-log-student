
package fr.estiam.traillog.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TrailDao {

    // Observer tous les sentiers
    @Query("""
        SELECT * FROM trails
        ORDER BY name COLLATE NOCASE, id
    """)
    fun observeAll(): Flow<List<TrailEntity>>

    // Rechercher un sentier par son identifiant
    @Query("""
        SELECT * FROM trails
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun findById(id: String): TrailEntity?

    // Ajouter ou modifier un sentier
    @Upsert
    suspend fun upsert(item: TrailEntity)

    // Supprimer un sentier
    @Query("DELETE FROM trails WHERE id = :id")
    suspend fun deleteById(id: String)

    // Pagination par nom
    @Query("""
        SELECT * FROM trails
        ORDER BY name COLLATE NOCASE, id
    """)
    fun pageName(): PagingSource<Int, TrailEntity>

    // Pagination par distance
    @Query("""
        SELECT * FROM trails
        ORDER BY distanceKm DESC, name COLLATE NOCASE, id
    """)
    fun pageDistance(): PagingSource<Int, TrailEntity>

    // Favoris triés par nom
    @Query("""
        SELECT * FROM trails
        WHERE isFavorite = 1
        ORDER BY name COLLATE NOCASE, id
    """)
    fun favoritesName(): PagingSource<Int, TrailEntity>

    // Favoris triés par distance
    @Query("""
        SELECT * FROM trails
        WHERE isFavorite = 1
        ORDER BY distanceKm DESC, name COLLATE NOCASE, id
    """)
    fun favoritesDistance(): PagingSource<Int, TrailEntity>
}
