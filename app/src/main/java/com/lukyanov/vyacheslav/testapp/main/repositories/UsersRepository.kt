package com.lukyanov.vyacheslav.testapp.main.repositories

import android.app.Application
import android.util.Log
import com.lukyanov.vyacheslav.testapp.api.APIClient
import com.lukyanov.vyacheslav.testapp.api.UserResponse
import com.lukyanov.vyacheslav.testapp.db.AppDatabase
import com.lukyanov.vyacheslav.testapp.db.dao.UserDao
import com.lukyanov.vyacheslav.testapp.db.model.Company
import com.lukyanov.vyacheslav.testapp.db.model.Geo
import com.lukyanov.vyacheslav.testapp.db.model.User
import com.lukyanov.vyacheslav.testapp.db.model.UserAddress
import com.lukyanov.vyacheslav.testapp.util.isConnectedToInternet
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

class UsersRepository(application: Application) {

    private val userDao: UserDao

    init {
        val roomDatabase = AppDatabase.getInstance(application)
        userDao = roomDatabase?.userDao()!!
    }

    fun getUsers(): Observable<List<User>> {
        return if (isConnectedToInternet()){
            getUsersFromApi()
        }else{
            getUsersFromDb()
        }
    }

    private fun getUsersFromDb(): Observable<List<User>> {
        return userDao.getAll()
                .toObservable()
                .doOnNext {
                    Log.d("UsersRepository", "Dispatching ${it.size} users from DB...")
                }
    }

    private fun getUsersFromApi(): Observable<List<User>> {
        return APIClient()
                .getAPIService()
                .getUsers()
                .flattenAsObservable { list -> list}
                .flatMap{user -> processUser(user)}
                .toList()
                .toObservable()
                .doOnNext {
                    updateUsers(it)
                }
    }

    private fun storeUsersInDb(users: List<User>) {
        Observable.fromCallable { userDao.insertAll(users) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d("UsersRepository","Inserted ${users.size} users from API in DB...")
                }
    }

    private fun updateUsers(users: List<User>) {
        Observable.fromCallable { userDao.deleteAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnComplete{
                    storeUsersInDb(users)
                }.subscribe {
                    Log.d("UsersRepository","Deleted all users from DB...")
                }

    }

    private fun processUser(userResponse: UserResponse): ObservableSource<User> {
        val address = UserAddress(userResponse.address.street,
                userResponse.address.suite,
                userResponse.address.city,
                userResponse.address.zipCode,
                Geo(userResponse.address.geo.lng, userResponse.address.geo.lng))

        val user = User(userResponse.id,
                userResponse.name,
                userResponse.username,
                userResponse.email,
                address,
                userResponse.phone,
                userResponse.website,
                Company(userResponse.company.name,
                        userResponse.company.catchPhrase,
                        userResponse.company.bs)
        )
        return Observable.just(user)
    }

}