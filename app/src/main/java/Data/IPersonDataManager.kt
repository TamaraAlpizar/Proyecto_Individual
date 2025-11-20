package Data

import Entity.Person


interface IPersonDataManager {
    fun add(person: Person)
    fun remove(id: String)
    fun update(person: Person)
    fun getAll(): List<Person>
    fun getById(id: String): Person?
    fun getByFullName(fullName: String): Person?
    fun getByEmail(email: String):  Person?

}