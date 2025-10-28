package Controller


import Data.PetMemoryDataManager
import Entity.Pet
import android.content.Context
import cr.ac.utn.petcare.R

class PetController  (private val context: Context) {

    private val dataManager = PetMemoryDataManager

    fun addPet(pet: Pet) {
        try {
            dataManager.add(pet)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgAddPet))
        }
    }

    fun updatePet(pet: Pet) {
        try {
            dataManager.update(pet)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgUpdatePet))
        }
    }

    fun getPets(): List<Pet> {
        try {
            return dataManager.getAll()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetPet))
        }
    }

    fun getById(id: String): Pet {
        return dataManager.getById(id)
            ?: throw Exception(context.getString(R.string.ErrorMsgGetPetById))
    }
}
