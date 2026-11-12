
package fr.estiam.traillog.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TrailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TrailDatabase : RoomDatabase() {

    abstract fun trailDao(): TrailDao
}
