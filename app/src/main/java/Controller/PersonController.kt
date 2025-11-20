package Controller

import Data.PersonMemoryDataManager
import Entity.Person
import android.content.Context
import cr.ac.utn.petcare.R

class PersonController(private val context: Context) {

    private val dataManager = PersonMemoryDataManager

    fun addPerson(person: Person) {
        try {
            dataManager.add(person)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgAddPerson))
        }
    }

    fun updatePerson(person: Person) {
        try {
            dataManager.update(person)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgUpdatePerson))
        }
    }

    fun getPeople(): List<Person> {
        try {
            return dataManager.getAll()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetPerson))
        }
    }

    fun getById(id: String): Person? {
        return dataManager.getById(id)
            ?: throw Exception(context.getString(R.string.ErrorMsgGetPersonById))
    }
}
