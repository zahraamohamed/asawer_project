package com.example.masharie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masharie.Data.NotificationData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentNotificationsAdapter : RecyclerView.Adapter<StudentNotificationsAdapter.ViewHolder>() {
    // عمل انشيلايز للفايرستور
    private val db = Firebase.firestore.collection("Notifications")
    private var notiList = emptyList<NotificationData>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notiTitle = itemView.findViewById<TextView>(R.id.notiTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notifications_row2, parent, false))
    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isAcceptedText = if (notiList[position].accepted == true) " تم قبول حجزك لمشروع "  else " تم رفض حجزك لمشروع "
        holder.notiTitle.text = isAcceptedText + notiList[position].projectTitle

    }
    fun setData(notifiaction: List<NotificationData>){
        this.notiList = notifiaction
        notifyDataSetChanged()
    }

}