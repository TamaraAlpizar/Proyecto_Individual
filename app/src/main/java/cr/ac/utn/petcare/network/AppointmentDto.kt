package cr.ac.utn.petcare.network

data class AppointmentDto(
    val id: String? = null,
    val petId: String,
    val serviceType: String,
    val date: String,
    val hour: String,
    val notes: String? = null,
    val status: String? = null
)