package com.example.masharie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.masharie.Data.ProjectData
import com.example.masharie.Fragments.AccountFragment
import com.example.masharie.Fragments.AccountFragmentDirections

class ProjectsAdapter : RecyclerView.Adapter<ProjectsAdapter.ViewHolder>() {
    private var projectsList = emptyList<ProjectData>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectTitle = itemView.findViewById<TextView>(R.id.cardTitle)
        val projectCard = itemView.findViewById<CardView>(R.id.projectCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return projectsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.projectTitle.text = projectsList[position].projectTitle

        holder.projectCard.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToSingleProjectFragment(projectsList[position])
            holder.itemView.findNavController().navigate(action)
        }
    }
    fun setData(project: List<ProjectData>){
        this.projectsList = project
        notifyDataSetChanged()
    }

}