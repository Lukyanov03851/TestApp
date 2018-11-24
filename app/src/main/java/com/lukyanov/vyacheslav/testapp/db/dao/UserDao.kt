package com.lukyanov.vyacheslav.testapp.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.lukyanov.vyacheslav.testapp.db.model.User
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * from user")
    fun getAll(): Single<List<User>>

    @Insert(onConflict = REPLACE)
    fun insert(user: User)

    @Insert(onConflict = REPLACE)
    fun insertAll(users: List<User>)

    @Query("DELETE from user")
    fun deleteAll()
}