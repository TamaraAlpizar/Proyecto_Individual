package Data

import Entity.Pet

object PetMemoryDataManager : IPetDataManager {

    private val petList = mutableListOf<Pet>()

    override fun add(pet: Pet) {
        petList.add(pet)
    }

    override fun update(pet: Pet) {
        remove(pet.id)
        add(pet)
    }

    override fun remove(id: String) {
        petList.removeIf { it.id.trim() == id.trim() }
    }

    override fun getAll(): List<Pet> = petList.toList()

    override fun getById(id: String): Pet? {
        return petList.find { it.id.trim() == id.trim() }
    }

    override fun getByOwnerId(ownerId: String): List<Pet> {
        return petList.filter { it.OwnerId.trim() == ownerId.trim() }
    }
}