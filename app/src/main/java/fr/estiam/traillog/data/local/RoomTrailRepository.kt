
package fr.estiam.traillog.data.local

import androidx.paging.PagingSource
import androidx.paging.map
import fr.estiam.traillog.data.TrailRepository
import fr.estiam.traillog.model.Trail
import fr.estiam.traillog.model.TrailSortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTrailRepository(
    private val dao: TrailDao
) : TrailRepository {

    // Observer les sentiers et convertir les Entity en Trail
    override fun observeTrails(): Flow<List<Trail>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Récupérer un sentier par son identifiant
    override suspend fun getTrail(id: String): Trail? {
        return dao.findById(id)?.toDomain()
    }

    // Ajouter ou modifier un sentier
    override suspend fun save(trail: Trail) {
        require(trail.isValid()) {
            "Les informations du sentier sont invalides"
        }

        dao.upsert(trail.toEntity())
    }

    // Supprimer un sentier
    override suspend fun delete(id: String) {
        dao.deleteById(id)
    }

    // Sélectionner la bonne source de pagination
    override fun pagingSource(
        sort: TrailSortOrder,
        favoritesOnly: Boolean
    ): PagingSource<Int, Trail> {

        val source = when {
            favoritesOnly && sort == TrailSortOrder.DISTANCE ->
                dao.favoritesDistance()

            favoritesOnly ->
                dao.favoritesName()

            sort == TrailSortOrder.DISTANCE ->
                dao.pageDistance()

            else ->
                dao.pageName()
        }

        // Convertir TrailEntity en Trail
        // tout en conservant l'invalidation de Room
        return source.map { entity ->
            entity.toDomain()
        }
    }

    // Le chargement initial sera réalisé dans le Lab 2
    override suspend fun seedIfNeeded() {
        // TODO M3-LAB2 : import du fichier JSON
    }
}
