package com.lukyanov.vyacheslav.testapp.util

import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import com.lukyanov.vyacheslav.testapp.App

fun isConnectedToInternet(): Boolean {
    val cm = App.applicationContext().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo

    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}