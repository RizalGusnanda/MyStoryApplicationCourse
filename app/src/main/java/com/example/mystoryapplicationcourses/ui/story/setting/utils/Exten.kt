package com.example.mystoryapplicationcourses.ui.story.setting.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load

@BindingAdapter("loadImage")
fun ImageView.loadImage(url: String?) {
    this.load(url) {
        crossfade(true)
        placeholder(CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        })
    }
}