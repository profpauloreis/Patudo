package com.example.patudo.adapter.listener

import com.example.patudo.model.ContactModel

class ContactOnClickListener(val clickListener: (contact: ContactModel) -> Unit) {
    fun onClick(contact: ContactModel) = clickListener(contact)

}
