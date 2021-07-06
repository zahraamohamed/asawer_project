package com.example.masharie.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masharie.Data.ProjectData
import com.example.masharie.ProjectsAdapter
import com.example.masharie.TeacherProjectsAdapter
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.Utils.loadingDialogStart
import com.example.masharie.databinding.FragmentTeacherAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account.*


class TeacherAccountFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentTeacherAccountBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore
    // انشيلايز للادبتر
    private val myAdapter = TeacherProjectsAdapter()
    // تعريف الاركز
    private val args by navArgs<TeacherAccountFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // تعريف البايندنك
        _binding = FragmentTeacherAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        //جلب معلومات الاستاذ
        val currentTeacherId = args.currentTeacher.teacherId
        binding.teacherName.text = args.currentTeacher.fullName


        // عمل انستانس للرسايكل فيو
        val projectsRecycler = binding.projectRecyclerView

        // تفعيل الادبتر
        projectsRecycler.adapter = myAdapter
        projectsRecycler.layoutManager = LinearLayoutManager(requireContext())

        // تشغيل دايلوك الانتظار
        loadingDialogStart(requireActivity())

        // جلب المشاريع الخاصة بالاستاذ فقط
        db.collection("Projects").whereEqualTo("teacherId", currentTeacherId).addSnapshotListener { snapshot, e ->
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


        return view
    }

}