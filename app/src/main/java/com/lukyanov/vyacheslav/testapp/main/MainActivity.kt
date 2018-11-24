package com.lukyanov.vyacheslav.testapp.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.facebook.login.LoginManager
import com.lukyanov.vyacheslav.testapp.*
import com.lukyanov.vyacheslav.testapp.databinding.ActivityMainBinding
import com.lukyanov.vyacheslav.testapp.databinding.HeaderDriverBinding
import com.lukyanov.vyacheslav.testapp.db.model.User
import com.lukyanov.vyacheslav.testapp.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mViewModel: MainViewModel?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mViewModel?.getOnUserClickEvent()?.observe(this, Observer<User> { user ->
            gotoUserDetailsFragment(user!!)
        })

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewmodel = mViewModel

        val headerView: View = binding.mainNavView.getHeaderView(0)

        val headerBinding = HeaderDriverBinding.bind(headerView)
        headerBinding.viewmodel = mViewModel
        setSupportActionBar(toolbar)

        setupViewFragment()

        // Set navigation view navigation item selected listener
        setupNavigationDrawer()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupViewFragment() {
        val usersListFragment: UsersListFragment = UsersListFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, usersListFragment)
                .commit()
    }

    private fun gotoUserDetailsFragment(user: User) {
        val usersDetailsFragment: UserDetailsFragment = UserDetailsFragment.newInstance(user)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, usersDetailsFragment)
                .addToBackStack(null)
                .commit()
    }

    private fun setupNavigationDrawer(){
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close){}

        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        setupDrawerContent()
    }

    private fun setupDrawerContent() {
        mainNavView.setNavigationItemSelectedListener{
            when (it.itemId){R.id.item_logout -> logout()}
            // Close the drawer
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }


    private fun logout(){
        mViewModel?.prefUtil?.clearToken()
        LoginManager.getInstance().logOut()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
