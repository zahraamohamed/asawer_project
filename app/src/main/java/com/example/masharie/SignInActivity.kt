package com.example.masharie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.masharie.Utils.loadingDialogEnd
import com.example.masharie.Utils.loadingDialogStart
import com.example.masharie.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    //اضافة الفيو بايندنك
    private lateinit var binding: ActivitySignInBinding
    // عمل انشيلايز للفايربيس اوث
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore.collection("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // عمل انستانس من الفيو بايندنك
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // عمل انستانس من الفايربيس اوث
        mAuth = FirebaseAuth.getInstance()

        binding.apply {

            // اولا عمل كلك لسنر لزر اضافة حساب وعند الضغط عليه القيام بعمل اضافة
            signInBtn.setOnClickListener {

                //اولا جلب المعلومات الموجودة داخل الحقول واضافتها لمتغيرات
                val email = editTextEmail.text?.trim().toString()
                val password = editTextPassword.text?.trim().toString()

                // هنا نقوم بالتأكد اذا تم ملء الحقول ام لا اي بمعنى فالديشن
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    editTextEmail.error = "يجب أدخال البريد الالكتروني وكلمة السر اولاً"
                    editTextEmail.requestFocus()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(email)) {
                    editTextEmail.error = "يجب أدخال البريد الالكتروني"
                    editTextEmail.requestFocus()
                    return@setOnClickListener
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError(" البريد الالكتروني غير صحيح")
                    editTextEmail.requestFocus()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(password)) {
                    editTextPassword.error = "يجب أدخال كلمة السر"
                    editTextPassword.requestFocus()
                    return@setOnClickListener
                } else if (password.length < 6){
                    editTextPassword.error = "يجب ان تكون كلمة السر اكثر من 6 احرف"
                    editTextPassword.requestFocus()
                    return@setOnClickListener
                } else {
                    // تشغيل دايلوك التحميل
                    loadingDialogStart(this@SignInActivity)

                    // القيام بعملية تسجيل الدخول وتحويل المستخدم الى الصفحة الرئيسية
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            db.whereEqualTo("email", email).get().addOnSuccessListener { result ->
                                for (n in result){
                                    val isStudent = n.data["student"]
                                    if (isStudent == false){
                                        loadingDialogEnd()
                                        val mainIntent = Intent(this@SignInActivity, MainActivity::class.java)
                                        startActivity(mainIntent)
                                        finish()
                                    } else {
                                        loadingDialogEnd()
                                        val studentMainIntent = Intent(this@SignInActivity, StudentMainActivity::class.java)
                                        startActivity(studentMainIntent)
                                        finish()
                                    }
                                }
                            }
                        } else {
                            loadingDialogEnd()
                            Toast.makeText(this@SignInActivity, "تعذر تسجيل الدخول , كلمة المرور او البريد الالكتروني غير صحيح", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


    }

}