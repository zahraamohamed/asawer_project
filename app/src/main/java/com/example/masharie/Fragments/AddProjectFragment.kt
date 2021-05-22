package com.example.masharie.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.masharie.Data.ProjectData
import com.example.masharie.MainActivity
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.Utils.loadingDialogStart
import com.example.masharie.databinding.FragmentAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class AddFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // تعريف البايندنك
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root

        //جلب معلومات اليوزر المسجل الدخول
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser

        binding.apply {
            // وضع لسنر للزر للقيام بأضافة المعلومات عند الضغط عليه
            addNewProjectBtn.setOnClickListener {
                // جلب المعلومات الموجودة داخل الحقول لاضافتها لقاعدة البيانات
                val title = editTextProjectTitle.text?.trim().toString()
                val details = editTextProjectDetails.text?.trim().toString()
                val notes = editTextProjectNotes.text?.trim().toString()
                val teacherId = currentUser?.uid.toString()

                // انشاء اي دي عشوائي للبروجكت
                val projectId = Random().nextInt((999000000 - 1000000)) + 1000000

                //القيام بلتحقق من الحقول
                if (TextUtils.isEmpty(title)) {
                    editTextProjectTitle.error = "يجب أدخال عنوان المشروع"
                    editTextProjectTitle.requestFocus()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(details)) {
                    editTextProjectDetails.error = "يجب أدخال تفاصيل المشروع"
                    editTextProjectDetails.requestFocus()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(notes)) {
                    editTextProjectNotes.error = "يجب أدخال الملاحظات على المشروع"
                    editTextProjectNotes.requestFocus()
                    return@setOnClickListener
                } else {
                    // تشغيل دايلوك التحميل
                    loadingDialogStart(requireActivity())
                    //وضع البيانات التي تم جلبها داخل الداتا كلاس
                    val myData = ProjectData(projectId.toString(), teacherId, title, details, notes )
                    db.collection("Projects").add(myData).addOnCompleteListener { task ->
                        if (task.isSuccessful) { // في حالة تم الاضافة بدون مشاكل
                            // غلق دايلوك التحميل
                            loadingDialogEnd()
                            // اغلاق الكيبورد حتى لا يبقى فعال عند التحويل الى  الرئيسية
                            closeKeyboard()
                            // عمل انتنت للتحويل الى الصفحة الرئيسية
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                            // اضهار رسالة الانتهاء
                            Toast.makeText(requireContext(), "تم اضافة المشروع بنجاح", Toast.LENGTH_SHORT).show()
                        } else { // في حالة تعذر الاضافة او حدوث مشكلة
                            // غلق دايلوك التحميل
                            loadingDialogEnd()
                            Toast.makeText(requireContext(), "تعذر اضافة المشروع , حاول مره اخرى", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        return view
    }
    private fun closeKeyboard(){
        val view = view?.findFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}