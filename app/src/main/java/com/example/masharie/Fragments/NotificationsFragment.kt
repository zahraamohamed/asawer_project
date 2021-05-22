package com.example.masharie.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masharie.Data.NotificationData
import com.example.masharie.Data.ProjectData
import com.example.masharie.NotificationsAdapter
import com.example.masharie.ProjectsAdapter
import com.example.masharie.R
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.databinding.FragmentHomeBinding
import com.example.masharie.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور
    private val db = Firebase.firestore
    // انشيلايز للادبتر
    private val myAdapter = NotificationsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // تعريف البايندنك
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val view = binding.root

        //جلب معلومات اليوزر المسجل الدخول
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser

        // عمل انستانس للرسايكل فيو
        val notiRecycler = binding.notificationsRecyclerView

        // تفعيل الادبتر
        notiRecycler.adapter = myAdapter
        notiRecycler.layoutManager = LinearLayoutManager(requireContext())

        db.collection("Notifications").whereEqualTo("notiTo" , currentUser?.uid.toString()).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val myData = snapshot.toObjects(NotificationData::class.java)
                myAdapter.setData(myData)
                if (myData.isEmpty()){
                    notiRecycler.visibility = View.INVISIBLE
                    binding.noNotifications.visibility = View.VISIBLE
                }
            } 
        }

        return view
    }
}