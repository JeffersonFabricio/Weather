package fabricio.jefferson.npd_projeto.ui.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fabricio.jefferson.npd_projeto.R
import fabricio.jefferson.npd_projeto.api.model.City
import fabricio.jefferson.npd_projeto.api.model.Favorite
import fabricio.jefferson.npd_projeto.api.model.FindResult
import fabricio.jefferson.npd_projeto.api.model.RetrofitManager
import fabricio.jefferson.npd_projeto.async.FavoritesAsyncTask
import fabricio.jefferson.npd_projeto.async.TaskListener
import fabricio.jefferson.npd_projeto.common.Constants
import fabricio.jefferson.npd_projeto.data.RoomManager
import fabricio.jefferson.npd_projeto.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListActivity : AppCompatActivity(), Callback<FindResult> {

    private lateinit var asyncTask: FavoritesAsyncTask

    private val sp: SharedPreferences by lazy {
        getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
    }

    private val db: RoomManager? by lazy {
        RoomManager.getInstance(this)
    }

    private val adapter: ListAdapter by lazy {
        ListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        btnSearch()
        getFavoritesCities()
    }

    private fun btnSearch() {
        btnSearch.setOnClickListener {
            if (isDeviceConnected())
                getCities()
            else
                Toast.makeText(this, R.string.msgNetworkOff, Toast.LENGTH_LONG).show()
        }
    }

    private fun initRecyclerView() {
        rvWeather.adapter = adapter
        rvWeather.layoutManager = LinearLayoutManager(this)
    }

    private fun getCities() {
        progressBar.visibility = View.VISIBLE
        val isCelsius = sp.getBoolean(Constants.PREFS_TEMP, true)
        val unit = if (isCelsius) Constants.PREFS_CELSIUS else Constants.PREFS_FAHRENHEIT
        val isEnglish = sp.getBoolean(Constants.PREFS_LANG, true)
        val lang = if (isEnglish) Constants.PREFS_ENGLISH else Constants.PREFS_PORTUGUESE

        val call = RetrofitManager.getWeatherService()
            .find(edtTxtCity.text.toString(), lang, unit, Constants.API_KEY)
        call.enqueue(this)
    }

    private fun getCitiesFavorites(favorites: List<Int>?) {
        if (favorites != null && favorites.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            val isCelsius = sp.getBoolean(Constants.PREFS_TEMP, true)
            val unit = if (isCelsius) Constants.PREFS_CELSIUS else Constants.PREFS_FAHRENHEIT
            val isEnglish = sp.getBoolean(Constants.PREFS_LANG, true)
            val lang = if (isEnglish) Constants.PREFS_ENGLISH else Constants.PREFS_PORTUGUESE
            val ids = TextUtils.join(",", favorites)
            val call = RetrofitManager.getWeatherService()
                .findFavorites(ids, lang, unit, Constants.API_KEY)
            call.enqueue(this)
        } else {
            adapter.updateData(null)
        }
    }

    private fun getFavoritesCities() {
        asyncTask = FavoritesAsyncTask(this, object : TaskListener {
            override fun onTaskComplete(favorites: List<Int>?) {
                getCitiesFavorites(favorites)
            }
        })
        asyncTask.execute()
    }

    fun favorite(city: City) {
        FavoriteAsync(this, city).execute()
    }

    private fun getFavorites() {
        asyncTask = FavoritesAsyncTask(this, object : TaskListener {
            override fun onTaskComplete(favorites: List<Int>?) {
                favorites?.let { adapter.updateFavorites(it) }
            }
        })
        asyncTask.execute()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuId = R.id.setting_item
        if (menuId == R.id.setting_item) {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun isDeviceConnected(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    override fun onFailure(call: Call<FindResult>, t: Throwable) {
        progressBar.visibility = View.GONE
    }

    override fun onResponse(call: Call<FindResult>, response: Response<FindResult>) {
        if (response.isSuccessful) {
            adapter.updateData(response.body()?.list)
        }
        progressBar.visibility = View.GONE
    }

    class FavoriteAsync(val context: Context, val city: City) : AsyncTask<Void, Void, Boolean>() {

        val db = RoomManager.getInstance(context)

        override fun doInBackground(vararg p0: Void?): Boolean {
            var favorite: Favorite? = db?.getCityDao()?.favoriteById(city.id)
            if (favorite == null) {
                favorite = Favorite(city.id, city.name)
                db?.getCityDao()?.insertFavorite(favorite)
                return true
            } else {
                db?.getCityDao()?.deleteFavorite(favorite)
                return false
            }
        }

        override fun onPostExecute(isSave: Boolean) {
            super.onPostExecute(isSave)
            val activity = context as ListActivity
            activity.getFavorites()
            if (activity.edtTxtCity.text.trim().isEmpty())
                activity.getFavoritesCities()
        }
    }

}
