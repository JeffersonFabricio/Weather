package fabricio.jefferson.npd_projeto.async

import android.content.Context
import android.os.AsyncTask
import fabricio.jefferson.npd_projeto.data.RoomManager

class FavoritesAsyncTask (context: Context, private val listener: TaskListener): AsyncTask<Void, Void, List<Int>?>(){

    val db = RoomManager.getInstance(context)

    override fun doInBackground(vararg p0: Void?): List<Int>? {
        return db?.getCityDao()?.allFavoritesByIds()
    }

    override fun onPostExecute(favorites: List<Int>?) {
        super.onPostExecute(favorites)
        listener.onTaskComplete(favorites)
    }
}

interface TaskListener{
    fun onTaskComplete(favorites: List<Int>?)
}