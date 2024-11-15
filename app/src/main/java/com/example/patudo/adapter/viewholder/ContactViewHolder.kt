package com.example.patudo.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patudo.R

class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textNome = view.findViewById<TextView>(R.id.text_nome)
    val imageProfile = view.findViewById<ImageView>(R.id.image_profile)


}