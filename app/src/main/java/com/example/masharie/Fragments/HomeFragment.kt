package com.example.masharie.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.Utils.loadingDialogStart
import com.example.masharie.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // تعريف البايندنك
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        //جلب معلومات اليوزر المسجل الدخول
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser

        loadingDialogStart(requireActivity())

        binding.apply {
            //جلب الاسم الكامل ووضعه في مكانه
            db.collection("User").whereEqualTo("email", currentUser?.email.toString()).get()
                .addOnSuccessListener { doc ->
                    for (d in doc){
                        val name = d.data["fullName"].toString()
                        binding.teacherNameHome.text = name
                    }
                }

            db.collection("Projects").whereEqualTo("teacherId", currentUser?.uid.toString()).get().addOnSuccessListener { result ->
                projectCount.text = result.count().toString()
                loadingDialogEnd()
            }
        }

        return view
    }


}