package com.example.masharie.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.masharie.MainActivity
import com.example.masharie.R
import com.example.masharie.databinding.FragmentSingleProjectBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SingleProjectFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentSingleProjectBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore.collection("Projects")
    // تعريف الاركز
    private val args by navArgs<SingleProjectFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // تعريف البايندنك
        _binding = FragmentSingleProjectBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.apply {
            TextViewProjectTitle.text = args.currentProject.projectTitle
            TextViewProjectDetails.text = args.currentProject.projectDetails
            TextViewProjectNotes.text = args.currentProject.projectNotes
        }

        // جلب ايدي المشروع الخاص بقاعدة البيانات نفسها
        var projectDbId = ""
        db.whereEqualTo("projectId", args.currentProject.projectId).get().addOnSuccessListener { doc ->
            for (d in doc) {
                projectDbId = d.id
            }
        }
        // عند الضغط على زر تعديل يقوم بنقل المعلومات والانتقال الى صفحة التعديل
        binding.editProjectBtn.setOnClickListener {
            val action = SingleProjectFragmentDirections.actionSingleProjectFragmentToEditProjectFragment(args.currentProject)
            findNavController().navigate(action)
        }

        binding.deleteProjectBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("أحذف"){ _, _ ->
                db.document(projectDbId).delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "تم الحذف بنجاح", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity?.finish()
                    }
                    .addOnFailureListener { Toast.makeText(requireContext(), "تعذر الحذف الان", Toast.LENGTH_SHORT).show() }
            }
            builder.setNegativeButton("لا"){_, _ -> }
            builder.setTitle(" حذف ${args.currentProject.projectTitle}")
            builder.setMessage(" هل انت متأكد من حذف ${args.currentProject.projectTitle}")
            builder.context.setTheme(R.style.myDialogTheme)
            builder.create().show()
        }

        return view
    }

}