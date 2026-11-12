
package fr.estiam.traillog.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
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

        return TrailPagingSource(source)
    }

    // Le chargement initial sera réalisé dans le Lab 2
    override suspend fun seedIfNeeded() {
        // TODO M3-LAB2 : import du fichier JSON
    }
}

private class TrailPagingSource(
    private val source: PagingSource<Int, TrailEntity>
) : PagingSource<Int, Trail>() {

    init {
        source.registerInvalidatedCallback { invalidate() }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Trail> =
        when (val result = source.load(params)) {
            is LoadResult.Page -> LoadResult.Page(
                data = result.data.map { it.toDomain() },
                prevKey = result.prevKey,
                nextKey = result.nextKey,
                itemsBefore = result.itemsBefore,
                itemsAfter = result.itemsAfter
            )
            is LoadResult.Error -> LoadResult.Error(result.throwable)
            is LoadResult.Invalid -> LoadResult.Invalid()
        }

    override fun getRefreshKey(state: PagingState<Int, Trail>): Int? =
        state.anchorPosition
}
