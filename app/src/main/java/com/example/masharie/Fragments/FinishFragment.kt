package com.example.masharie.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.masharie.MainActivity
import com.example.masharie.R
import com.example.masharie.StudentMainActivity

class FinishFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_finish, container, false)

        view.findViewById<Button>(R.id.finishBackToHomeBtn).setOnClickListener {
            startActivity(Intent(requireContext(), StudentMainActivity::class.java))
            activity?.finish()
        }

        return view
    }

}