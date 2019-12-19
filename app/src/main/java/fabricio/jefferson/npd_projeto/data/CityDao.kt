package fabricio.jefferson.npd_projeto.data

import androidx.room.*
import fabricio.jefferson.npd_projeto.api.model.Favorite

@Dao
interface CityDao {

    @Insert
    fun insertFavorite(favorite: Favorite)

    @Delete
    fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM TB_FAVORITE WHERE id = :id")
    fun getFavoriteById(id : Int) : Favorite

    @Query("SELECT * FROM TB_FAVORITE")
    fun allFavorites() : List<Favorite>

}