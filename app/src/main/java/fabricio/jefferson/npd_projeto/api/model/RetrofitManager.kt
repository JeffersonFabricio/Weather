package fabricio.jefferson.npd_projeto.api.model

import fabricio.jefferson.npd_projeto.api.WeatherService
import fabricio.jefferson.npd_projeto.common.Constrants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    private val retrofitInstance = Retrofit.Builder()
        .baseUrl(Constrants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getWeatherService() = retrofitInstance.create(WeatherService::class.java)
}