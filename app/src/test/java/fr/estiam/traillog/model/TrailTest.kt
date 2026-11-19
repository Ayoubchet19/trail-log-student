package fr.estiam.traillog.model

import org.junit.Assert.*
import org.junit.Test
import fr.estiam.traillog.data.local.TrailEntity
import fr.estiam.traillog.data.local.toEntity

class TrailTest {
    @Test fun acceptsValidTrail() {
        assertTrue(Trail("t1", "A", "Alpes", 3.5, 120).isValid())
    }
    @Test fun rejectsInvalidTrail() {
        assertFalse(Trail("t1", "  ", "Alpes", 3.5, 120).isValid())
        assertFalse(Trail("t1", "A", "Alpes", -1.0, 120).isValid())
    }
    @Test fun milesConversion() {
        assertTrue(TrailFormatter.distance(10.0, DistanceUnit.MI).replace(',', '.').contains("6.2"))
    }

    @Test fun mapsEntityFieldsToDomainTrail() {
        val entity = TrailEntity(
            id = "trail-1",
            name = "Pine Loop",
            region = "Alpes",
            distanceKm = 3.2,
            elevationGainM = 100,
            difficulty = "MODERATE",
            notes = "Test trail",
            isFavorite = true,
            updatedAt = 1234L
        )

        val trail = entity.toDomain()

        assertEquals("trail-1", trail.id)
        assertEquals("Pine Loop", trail.name)
        assertEquals(3.2, trail.distanceKm, 0.001)
        assertEquals(Difficulty.MODERATE, trail.difficulty)
    }

    @Test fun mapsDomainTrailToEntity() {
        val trail = Trail("trail-2", "Creekside", "Jura", 1.0, 50)

        val entity = trail.toEntity()

        assertEquals("trail-2", entity.id)
        assertEquals("Creekside", entity.name)
        assertEquals(1.0, entity.distanceKm, 0.001)
    }
}
