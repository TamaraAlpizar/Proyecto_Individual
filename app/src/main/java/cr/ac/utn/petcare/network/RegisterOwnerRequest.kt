package cr.ac.utn.petcare.network

data class RegisterOwnerRequest(
    val name: String,
    val fLastName: String,
    val sLastName: String,
    val email: String,
    val password: String,
    val phone: String,
    val address: String
)
