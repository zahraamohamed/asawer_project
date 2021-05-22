package com.example.masharie

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.masharie.Data.NotificationData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    // عمل انشيلايز للفايرستور
    private val db = Firebase.firestore.collection("Notifications")
    private var notiList = emptyList<NotificationData>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notiTitle = itemView.findViewById<TextView>(R.id.notiTitle)
        val acceptButton = itemView.findViewById<Button>(R.id.acceptBtn)
        val notAccceptButton = itemView.findViewById<TextView>(R.id.notAcceptBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notifications_row, parent, false))
    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notiTitle.text = " قام ${ notiList[position].studentName} بحجز مشروع ${notiList[position].projectTitle}"

        // قبول الحجز
        holder.acceptButton.setOnClickListener {
            db.whereEqualTo("notiFrom", notiList[position].notiFrom).whereEqualTo("notiTo" , notiList[position].notiTo).whereEqualTo("projectTitle", notiList[position].projectTitle).get().addOnSuccessListener { task ->
                for (n in task) {
                    val notiId = n.id
                    Log.d("haider", notiId)
                    val myData = NotificationData(notiList[position].notiTo, notiList[position].studentName, notiList[position].notiFrom, notiList[position].projectTitle, true)
                    db.document("$notiId").set(myData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // اضهار رسالة تم الارسالة واخفاء الازرار
                            Toast.makeText(holder.itemView.context, "تم ارسال قبولك", Toast.LENGTH_SHORT).show()
                        } else {
                            // أضهار رسالة في حالة حدوث خطأ
                            Toast.makeText(holder.itemView.context, " تعذر ارسال قبولك حاول مجدداً", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // رفض الحجز
        holder.notAccceptButton.setOnClickListener {
            db.whereEqualTo("notiFrom", notiList[position].notiFrom).whereEqualTo("notiTo" , notiList[position].notiTo).whereEqualTo("projectTitle", notiList[position].projectTitle).get().addOnSuccessListener { task ->
                for (n in task) {
                    val notiId = n.id
                    val myData = NotificationData(notiList[position].notiTo, notiList[position].studentName, notiList[position].notiFrom, notiList[position].projectTitle, false)
                    db.document("$notiId").set(myData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // اضهار رسالة تم الارسالة واخفاء الازرار
                            Toast.makeText(holder.itemView.context, "تم ارسال رفضك", Toast.LENGTH_SHORT).show()
                        } else {
                            // أضهار رسالة في حالة حدوث خطأ
                            Toast.makeText(holder.itemView.context, "تعذر ارسال رفضك حاول مجدداً", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


    }
    fun setData(notifiaction: List<NotificationData>){
        this.notiList = notifiaction
        notifyDataSetChanged()
    }

}