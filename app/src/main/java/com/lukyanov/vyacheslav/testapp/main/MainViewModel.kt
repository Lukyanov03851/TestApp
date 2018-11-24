package com.lukyanov.vyacheslav.testapp.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.lukyanov.vyacheslav.testapp.db.AppDatabase
import com.lukyanov.vyacheslav.testapp.db.model.User
import com.lukyanov.vyacheslav.testapp.interfaces.LoadProfileAvatarCallback
import com.lukyanov.vyacheslav.testapp.main.repositories.ProfileAvatarRepository
import com.lukyanov.vyacheslav.testapp.main.repositories.UsersRepository
import com.lukyanov.vyacheslav.testapp.util.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var prefUtil: PrefUtil = PrefUtil(application)

    private val usersRepository: UsersRepository = UsersRepository(application)
    private val profileAvatarRepository: ProfileAvatarRepository = ProfileAvatarRepository()

    var userItems: MutableLiveData<ArrayList<User>> = MutableLiveData()
    private var onUserClickEvent: MutableLiveData<User> = MutableLiveData()
    val dataLoading = ObservableBoolean(false)
    val profileName = ObservableField<String>()
    val profileEmail = ObservableField<String>()
    val profileAvatar = ObservableField<Drawable>()

    init {
        getUsers()
        receiveProfileData()
    }

    fun getOnUserClickEvent(): MutableLiveData<User> {
        return onUserClickEvent
    }

    fun onUserClick(user: User){
        onUserClickEvent.value = user
    }

    private fun receiveProfileData(){
        profileEmail.set(prefUtil.getFacebookUserEmail())
        profileName.set(prefUtil.getFacebookUserName())
        profileAvatarRepository.getAvatar(object : LoadProfileAvatarCallback {
            override fun onAvatarLoaded(avatar: Drawable?) {
                profileAvatar.set(avatar)
            }
        })
    }

    override fun onCleared() {
        AppDatabase.destroyInstance()
    }

    private fun getUsers() {
        dataLoading.set(true)

        usersRepository
                .getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = {
                            dataLoading.set(false)
                        },
                        onNext = {
                            users ->
                                dataLoading.set(false)
                                userItems.value = ArrayList(users)
                        }
                )
    }

}