package fabricio.jefferson.npd_projeto.api

import fabricio.jefferson.npd_projeto.api.model.FindResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("find")
    fun find(
        @Query("q")
        cityName: String,
        @Query("lang")
        lang: String,
        @Query("units")
        unit: String,
        @Query("appId")
        appId: String
    ): Call<FindResult>

    @GET("group")
    fun findFavorites(
        @Query("id")
        ids: String,
        @Query("lang")
        lang: String,
        @Query("units")
        unit: String,
        @Query("appId")
        appId: String
    ): Call<FindResult>

}