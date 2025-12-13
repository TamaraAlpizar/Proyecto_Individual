package cr.ac.utn.petcare.network

data class PetDto(
    val id: String? = null,
    val petName: String,
    val breed: String,
    val gender: String,
    val age: Int,
    val weight: Double,
    val ownerId: String,
    val notes: String,
    val photo: String? = null
)