package com.lukyanov.vyacheslav.testapp.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lukyanov.vyacheslav.testapp.R
import com.lukyanov.vyacheslav.testapp.db.model.User
import kotlinx.android.synthetic.main.fragment_users_list.*

/**
 * A simple [Fragment] subclass.
 * Use the [UsersListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class UsersListFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = UsersListFragment().apply {}
    }

    private var mViewModel: MainViewModel?= null

    private var users: ArrayList<User> = arrayListOf()

//    internal lateinit var rvUsers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        mViewModel?.userItems?.observe(this, Observer<ArrayList<User>> { list ->
                Log.e("TAG", "Add part to List = "+list?.size)
                users.clear()
                users.addAll(list!!)
                if (users.isEmpty()) showToast(getString(R.string.empty_list))
                rvUsers.adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvUsers.setHasFixedSize(true)
        rvUsers.layoutManager = LinearLayoutManager(context)

        val adapter = UsersListAdapter(users)
        adapter.setOnUserItemClickListener(object : UsersListAdapter.OnUserItemClicListener {
            override fun onUserItemClick(user: User) {
                mViewModel?.onUserClick(user)
            }
        })
        rvUsers.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)
                .show()
    }
}
