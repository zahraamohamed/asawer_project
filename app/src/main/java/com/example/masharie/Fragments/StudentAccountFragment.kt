package com.example.masharie.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.masharie.R
import com.example.masharie.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class StudentAccountFragment : Fragment() {
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_account, container, false)

        //جلب معلومات اليوزر المسجل الدخول
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser

        //جلب الاسم الكامل ووضعه في مكانه
        db.collection("User").whereEqualTo("email", currentUser?.email.toString()).get()
            .addOnSuccessListener { doc ->
                for (d in doc){
                    val name = d.data["fullName"].toString()
                    view.findViewById<TextView>(R.id.studentName).text = name
                }
            }

        // تسجيل الخروج
        view.findViewById<Button>(R.id.signOutBtn).setOnClickListener {
            mAuth.signOut()
            Toast.makeText(requireContext(), "تم تسجيل الخروج", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

}