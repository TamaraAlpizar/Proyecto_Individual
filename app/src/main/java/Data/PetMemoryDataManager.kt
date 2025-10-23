package Data

import Entity.Pet

object PetMemoryDataManager : IPetDataManager {
    private var petList = mutableListOf<Pet>()

    override fun add(pet: Pet) {
        petList.add(pet)
    }

    override fun update(pet: Pet) {
        remove(pet.Id)
        add(pet)
    }

    override fun remove(id: String) {
        petList.removeIf { it.Id.trim() == id.trim() }
    }

    override fun getAll(): List<Pet> = petList.toList()

    override fun getById(id: String): Pet? {
        return petList.find { it.Id.trim() == id.trim() }
    }
}