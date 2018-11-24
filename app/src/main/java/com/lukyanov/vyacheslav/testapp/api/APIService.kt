package com.lukyanov.vyacheslav.testapp.api

import io.reactivex.Single
import retrofit2.http.GET

interface APIService {

    @GET("users")
    fun getUsers(): Single<List<UserResponse>>
}