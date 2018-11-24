package com.lukyanov.vyacheslav.testapp.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.lukyanov.vyacheslav.testapp.util.PrefUtil
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var prefUtil: PrefUtil = PrefUtil(application)

    private var gotoMainActivityEvent: MutableLiveData<Void> = MutableLiveData()

    init {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn){
            gotoMainActivityEvent.value = null
        }
    }

    fun getGotoMainActivityEvent(): MutableLiveData<Void> {
        return gotoMainActivityEvent
    }

    private fun gotoMainActivity(){
        gotoMainActivityEvent.value = null
    }

    private fun parseFacebookData(profileObject: JSONObject) {
        val id = profileObject.getString("id")
        var profilePic: URL? = null
        try {
            profilePic = URL("https://graph.facebook.com/$id/picture?type=large")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        prefUtil.saveFacebookUserInfo(profileObject.getString("first_name"),
                profileObject.getString("last_name"), profileObject.getString("email"), profilePic?.toString() ?: "")
    }

    private fun deleteAccessToken() {
        object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken, currentAccessToken: AccessToken?) {
                if (currentAccessToken == null) {
                    //User logged out
                    prefUtil.clearToken()
                    LoginManager.getInstance().logOut()
                }
            }
        }
    }

    fun onLoginSuccess(loginResult: LoginResult) {
        val accessToken = loginResult.accessToken.token

        // save accessToken to SharedPreference
        prefUtil.saveAccessToken(accessToken)

        val request = GraphRequest.newMeRequest(loginResult.accessToken) { jsonObject, _ ->
            // Getting FB User Data
            parseFacebookData(jsonObject)
            gotoMainActivity()
        }

        val parameters = Bundle()
        parameters.putString("fields", "id, first_name, last_name, email")
        request.parameters = parameters
        request.executeAsync()
    }

    fun onLoginError() {
        deleteAccessToken()
    }
}