package Data

import Entity.Pet

interface IPetDataManager {
    fun add(pet: Pet)
    fun update(pet: Pet)
    fun remove(id: String)
    fun getAll(): List<Pet>
    fun getById(id: String): Pet?
}