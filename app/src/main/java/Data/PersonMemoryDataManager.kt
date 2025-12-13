package Data

import Entity.Person

object PersonMemoryDataManager : IPersonDataManager {
    private val personList = mutableListOf<Person>()

    override fun add(person: Person) {
        personList.add(person)
    }

    override fun remove(id: String) {
        personList.removeIf { it.Id.trim() == id.trim() }
    }

    override fun update(person: Person) {
        val index = personList.indexOfFirst { it.Id == person.Id }
        if (index != -1) {
            personList[index] = person
        }
    }

    override fun getAll(): List<Person> {
        return personList
    }

    override fun getById(id: String): Person? {
        return personList.find { it.Id.trim() == id.trim() }
    }

    override fun getByFullName(fullName: String): Person? {
        return personList.find { it.FullName().trim() == fullName.trim() }
    }
    override fun getByEmail(email: String): Person? {
        return personList.find { it.Email.trim().equals(email.trim(), ignoreCase = true) }
    }
}
