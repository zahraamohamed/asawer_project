package com.example.masharie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    // عمل انشيلايز للفايرستور لكولكشن اليوزر
    private val db = Firebase.firestore.collection("User")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // هنا سوينه انستانس للفاير بيس وجلبنا اليوزر حتى نتأكد منه
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            // هاي الدالة تتأكد اذا جان مسجل دخول تحوله للداش بورد واذا لا تحوله لصفحة التسجيل
            if (currentUser != null){
                val currentEmail = currentUser.email
                db.whereEqualTo("email", currentEmail).get().addOnSuccessListener { result ->
                    for (n in result){
                        val isStudent = n.data["student"]
                        if (isStudent == false){
                            val mainIntent = Intent(this, MainActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            val studentMainIntent = Intent(this, StudentMainActivity::class.java)
                            startActivity(studentMainIntent)
                            finish()
                        }
                    }
                }

            } else {
                val loginIntent = Intent(this, SignInActivity::class.java)
                startActivity(loginIntent)
                finish()
            }

        },5000)

    }
}