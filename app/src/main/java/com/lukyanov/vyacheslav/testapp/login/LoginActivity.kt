package com.lukyanov.vyacheslav.testapp.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.CallbackManager
import android.content.Intent
import com.lukyanov.vyacheslav.testapp.R
import com.lukyanov.vyacheslav.testapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

private const val EMAIL = "email"
private const val USER_PROFILE = "public_profile"

class LoginActivity : AppCompatActivity() {

    private var mCallbackManager: CallbackManager? = null

    private var mViewModel: LoginViewModel?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        mViewModel?.getGotoMainActivityEvent()?.observe(this, Observer<Void> {
            gotoMainActivity()
        })

        mCallbackManager = CallbackManager.Factory.create()

        // Set the initial permissions to request from the user while logging in
        loginButton.setReadPermissions(Arrays.asList(EMAIL, USER_PROFILE))

        // Register a callback to respond to the user
        loginButton.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                mViewModel?.onLoginSuccess(loginResult)
            }

            override fun onCancel() {
                finish()
            }

            override fun onError(e: FacebookException) {
                mViewModel?.onLoginError()
            }
        })
    }

    private fun gotoMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

}
