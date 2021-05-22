package com.example.masharie.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masharie.Data.User
import com.example.masharie.UsersAdapter
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.Utils.loadingDialogStart
import com.example.masharie.databinding.FragmentStudentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class StudentHomeFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentStudentHomeBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور
    private val db = Firebase.firestore
    // انشيلايز للادبتر
    private val myAdapter = UsersAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // تعريف البايندنك
        _binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        //جلب معلومات اليوزر المسجل الدخول
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser

        // تشغيل دايلوك الانتظار
        loadingDialogStart(requireActivity())

        binding.apply {
            //جلب الاسم الكامل ووضعه في مكانه
            db.collection("User").whereEqualTo("email", currentUser?.email.toString()).get()
                .addOnSuccessListener { doc ->
                    for (d in doc) {
                        val name = d.data["fullName"].toString()
                        binding.studentHomeName.text = name
                    }
                }

            // عمل انستانس للرسايكل فيو
            val teachersRecycler = binding.studentRecycler

            // تفعيل الادبتر
            teachersRecycler.adapter = myAdapter
            teachersRecycler.layoutManager = LinearLayoutManager(requireContext())

            // جلب المشاريع الخاصة بالاستاذ فقط
            db.collection("User").whereEqualTo("student", false).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val myData = snapshot.toObjects(User::class.java)
                    myAdapter.setData(myData)
                    loadingDialogEnd()
                } else {
                    loadingDialogEnd()
                }
            }
        }

        return view
    }

}