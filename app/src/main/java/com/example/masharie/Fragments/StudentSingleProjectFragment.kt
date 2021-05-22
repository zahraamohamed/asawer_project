package com.example.masharie.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.masharie.Data.NotificationData
import com.example.masharie.MainActivity
import com.example.masharie.R
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.Utils.loadingDialogStart
import com.example.masharie.databinding.FragmentSingleProjectBinding
import com.example.masharie.databinding.FragmentStudentSingleProjectBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class StudentSingleProjectFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentStudentSingleProjectBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore
    // تعريف الاركز
    private val args by navArgs<StudentSingleProjectFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // تعريف البايندنك
        _binding = FragmentStudentSingleProjectBinding.inflate(inflater, container, false)
        val view = binding.root

        //جلب معلومات اليوزر المسجل الدخول
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser

        binding.apply {
            TextViewProjectTitle.text = args.cProject.projectTitle
            TextViewProjectDetails.text = args.cProject.projectDetails
            TextViewProjectNotes.text = args.cProject.projectNotes

            takeProjectBtn.setOnClickListener {
                // تشغيل دايلوك التحميل
                loadingDialogStart(requireActivity())
                //جلب معلومات النوتفكشن وثم اضافتها لقاعدة البيانات
                db.collection("User").whereEqualTo("email", currentUser?.email.toString()).get()
                    .addOnSuccessListener { doc ->
                        for (d in doc){
                            val studentName = d.data["fullName"].toString()
                            Log.d("haider", studentName)
                            val notiData = NotificationData(currentUser?.uid.toString(), studentName, args.cProject.teacherId, args.cProject.projectTitle)
                            db.collection("Notifications").add(notiData).addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    // غلق دايلوك التحميل
                                    loadingDialogEnd()
                                    // التحويل الى صفحة الانتهاء
                                    findNavController().navigate(StudentSingleProjectFragmentDirections.actionStudentSingleProjectFragmentToFinishFragment())

                                } else {
                                    // غلق دايلوك التحميل
                                    loadingDialogEnd()
                                    // اضهار رسالة الانتهاء
                                    Toast.makeText(requireContext(), "تعذر حجز المشروع حاول مجدداً", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

            }
        }

        return view
    }

}