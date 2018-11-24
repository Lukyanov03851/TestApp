package com.lukyanov.vyacheslav.testapp.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lukyanov.vyacheslav.testapp.databinding.ItemUserListBinding
import com.lukyanov.vyacheslav.testapp.db.model.User

class UsersListAdapter(private val users : List<User>) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    private var onUserItemClickListener: OnUserItemClickListener? = null
    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return users.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = ItemUserListBinding.inflate(inflater)
        return ViewHolder(binding)
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(users[position])
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
        holder.binding.clRoot.setOnClickListener {
            onUserItemClickListener!!.onUserItemClick(users[position])
        }
}

    inner class ViewHolder(val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun setOnUserItemClickListener(listener: OnUserItemClickListener){
        onUserItemClickListener = listener
    }

    interface OnUserItemClickListener{
        fun onUserItemClick(user: User)
    }
}