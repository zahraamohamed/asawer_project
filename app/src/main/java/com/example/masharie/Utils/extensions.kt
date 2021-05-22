package com.example.masharie.Utils

import android.app.Activity
import android.app.AlertDialog
import com.example.masharie.R

lateinit var dialog: AlertDialog
fun loadingDialogStart(activity: Activity) {
    val builder = AlertDialog.Builder(activity)
    builder.setView(R.layout.loading_dialog)
    builder.setCancelable(false)
    dialog = builder.create()
    dialog.show()
}
fun loadingDialogEnd(){
    dialog.dismiss()
}
