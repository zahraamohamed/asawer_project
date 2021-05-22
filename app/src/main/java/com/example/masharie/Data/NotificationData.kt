package com.example.masharie.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationData (
        val notiFrom : String? = null,
        val studentName: String?= null,
        val notiTo : String? = null,
        val projectTitle : String? = null,
        val accepted : Boolean? = null
): Parcelable