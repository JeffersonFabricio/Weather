package fabricio.jefferson.npd_projeto.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fabricio.jefferson.npd_projeto.R
import fabricio.jefferson.npd_projeto.api.model.City
import kotlinx.android.synthetic.main.row_weather_layout.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var list: List<City>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_weather_layout,
            parent,
            false)

        return ViewHolder(view)
    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.let{
            holder.bind(it[position])
        }
    }

    fun updateData(list: List<City>?){
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(city : City) {
            itemView.txtViewNameCity.text = "${city.name}, ${city.sys.country}"
            itemView.txtViewTemp.text = city.main.temp.toInt().toString()
            itemView.txtViewWind.text = "Wind ${city.wind.speed} m/s | clouds ${city.clouds.all} | " +
                    "${city.main.pressure} hpa"
            if (city.weather.isNotEmpty()){
                Glide.with(itemView.context)
                    .load("http://openweathermap.org/img/w/${city.weather[0].icon}.png")
                    //.placeholder(R.drawable.ic_launcher_background)
                    .into(itemView.imgViewWeatherIcon)
                itemView.txtViewDescription.text = city.weather[0].description
            }
        }
    }

}