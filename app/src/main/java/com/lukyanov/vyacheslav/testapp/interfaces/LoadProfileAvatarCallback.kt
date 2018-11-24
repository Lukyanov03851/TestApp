package com.lukyanov.vyacheslav.testapp.interfaces

import android.graphics.drawable.Drawable

interface LoadProfileAvatarCallback {
    fun onAvatarLoaded(avatar: Drawable?)
}