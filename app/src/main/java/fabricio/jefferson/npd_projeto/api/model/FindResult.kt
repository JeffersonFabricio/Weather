package fabricio.jefferson.npd_projeto.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class FindResult(
    val list: List<City>
)

data class City(
    val id: Int,
    val name: String,
    val main: Main,
    val weather : List<Weather>
)

data class Main(
    val temp: Float
)

data class Weather(
    val icon : String
)

@Entity(tableName = "TB_FAVORITE")
data class Favorite (
    @PrimaryKey
    val id : Int,
    val name: String
)