package com.example.patudo.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.patudo.adapter.listener.ContactOnClickListener
import com.example.patudo.adapter.viewholder.ContactViewHolder
import com.example.patudo.model.ContactModel

class ContactListAdapter(private val contactList: List<ContactModel>,
    private val contactOnClickListener: ContactOnClickListener
): RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}