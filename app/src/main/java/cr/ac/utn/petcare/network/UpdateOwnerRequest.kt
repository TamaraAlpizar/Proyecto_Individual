package cr.ac.utn.petcare.network

data class UpdateOwnerRequest(
    val name: String,
    val fLastName: String,
    val sLastName: String,
    val email: String,
    val phone: String,
    val address: String
)