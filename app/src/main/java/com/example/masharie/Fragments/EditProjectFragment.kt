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
import androidx.navigation.fragment.navArgs
import com.example.masharie.Data.ProjectData
import com.example.masharie.MainActivity
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.Utils.loadingDialogStart
import com.example.masharie.databinding.FragmentEditProjectBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EditProjectFragment : Fragment() {
    //اضافة الفيو بايندنك
    private var _binding: FragmentEditProjectBinding? = null
    private val binding get() = _binding!!
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore.collection("Projects")
    // تعريف الاركز
    private val args by navArgs<EditProjectFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // تعريف البايندنك
        _binding = FragmentEditProjectBinding.inflate(inflater, container, false)
        val view = binding.root

        // جلب ايدي المشروع الخاص بقاعدة البيانات نفسها
        var projectDbId = ""
        db.whereEqualTo("projectId", args.editCurrentProject.projectId).get().addOnSuccessListener { doc ->
            for (d in doc) {
                projectDbId = d.id
            }
        }

        binding.apply {
            editTextProjectTitle.setText(args.editCurrentProject.projectTitle)
            editTextProjectDetails.setText(args.editCurrentProject.projectDetails)
            editTextProjectNotes.setText(args.editCurrentProject.projectNotes)

            updateProjectBtn.setOnClickListener {
                // جلب المعلومات الموجودة داخل الحقول لاضافتها لقاعدة البيانات
                val title = editTextProjectTitle.text?.trim().toString()
                val details = editTextProjectDetails.text?.trim().toString()
                val notes = editTextProjectNotes.text?.trim().toString()
                val projectId = args.editCurrentProject.projectId
                val teacherId = args.editCurrentProject.teacherId


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
                    val myData = ProjectData( projectId,teacherId, title, details, notes )
                    db.document(projectDbId).set(myData).addOnCompleteListener { task ->
                        if (task.isSuccessful) { // في حالة تم التعديل بدون مشاكل
                            // غلق دايلوك التحميل
                            loadingDialogEnd()
                            // اغلاق الكيبورد حتى لا يبقى فعال عند التحويل الى  الرئيسية
                            closeKeyboard()
                            // عمل انتنت للتحويل الى الصفحة الرئيسية
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                            // اضهار رسالة الانتهاء
                            Toast.makeText(requireContext(), "تم تعديل المشروع بنجاح", Toast.LENGTH_SHORT).show()
                        } else { // في حالة تعذر التعديل او حدوث مشكلة
                            // غلق دايلوك التحميل
                            loadingDialogEnd()
                            Toast.makeText(requireContext(), "تعذر تعديل المشروع , حاول مره اخرى", Toast.LENGTH_SHORT).show()
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