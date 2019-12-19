package fabricio.jefferson.npd_projeto.ui.list

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fabricio.jefferson.npd_projeto.R
import fabricio.jefferson.npd_projeto.api.model.Favorite
import fabricio.jefferson.npd_projeto.api.model.FindResult
import fabricio.jefferson.npd_projeto.api.model.RetrofitManager
import fabricio.jefferson.npd_projeto.common.Constrants
import fabricio.jefferson.npd_projeto.data.RoomManager
import fabricio.jefferson.npd_projeto.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListActivity : AppCompatActivity(), Callback<FindResult> {

    private val db : RoomManager? by lazy {
        RoomManager.getInstance(this)
    }

    private val adapter : ListAdapter by lazy {
        ListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        btnSearch.setOnClickListener {
            if (isDeviceConnected())
                getCities()
            else
                Toast.makeText(this, R.string.msgNetworkOff, Toast.LENGTH_LONG).show()
        }
    }

    private fun initRecyclerView(){
        rvWeather.adapter = adapter
        rvWeather.layoutManager = LinearLayoutManager(this)
    }

    private fun getCities() {
        progressBar.visibility = View.VISIBLE
        val call = RetrofitManager.getWeatherService()
            .find(edtTxtCity.text.toString(), Constrants.API_KEY)
        call.enqueue(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuId = R.id.setting_item
        if (menuId == R.id.setting_item){
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
        Log.e("Jeff","Error", t)
        progressBar.visibility = View.GONE
    }

    override fun onResponse(call: Call<FindResult>, response: Response<FindResult>) {
        if (response.isSuccessful){
            adapter.updateData(response.body()?.list)
        }
        progressBar.visibility = View.GONE
    }

}
