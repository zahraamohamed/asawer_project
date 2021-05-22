package com.example.masharie.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProjectData (
    val projectId : String? = null,
    val teacherId : String? = null,
    val projectTitle : String? = null,
    val projectDetails : String? = null,
    val projectNotes : String? = null
): Parcelable