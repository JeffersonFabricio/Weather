package fabricio.jefferson.npd_projeto.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fabricio.jefferson.npd_projeto.api.model.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class RoomManager : RoomDatabase() {

    abstract fun getCityDao(): CityDao

    companion object {
        var INSTANCE: RoomManager? = null

        fun getInstance(context: Context): RoomManager {
            synchronized(RoomManager::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RoomManager::class.java,
                        "Weather.db"
                    )//.allowMainThreadQueries()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }

}