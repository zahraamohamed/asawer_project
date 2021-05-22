package com.example.masharie.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
        val teacherId : String? = null,
        val fullName : String? = null,
        val email : String? = null,
        val password : String? = null,
        val isStudent : Boolean? = null
): Parcelable