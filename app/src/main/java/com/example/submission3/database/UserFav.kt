package com.example.submission3.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_fav")
data class UserFav(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "login")
    var login: String?,

    @ColumnInfo(name = "avatar")
    var avatar_url : String?,

    @ColumnInfo(name = "url")
    var url : String?


) : Parcelable