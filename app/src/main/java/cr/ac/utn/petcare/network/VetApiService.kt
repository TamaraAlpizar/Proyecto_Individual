package cr.ac.utn.petcare.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VetApiService {


    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): OwnerDto

    @POST("owners")
    suspend fun registerOwner(
        @Body request: RegisterOwnerRequest
    ): OwnerDto


    @GET("owners")
    suspend fun getOwners(): List<OwnerDto>

    @PUT("owners/{id}")
    suspend fun updateOwner(
        @Path("id") id: String,
        @Body body: UpdateOwnerRequest
    ): OwnerDto
    @POST("pets")
    suspend fun createPet(@Body pet: PetDto): PetDto
    @GET("pets")
    suspend fun getPets(): List<PetDto>
    @PUT("pets/{id}")
    suspend fun updatePet(
        @Path("id") id: String,
        @Body body: UpdatePetRequest
    ): PetDto

    @DELETE("pets/{id}")
    suspend fun deletePet(
        @Path("id") id: String
    )

    @POST("appointments")
    suspend fun createAppointment(
        @Body appointment: AppointmentDto
    ): AppointmentDto
}