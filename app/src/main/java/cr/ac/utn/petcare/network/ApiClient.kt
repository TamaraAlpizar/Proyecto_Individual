package cr.ac.utn.petcare.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL =
        "https://petcare-rg-hjcydddmfpg2eud2.canadacentral-01.azurewebsites.net/"

    val vetApi: VetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VetApiService::class.java)
    }
}