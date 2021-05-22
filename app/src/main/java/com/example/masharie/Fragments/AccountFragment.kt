package com.example.masharie.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masharie.Data.ProjectData
import com.example.masharie.ProjectsAdapter
import com.example.masharie.R
import com.example.masharie.SignInActivity
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.Utils.loadingDialogStart
import com.example.masharie.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore
    // انشيلايز للادبتر
    private val myAdapter = ProjectsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // تعريف البايندنك
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        //جلب معلومات اليوزر المسجل الدخول
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser

        //جلب الاسم الكامل ووضعه في مكانه
        db.collection("User").whereEqualTo("email", currentUser?.email.toString()).get()
                .addOnSuccessListener { doc ->
                    for (d in doc){
                        val name = d.data["fullName"].toString()
                        binding.teacherName.text = name
                    }
                }

        // عمل انستانس للرسايكل فيو
        val projectsRecycler = binding.projectRecyclerView

        // تفعيل الادبتر
        projectsRecycler.adapter = myAdapter
        projectsRecycler.layoutManager = LinearLayoutManager(requireContext())

        // تشغيل دايلوك الانتظار
        loadingDialogStart(requireActivity())

        // جلب المشاريع الخاصة بالاستاذ فقط
        db.collection("Projects").whereEqualTo("teacherId", currentUser?.uid.toString()).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val myData = snapshot.toObjects(ProjectData::class.java)
                myAdapter.setData(myData)
                if (myData.isEmpty()){
                    projectsRecycler.visibility = View.INVISIBLE
                    noResult.visibility = View.VISIBLE
                }
                loadingDialogEnd()
            } else {
                loadingDialogEnd()
            }
        }

        // تسجيل الخروج
        binding.signOutBtn.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(requireContext(), "تم تسجيل الخروج", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }


        return view
    }
}