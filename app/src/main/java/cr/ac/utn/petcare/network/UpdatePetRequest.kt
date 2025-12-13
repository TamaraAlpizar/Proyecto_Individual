package cr.ac.utn.petcare.network

data class UpdatePetRequest(
    val petName: String,
    val breed: String,
    val gender: String,
    val age: Int,
    val weight: Double,
    val notes: String
)