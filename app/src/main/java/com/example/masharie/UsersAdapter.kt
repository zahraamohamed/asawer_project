package com.example.masharie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.masharie.Data.User
import com.example.masharie.Fragments.StudentHomeFragmentDirections

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private var usersList = emptyList<User>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectTitle = itemView.findViewById<TextView>(R.id.cardTitle)
        val projectCard = itemView.findViewById<CardView>(R.id.projectCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_row_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.projectTitle.text = usersList[position].fullName

        holder.projectCard.setOnClickListener {
            val action = StudentHomeFragmentDirections.actionStudentHomeFragmentToTeacherAccountFragment(usersList[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(user: List<User>) {
        this.usersList = user
        notifyDataSetChanged()
    }
}