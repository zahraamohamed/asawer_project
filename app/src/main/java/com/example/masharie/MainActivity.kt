package com.example.masharie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.masharie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mainNav = binding.mainNavBottom
        val navController = findNavController(R.id.mainNavController)
        mainNav.setupWithNavController(navController)

        binding.fab.setOnClickListener{
            navController.navigate(R.id.addProjectFragment)
        }


    }
}