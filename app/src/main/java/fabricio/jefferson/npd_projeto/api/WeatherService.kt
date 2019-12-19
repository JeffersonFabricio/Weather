package fabricio.jefferson.npd_projeto.api

import fabricio.jefferson.npd_projeto.api.model.FindResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("find?units=metric")
    fun find(
        @Query("q")
        cityName: String,
        @Query("appId")
        appId: String
    ): Call<FindResult>

}