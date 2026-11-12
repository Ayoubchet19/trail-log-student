
package fr.estiam.traillog

import android.app.Application
import androidx.room.Room
import fr.estiam.traillog.data.TrailRepository
import fr.estiam.traillog.data.PreferencesRepository
import fr.estiam.traillog.data.DataStorePreferencesRepository
import fr.estiam.traillog.data.local.TrailDatabase
import fr.estiam.traillog.data.local.RoomTrailRepository
import fr.estiam.traillog.worker.TrailReminderWorker

/**
 * Application principale TrailLog.
 * Room est utilisé pour conserver les sentiers.
 */
class TrailLogApplication : Application() {

    // Création de la base de données Room
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            TrailDatabase::class.java,
            "traillog.db"
        ).build()
    }

    // Repository Room : remplace MemoryTrailRepository
    val trails: TrailRepository by lazy {
        RoomTrailRepository(database.trailDao())
    }

    val settings: PreferencesRepository by lazy {
        DataStorePreferencesRepository(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        TrailReminderWorker.ensureChannel(this)
    }
}
