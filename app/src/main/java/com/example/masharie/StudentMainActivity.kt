package com.example.masharie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class StudentMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        val mainNav = findViewById<BottomNavigationView>(R.id.studentBottomNavigationView)
        val navController = findNavController(R.id.studentNavController)
        mainNav.setupWithNavController(navController)
    }
}