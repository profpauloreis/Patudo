package com.example.patudo.model

data class ContactModel(
    val id: Int=0,
    val nome: String ="",
    val endereco: String="",
    val email: String="",
    val telefone: Int=0,
    val imageId: Int=0
) {
    fun isValid(): Boolean {
        return nome.isNotBlank() &&
                telefone.toString().isNotBlank() &&
                email.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"))
    }

    // Formatação para exibição na ListView
    override fun toString(): String {
        return nome
    }

    companion object {
        fun validateEmail(email: String): Boolean {
            return email.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"))
        }
    }
}
