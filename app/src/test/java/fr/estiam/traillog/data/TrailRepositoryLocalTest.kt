package fr.estiam.traillog.data

import androidx.paging.PagingSource
import fr.estiam.traillog.model.Trail
import fr.estiam.traillog.model.TrailSortOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/** M5 Lab 1: exercise the real MemoryTrailRepository without an Android device.
 * The repository already implements the TrailRepository contract. */
class TrailRepositoryLocalTest {
    private fun fixture(
        id: String,
        name: String,
        distance: Double
    ) = Trail(id, name, "Alpes", distance, 100)

    private suspend fun MemoryTrailRepository.clear() {
        observeTrails().first().forEach { delete(it.id) }
    }

    private suspend fun load(
        repository: MemoryTrailRepository,
        sort: TrailSortOrder
    ): List<Trail> {
        val source = repository.pagingSource(sort, favoritesOnly = false)
        val params = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 100,
            placeholdersEnabled = false
        )
        return (source.load(params) as PagingSource.LoadResult.Page).data
    }

    @Test fun saveGetAndUpdateByStableId() = runBlocking {
        val repository = MemoryTrailRepository()
        repository.clear()

        repository.save(fixture("trail-1", "Pine Loop", 3.2))
        assertEquals("Pine Loop", repository.getTrail("trail-1")?.name)

        repository.save(fixture("trail-1", "Pine Loop Updated", 4.5))
        assertEquals("Pine Loop Updated", repository.getTrail("trail-1")?.name)
        assertEquals(4.5, repository.getTrail("trail-1")?.distanceKm ?: 0.0, 0.001)
    }

    @Test fun deleteAndObserveTrails() = runBlocking {
        val repository = MemoryTrailRepository()
        repository.clear()
        repository.save(fixture("trail-1", "Pine Loop", 3.2))

        repository.delete("trail-1")

        assertTrue(repository.observeTrails().first().isEmpty())
        assertNull(repository.getTrail("trail-1"))
    }

    @Test fun sortsNamesAlphabetically() = runBlocking {
        val repository = MemoryTrailRepository()
        repository.clear()
        repository.save(fixture("trail-1", "Ridge Run", 7.5))
        repository.save(fixture("trail-2", "Creekside", 1.0))
        repository.save(fixture("trail-3", "Pine Loop", 3.2))

        val names = load(repository, TrailSortOrder.NAME).map { it.name }

        assertEquals(listOf("Creekside", "Pine Loop", "Ridge Run"), names)
    }

    @Test fun sortsDistancesDescending() = runBlocking {
        val repository = MemoryTrailRepository()
        repository.clear()
        repository.save(fixture("trail-1", "Pine Loop", 3.2))
        repository.save(fixture("trail-2", "Ridge Run", 7.5))
        repository.save(fixture("trail-3", "Creekside", 1.0))

        val result = load(repository, TrailSortOrder.DISTANCE)

        assertEquals(listOf("Ridge Run", "Pine Loop", "Creekside"), result.map { it.name })
    }

    @Test fun emptyRepositoryReturnsNoSortedResults() = runBlocking {
        val repository = MemoryTrailRepository()
        repository.clear()

        assertTrue(load(repository, TrailSortOrder.NAME).isEmpty())
        assertTrue(load(repository, TrailSortOrder.DISTANCE).isEmpty())
    }
}
