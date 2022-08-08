package com.example.submission3.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserFavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userfav: UserFav)

    @Query("DELETE FROM user_fav WHERE id = :id")
    fun delete(id: Int):Int

    @Query("SELECT count(*) FROM user_fav WHERE id =:id")
    fun getTotal(id: Int):Int

    @Query("SELECT * from user_fav ORDER BY login ASC")
    fun getUserFav(): LiveData<List<UserFav>>

    @Update
    fun update(userfav: UserFav)
}