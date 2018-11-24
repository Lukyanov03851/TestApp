package com.lukyanov.vyacheslav.testapp.util

import android.preference.PreferenceManager
import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.net.URL


class PrefUtil(context: Context) {

    private var context: Context = context

    fun saveAccessToken(token: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString("fb_access_token", token)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getToken(): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString("fb_access_token", null)
    }

    fun clearToken() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.clear()
        editor.apply() // This line is IMPORTANT !!!
    }

    fun saveFacebookUserInfo(first_name: String, last_name: String, email: String, profilePicURL: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString("fb_first_name", first_name)
        editor.putString("fb_last_name", last_name)
        editor.putString("fb_email", email)
        editor.putString("fb_profilePicURL", profilePicURL)
        editor.apply() // This line is IMPORTANT !!!
        Log.d("MyApp", "Shared Name : $first_name\nLast Name : $last_name\nEmail : $email\nProfile Pic : $profilePicURL")
    }

    fun getFacebookProfilePicture(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString("fb_profilePicURL", "")
    }

    fun getFacebookUserEmail():String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
       return prefs.getString("fb_email", "")
    }

    fun getFacebookUserName() :String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString("fb_first_name", "") +" "+ prefs.getString("fb_last_name", "")
    }

}