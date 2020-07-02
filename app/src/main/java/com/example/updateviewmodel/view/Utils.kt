package com.example.updateviewmodel.view

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.updateviewmodel.R
import com.example.updateviewmodel.model.SongSearch

class Utils {
    //cac phuong thuc, thuoc tinh static sen nam trong  companion object
    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(iv: ImageView, link: String?) {
            if (link == null || link.equals("")) {
                Glide.with(iv)
                        .load(R.drawable.aodai)
                        .into(iv)
            } else {
                Glide.with(iv)
                        .load(link)
                        .placeholder(R.drawable.aodai)
                        .error(R.drawable.aodai)
                        .into(iv)
            }
        }

        @JvmStatic
        @BindingAdapter("updateText")
        fun updateText(tv:TextView, value:String?){
            tv.text = value
        }

        @JvmStatic
        @BindingAdapter("notifyChangeSong")
        fun notifyChangeSong(rc:RecyclerView, values:MutableList<SongSearch>?){
            (rc.adapter as SongAdapter).setData(if (values == null) mutableListOf<SongSearch>() else values!! )
        }
    }
}